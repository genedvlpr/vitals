package com.orbitsoftlabs.vitals.RegistrationUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.Login;
import com.orbitsoftlabs.vitals.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationEmailPassword extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextInputLayout email, pass, con_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_email_password);

        progressBar = findViewById(R.id.prg_loading);

        email = findViewById(R.id.tl_1);
        pass = findViewById(R.id.tl_2);
        con_pass = findViewById(R.id.tl_3);
    }

    public void registerEmail(View v){
        HelperUtil.registerUser(this,RegistrationName.class,email,pass,con_pass,progressBar,progressBar);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        HelperUtil.backNavigationFinish(this, Login.class);
    }
}
