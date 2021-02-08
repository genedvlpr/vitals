package com.orbitsoftlabs.vitals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.RegistrationUtils.RegistrationEmailPassword;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    private TextInputLayout tl1, tl2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tl1 = findViewById(R.id.tl_1);
        tl2 = findViewById(R.id.tl_2);

        HelperUtil.firebaseInitialization(this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Login.this.finish();
    }

    public void login(View v){
        HelperUtil.authentication(this, HomeHealthWorker.class,tl1,tl2);
    }

    public void register(View v){
        HelperUtil.nextActivity(this, RegistrationEmailPassword.class);
    }

    public void guest(View v){
        HelperUtil.nextActivity(this, HomeConstituents.class);
    }
}
