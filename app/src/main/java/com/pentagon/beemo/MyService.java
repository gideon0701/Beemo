package com.pentagon.beemo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pentagon.beemo.Model.SettingsModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyService extends Service implements MqttCallback {
    static final int NOTIFICATION_ID = 0;

    public static boolean isServiceRunning = false;
    MqttClient mClientReceive;
    NotificationManager mNotificationManager;
    final String mStrTopic = "bscpe_topics_hives/";
    Gson mGson;
    SettingsModel mSettingsModel;
    int intNotifAlertID = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        initNotif();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startMyOwnForeground();
        }else {
            startServiceWithNotification();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        mGson = gsonBuilder.create();
        mSettingsModel = new SettingsModel();
        Log.d("DEBUGBEEMO","intent created");
        setupMqtt();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals("STARTSERVICE")) {
            startServiceWithNotification();
        }
        else stopMyService();
        return START_STICKY;
    }

    // In case the service is deleted or crashes some how
    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    void startServiceWithNotification() {
        if (isServiceRunning) return;
        isServiceRunning = true;

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction("NOTIFICATION");  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.my_string))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.pentagon.beemo";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);
    }

    void stopMyService() {
        stopForeground(true);
        stopSelf();
        isServiceRunning = false;
    }

    void initNotif()
    {
        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "ALERT",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }
    }
    void sentNotification(String message, int intNotifId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle("Alert!");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Alert!");
        mBuilder.setContentText(message);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager.notify(intNotifId, mBuilder.build());
    }

    private void setupMqtt()
    {
        //m2m.eclipse.org (backup topic)
        final String broker = "tcp://broker.hivemq.com:1883";
        final String clientId = MqttClient.generateClientId();
        final MemoryPersistence persistence = new MemoryPersistence();

        try {
            mClientReceive = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(300);
            mClientReceive.connect(options);
            mClientReceive.setCallback(this);
            mClientReceive.subscribe(mStrTopic + "#",2);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void reconnect(){

        while (true) {
            Log.e("MQTT"," CONNECTING");
            try {

                mClientReceive.connect();

                if (mClientReceive.isConnected()){
                    mClientReceive.setCallback(this);
                    mClientReceive.subscribe(mStrTopic + "#",2);
                    Log.e("MQTT"," CONNECTED");
                    break;

                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void connectionLost(Throwable cause) {
//        sentNotification("Reconnecting",0);
        reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        if(topic.equals(mStrTopic + "settings")){
            mSettingsModel =  mGson.fromJson(msg,SettingsModel.class);
        }

        if(topic.equals(mStrTopic + "alerts")) {

            if(mSettingsModel.getParameters().getNotifStatus()) {
                sentNotification(msg,intNotifAlertID);
                saveToLog(msg);
                intNotifAlertID++;
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void saveToLog(String msg){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("NOTIF_LOG",Context.MODE_PRIVATE);
        String strOldVal = prefs.getString("LOG","");
        StringBuilder sb = new StringBuilder();
        sb.append(strOldVal);
        sb.append(msg);
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LOG",sb.toString());
        editor.apply();
    }
}