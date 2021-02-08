package com.orbitsoftlabs.vitals.RegistrationUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationName extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextInputLayout fName, mName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_name);

        progressBar = findViewById(R.id.prg_loading);
        fName = findViewById(R.id.tl_1);
        mName = findViewById(R.id.tl_2);
        lName = findViewById(R.id.tl_3);
    }

    public void registerName(View v){
        HelperUtil.registerName(this, RegistrationPersonalDetails.class,fName,mName,lName,progressBar,progressBar);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        HelperUtil.backNavigationWithAlert(this, "Alert","You need to finish and fill up the necessary informations, when all fields are filled up, click Finish");
    }
}
