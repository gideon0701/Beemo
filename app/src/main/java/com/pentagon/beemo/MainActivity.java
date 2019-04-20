package com.pentagon.beemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pentagon.beemo.Fragment.AboutFragment;
import com.pentagon.beemo.Fragment.HumidityFragment;
import com.pentagon.beemo.Fragment.SettingsFragment;
import com.pentagon.beemo.Fragment.TempFragment;
import com.pentagon.beemo.Fragment.WeightFragment;
import com.pentagon.beemo.Model.ContactNumberModel;
import com.pentagon.beemo.Model.HistoryModel;
import com.pentagon.beemo.Model.ReportModel;
import com.pentagon.beemo.Model.SettingsModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MqttCallback {

    private ReportViewModel mReportViewModel;
    ReportModel mReportModel;
    HistoryModel mHistoryModel;
    MqttClient mClientReceive;
    MqttClient mClientSend;
    Gson mGson;

    Handler dialog;
    AlertDialog.Builder builder;
    ProgressDialog progress;
    PowerManager.WakeLock wakeLock;
    NotificationManager mNotificationManager;

    int mNavCurrentitemID;
    int intNotifAlertID = 2;
    final String mStrTopic = "bscpe_topics_hives/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"BEEMO::MyWakeLockTag");
        wakeLock.acquire(30*60*1000L /*10 minutes*/);
        initNotif();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mReportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        GsonBuilder gsonBuilder = new GsonBuilder();
        mGson = gsonBuilder.create();
        setupMqtt();
        //subscribe
        try {
            mClientReceive.subscribe(mStrTopic + "#",2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        builder =  new AlertDialog.Builder(this);
        progress = ProgressDialog.show(MainActivity.this,"LOADING!","please wait!");
        dialog = new Handler(){
            public  void handleMessage(Message message){
                progress = ProgressDialog.show(MainActivity.this,"Reconnecting","please wait!");
            }
        };
//        Intent startIntent = new Intent(getApplicationContext(), MyService.class);
//        startIntent.setAction("STARTSERVICE");
//        startService(startIntent);
        navigationView.getMenu().getItem(4).setChecked(true);
        showAboutFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLock.release();

        Intent startIntent = new Intent(getApplicationContext(), MyService.class);
        startIntent.setAction("STARTSERVICE");
        startService(startIntent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mNavCurrentitemID = item.getItemId();

        if (mNavCurrentitemID == R.id.nav_weight) {
            showWeightFragment();
        } else if(mNavCurrentitemID == R.id.nav_humidity) {
            showHumidityFragment();
        }else if(mNavCurrentitemID == R.id.nav_temp) {
            showTempFragment();
        }else if(mNavCurrentitemID == R.id.nav_settings) {
            showSettingsFragment();
        }else if(mNavCurrentitemID == R.id.nav_about) {
            showAboutFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showSettingsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        SettingsFragment settingsFragment = new SettingsFragment().newInstance();
        manager.beginTransaction().replace(
                R.id.content_main,
                settingsFragment,
                settingsFragment.getTag())
                .commit();
    }
    private void showAboutFragment() {
        FragmentManager manager = getSupportFragmentManager();
        AboutFragment aboutFragment = new AboutFragment().newInstance();
        manager.beginTransaction().replace(
                R.id.content_main,
                aboutFragment,
                aboutFragment.getTag())
                .commit();
    }

    private void showWeightFragment() {

        FragmentManager manager = getSupportFragmentManager();
        WeightFragment weightFragment = new WeightFragment().newInstance();
        manager.beginTransaction().replace(
                R.id.content_main,
                weightFragment,
                weightFragment.getTag())
                .commit();
    }

    private void showHumidityFragment() {

        FragmentManager manager = getSupportFragmentManager();
        HumidityFragment humidityFragment = new HumidityFragment().newInstance();
        manager.beginTransaction().replace(
                R.id.content_main,
                humidityFragment,
                humidityFragment.getTag())
                .commit();
    }

    private void showTempFragment() {

        FragmentManager manager = getSupportFragmentManager();
        TempFragment tempFragment = new TempFragment().newInstance();
        manager.beginTransaction().replace(
                R.id.content_main,
                tempFragment,
                tempFragment.getTag())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_viewNotifLog) {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("NOTIF_LOG",Context.MODE_PRIVATE);
            String strOldVal = prefs.getString("LOG","");
            if(strOldVal.equals("")) {
                showMessage("Notification Log","No Data Yet");
            }else {
                showMessage("Notification Log",strOldVal);
            }

            return true;
        }else if(id == R.id.action_viewHistory) {
            List<ReportModel> lstReportNodel = new ArrayList<>();
            lstReportNodel = mHistoryModel.getReports();
            StringBuilder stringBuilder = new StringBuilder();
            String strPreviousdDate = getPreviousDate();
            String strCurrentDate = getCurrentDate();
            for (ReportModel reportModel: lstReportNodel) {
                if(reportModel.getTimestamp().contains(strCurrentDate)
                        || reportModel.getTimestamp().contains(strPreviousdDate)) {
                    stringBuilder.append(showHistory(reportModel));
                }
            }
            if(stringBuilder.toString().equals("")) {
                showMessage("History", "No data for "+ strPreviousdDate +" and "+ strCurrentDate);
            }else {
                showMessage("History", stringBuilder.toString());
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String title, String Message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * TO SETUP MQTT
     */
    private void setupMqtt()
    {
        //m2m.eclipse.org (backup topic)
        final String broker = "tcp://iot.eclipse.org:1883";
        final String clientId = MqttClient.generateClientId();
        final MemoryPersistence persistence = new MemoryPersistence();
        final String clientid2 = MqttClient.generateClientId();

        try {
            mClientReceive = new MqttClient(broker, clientId, persistence);
            mClientSend = new MqttClient(broker, clientid2, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(300);
            mClientReceive.connect(options);
            mClientReceive.setCallback(this);

            mClientSend.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendSettingsData(SettingsModel settingsModel, ContactNumberModel contactNumberModel) {

        String jsonSettings = mGson.toJson(settingsModel);
        String jsonContacts = mGson.toJson(contactNumberModel);
        try {
            mClientSend.publish(mStrTopic + "settings",jsonSettings.getBytes(),2,true);
            mClientSend.publish(mStrTopic + "numbers",jsonContacts.getBytes(),2,true);
            Toast.makeText(getApplicationContext(),"SAVE!",Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void reconnect(){

        while (true) {
            Log.e("MQTT"," CONNECTING");
            try {
                mClientReceive.connect();
                mClientSend.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }

            if (mClientReceive.isConnected() && mClientSend.isConnected() ){

                try {
                    mClientReceive.setCallback(this);
                    mClientReceive.subscribe(mStrTopic + "#",2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Log.e("MQTT"," CONNECTED");
                progress.dismiss();
                Toast.makeText(MainActivity.this,"CONNECTED!",Toast.LENGTH_SHORT).show();
                break;

            }
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e("MESSAGE  "," LOST CONNECTION");
        dialog.sendEmptyMessage(0);
        reconnect();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        Log.e("MESSAGE  "," " + topic + ":   " + msg );

        if(topic.equals(mStrTopic + "reports/history")) {
            mHistoryModel =  mGson.fromJson(msg,HistoryModel.class);
            Log.e("MESSAGE  "," THIS IS HISTORY");
        }

        if(topic.equals(mStrTopic + "reports/live")) {
            Log.e("MESSAGE  "," THIS IS LIVE");
            mReportModel = mGson.fromJson(msg, ReportModel.class);
            mReportViewModel.reportModel = mReportModel;
            mReportViewModel.setWeightGraph();
            mReportViewModel.setHumidityGraph();
            mReportViewModel.setTemperatureGraph();

        }
        if(topic.equals(mStrTopic + "settings")){
            Log.e("MESSAGE  "," THIS IS SETTING");
            SettingsModel settingsModel =  mGson.fromJson(msg,SettingsModel.class);
            mReportViewModel.settingsModel = settingsModel;
        }

        if(topic.equals(mStrTopic + "numbers")){
            Log.e("MESSAGE  "," THIS IS CONTACTS");
            ContactNumberModel contactNumberModel = mGson.fromJson(msg,ContactNumberModel.class);
            mReportViewModel.contactNumberModel = contactNumberModel;
            progress.dismiss();
        }

        if(topic.equals(mStrTopic + "alerts")) {

            if(mReportViewModel.settingsModel.getParameters().getNotifStatus()) {
                sentNotification(msg,intNotifAlertID);
                saveToLog(msg);
                intNotifAlertID++;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private String showHistory(ReportModel reportModel) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(reportModel.getTimestamp());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("[HIVE 1]");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Weight: ");
        stringBuilder.append(reportModel.getHive1().getWeight());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Humidity:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive1().getHumidities().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive1().getHumidities().get(1));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Temperature:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive1().getTemperatures().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive1().getTemperatures().get(1));
        stringBuilder.append(System.getProperty("line.separator"));


        stringBuilder.append("[HIVE 2]");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Weight: ");
        stringBuilder.append(reportModel.getHive2().getWeight());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Humidity:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive2().getHumidities().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive2().getHumidities().get(1));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Temperature:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive2().getTemperatures().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive2().getTemperatures().get(1));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("[HIVE 3]");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Weight: ");
        stringBuilder.append(reportModel.getHive3().getWeight());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Humidity:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive3().getHumidities().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive3().getHumidities().get(1));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Temperature:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer1: ");
        stringBuilder.append(reportModel.getHive3().getTemperatures().get(0));
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tLayer2: ");
        stringBuilder.append(reportModel.getHive3().getTemperatures().get(1));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Outside:");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tHumid: ");
        stringBuilder.append(reportModel.getOutside().getHumidity());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("\tTemp: ");
        stringBuilder.append(reportModel.getOutside().getTemperature());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        return stringBuilder.toString();
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private String getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date c = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void initNotif()
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

    private void sentNotification(String message, int intNotifId) {
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
