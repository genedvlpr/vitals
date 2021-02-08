package com.orbitsoftlabs.vitals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    private static int TIME_OUT = 400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HelperUtil.checkUserExistence(Splashscreen.this);
            }
        }, TIME_OUT);
    }
}
