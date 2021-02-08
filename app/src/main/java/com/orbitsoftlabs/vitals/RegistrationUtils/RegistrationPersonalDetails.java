package com.orbitsoftlabs.vitals.RegistrationUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationPersonalDetails extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextInputLayout mobileNo, messengerUsername, barangay, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personal_details);

        progressBar = findViewById(R.id.prg_loading);
        mobileNo = findViewById(R.id.tl_1);
        messengerUsername = findViewById(R.id.tl_2);
        barangay = findViewById(R.id.tl_3);
        address = findViewById(R.id.tl_4);
    }

    public void registerPersonalDetails(View v){
        HelperUtil.registerPersonalDetails(this, HomeHealthWorker.class,mobileNo,messengerUsername,barangay,address,progressBar,progressBar);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        HelperUtil.backNavigationWithAlert(this, "Alert","You need to finish and fill up the necessary informations, when all fields are filled up, click Finish");
    }
}
