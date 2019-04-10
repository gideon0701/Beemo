package com.pentagon.beemo.Fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.pentagon.beemo.MainActivity;
import com.pentagon.beemo.Model.ContactNumberModel;
import com.pentagon.beemo.Model.Parameters;
import com.pentagon.beemo.Model.SettingsModel;
import com.pentagon.beemo.R;
import com.pentagon.beemo.ReportViewModel;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private ReportViewModel mReportViewModel;
    boolean blnSwitchValue;
    String strTempFrom;
    String strTempUpto;
    String strTempFrom2;
    String strTempUpto2;
    String strWeightHarvest;
    String strTimeSpan;
    String strContactNum;
    String strHumidMin;
    String strHumidMax;
    String strHumidMin2;
    String strHumidMax2;

    EditText editTextTempFrom;
    EditText editTextTempUpto;
    EditText editTextTempFrom2;
    EditText editTextTempUpto2;
    EditText editTextHarvestWeight;
    EditText editTextTimeSpan;
    EditText editTextContactNumber;
    EditText editTextHumidMax;
    EditText editTextHumidMin;
    EditText editTextHumidMax2;
    EditText editTextHumidMin2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportViewModel = ViewModelProviders.of(getActivity()).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        final ConstraintLayout layout = view.findViewById(R.id.layoutNotificationEnabled);
        Button buttonSave = view.findViewById(R.id.buttonSave);
        editTextTempFrom = view.findViewById(R.id.editTextTempFrom);
        editTextTempUpto = view.findViewById(R.id.editTextTempTo);
        editTextTempFrom2 = view.findViewById(R.id.editTextTempFrom2);
        editTextTempUpto2 = view.findViewById(R.id.editTextTempTo2);
        editTextTimeSpan = view.findViewById(R.id.editTextTimeSpan);
        editTextHarvestWeight = view.findViewById(R.id.editTextHarvestWeight);
        editTextContactNumber = view.findViewById(R.id.editTextContactNumber);
        editTextHumidMax = view.findViewById(R.id.editTextMaxHumid);
        editTextHumidMin = view.findViewById(R.id.editTextMinHumid);
        editTextHumidMax2 = view.findViewById(R.id.editTextMaxHumid2);
        editTextHumidMin2 = view.findViewById(R.id.editTextMinHumid2);

        Switch switchNotification = view.findViewById(R.id.switchNotification);

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                blnSwitchValue = isChecked;

                if(isChecked == true) {
                    layout.setVisibility(View.VISIBLE);
                    if(mReportViewModel.settingsModel != null) {
                        Parameters parameters = mReportViewModel.settingsModel.getParameters();
                        editTextTempFrom.setText(parameters.getMinTemp().toString());
                        editTextTempUpto.setText(parameters.getMaxTemp().toString());
                        editTextTempFrom2.setText(parameters.getMinTemp2().toString());
                        editTextTempUpto2.setText(parameters.getMaxTemp2().toString());
                        editTextHarvestWeight.setText(parameters.getHarvestWeight().toString());
                        editTextTimeSpan.setText(parameters.getTimeSpan().toString());
                        editTextHumidMax.setText(parameters.getMaxHumid().toString());
                        editTextHumidMin.setText(parameters.getMinHumid().toString());
                        editTextHumidMax2.setText(parameters.getMaxHumid2().toString());
                        editTextHumidMin2.setText(parameters.getMinHumid2().toString());
                    }
                    if(mReportViewModel.contactNumberModel != null) {
                        ContactNumberModel contactNumberModel = mReportViewModel.contactNumberModel;
                        StringBuilder strBuilderNumber = new StringBuilder();
                        for (String number: contactNumberModel.getNumbers()) {
                            strBuilderNumber.append(number);
                            strBuilderNumber.append(",");
                        }
                        strBuilderNumber.deleteCharAt(strBuilderNumber.toString().length() - 1);
                        editTextContactNumber.setText(strBuilderNumber.toString());
                    }

                }else {
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTempFrom = editTextTempFrom.getText().toString();
                strTempUpto = editTextTempUpto.getText().toString();
                strTempFrom2 = editTextTempFrom2.getText().toString();
                strTempUpto2 = editTextTempUpto2.getText().toString();
                strWeightHarvest = editTextHarvestWeight.getText().toString();
                strTimeSpan = editTextTimeSpan.getText().toString();
                strContactNum = editTextContactNumber.getText().toString();
                strHumidMax = editTextHumidMax.getText().toString();
                strHumidMin = editTextHumidMin.getText().toString();
                strHumidMax2 = editTextHumidMax2.getText().toString();
                strHumidMin2 = editTextHumidMin2.getText().toString();

                if(validateInputs()) {
                    SettingsModel settingsModel = new SettingsModel();
                    Parameters parameters = new Parameters();
                    parameters.setNotifStatus(blnSwitchValue);
                    parameters.setMaxTemp(Double.parseDouble(strTempUpto));
                    parameters.setMinTemp(Double.parseDouble(strTempFrom));
                    parameters.setMaxTemp2(Double.parseDouble(strTempUpto2));
                    parameters.setMinTemp2(Double.parseDouble(strTempFrom2));
                    parameters.setHarvestWeight(Double.parseDouble(strWeightHarvest));
                    parameters.setTimeSpan(Integer.parseInt(strTimeSpan));
                    parameters.setMaxHumid(Double.parseDouble(strHumidMax));
                    parameters.setMinHumid(Double.parseDouble(strHumidMin));
                    parameters.setMaxHumid2(Double.parseDouble(strHumidMax2));
                    parameters.setMinHumid2(Double.parseDouble(strHumidMin2));
                    settingsModel.setParameters(parameters);

                    ContactNumberModel contactNumberModel = new ContactNumberModel();
                    List<String> contactnumbers = Arrays.asList(strContactNum.split(","));
                    contactNumberModel.setNumbers(contactnumbers);

                    ((MainActivity)getActivity()).sendSettingsData(settingsModel,contactNumberModel);

                } else {
                    Toast.makeText(getContext(),"All items are required",Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(mReportViewModel.settingsModel != null) {
            Parameters parameters = mReportViewModel.settingsModel.getParameters();
            switchNotification.setChecked(parameters.getNotifStatus());
            editTextTempFrom.setText(parameters.getMinTemp().toString());
            editTextTempUpto.setText(parameters.getMaxTemp().toString());
            editTextTempFrom2.setText(parameters.getMinTemp2().toString());
            editTextTempUpto2.setText(parameters.getMaxTemp2().toString());
            editTextHarvestWeight.setText(parameters.getHarvestWeight().toString());
            editTextTimeSpan.setText(parameters.getTimeSpan().toString());
            editTextHumidMax.setText(parameters.getMaxHumid().toString());
            editTextHumidMin.setText(parameters.getMinHumid().toString());
            editTextHumidMax2.setText(parameters.getMaxHumid2().toString());
            editTextHumidMin2.setText(parameters.getMinHumid2().toString());
        }
        if(mReportViewModel.contactNumberModel != null) {
            ContactNumberModel contactNumberModel = mReportViewModel.contactNumberModel;
            StringBuilder strBuilderNumber = new StringBuilder();
            for (String number: contactNumberModel.getNumbers()) {
                strBuilderNumber.append(number);
                strBuilderNumber.append(",");
            }
            strBuilderNumber.deleteCharAt(strBuilderNumber.toString().length() - 1);
            editTextContactNumber.setText(strBuilderNumber.toString());
        }

        return view;
    }

    private boolean validateInputs() {
        boolean isValidate = true;

        if(strTempFrom.equals("")) {
            isValidate = false;
        }
        if(strTempUpto.equals("")) {
            isValidate = false;
        }
        if(strTempFrom2.equals("")) {
            isValidate = false;
        }
        if(strTempUpto2.equals("")) {
            isValidate = false;
        }
        if(strWeightHarvest.equals("")) {
            isValidate = false;
        }
        if(strTimeSpan.equals("")) {
            isValidate = false;
        }
        if(strContactNum.equals("")){
            isValidate = false;
        }
        if(strHumidMax.equals("")){
            isValidate = false;
        }
        if(strHumidMin.equals("")){
            isValidate = false;
        }
        if(strHumidMax2.equals("")){
            isValidate = false;
        }
        if(strHumidMin2.equals("")){
            isValidate = false;
        }
        return isValidate;
    }

}
