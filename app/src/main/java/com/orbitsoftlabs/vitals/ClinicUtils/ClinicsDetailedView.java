package com.orbitsoftlabs.vitals.ClinicUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.orbitsoftlabs.vitals.R;

public class ClinicsDetailedView extends AppCompatActivity {

    private Class aClass;
    private String clinicName, clinicAddress, clinicBarangay, clinicPhoneNo;
    private TextView tv_clinicName, tv_clinicAddress, tv_clinicBarangay, tv_clinicPhoneNo;
    private Toolbar toolbar;

    private String last_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics_detailed_view);
        toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        clinicName = i.getStringExtra("clinicName");
        clinicAddress = i.getStringExtra("clinicAddress");
        clinicBarangay = i.getStringExtra("clinicBarangay");
        clinicPhoneNo = i.getStringExtra("clinicPhoneNo");

        tv_clinicName = findViewById(R.id.clinicName);
        tv_clinicAddress = findViewById(R.id.clinicAddress);
        tv_clinicBarangay = findViewById(R.id.clinicBarangay);
        tv_clinicPhoneNo = findViewById(R.id.clinicPhoneNo);

        last_activity = i.getStringExtra("activity");
        try {
            //Intent lastIntent = getIntent();

            Log.d("Last activity", last_activity+"");

            if (last_activity.contains("Worker")){
                aClass = HomeHealthWorker.class;
            }   else {
                aClass = HomeConstituents.class;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        tv_clinicName.setText(clinicName);
        tv_clinicAddress.setText(clinicAddress);
        tv_clinicBarangay.setText(clinicBarangay);
        tv_clinicPhoneNo.setText(clinicPhoneNo);
        toolbar.setTitle(clinicName);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        //HelperUtil.backNavigationFinishLast(last_activity,this,MedicalSolutions.class);
    }

    public void googleMaps(View v){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+clinicAddress+"");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void call(View v){
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clinicPhoneNo));
        //startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clinicPhoneNo));// Initiates the Intent
        startActivity(intent);
    }

    public void message(View v){
        String number = clinicPhoneNo;  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }
}
