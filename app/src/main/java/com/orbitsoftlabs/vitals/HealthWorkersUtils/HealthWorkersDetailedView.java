package com.orbitsoftlabs.vitals.HealthWorkersUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;

public class HealthWorkersDetailedView extends AppCompatActivity {
    private Class aClass;

    public String address, barangay, email, first_name, last_name , middle_name, messenger_username, mobile_no, user_id;

    private String lastIntent, last_activity;

    private TextView tv_address, tv_barangay, tv_email, tv_full_name, tv_mobile_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_workers_detailed_view);

        tv_address = findViewById(R.id.workerAddress);
        tv_barangay = findViewById(R.id.workerBarangay);
        tv_email = findViewById(R.id.workerEmail);
        tv_full_name = findViewById(R.id.workerName);
        tv_mobile_no = findViewById(R.id.workerMobileNo);

        try {
            Intent i = getIntent();
            last_activity = i.getStringExtra("activity");
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
        getIntentExtras();
        setDataToViews();
    }
    private void getIntentExtras(){
        Intent i = getIntent();
        address = i.getStringExtra("address");
        barangay = i.getStringExtra("barangay");
        email = i.getStringExtra("email");
        first_name = i.getStringExtra("first_name");
        last_name = i.getStringExtra("last_name");
        messenger_username = i.getStringExtra("messenger_username");
        middle_name = i.getStringExtra("middle_name");
        mobile_no = i.getStringExtra("mobile_no");
        user_id = i.getStringExtra("user_id");

        last_activity = i.getStringExtra("activity");
    }

    private void setDataToViews(){
        tv_address.setText(address);
        tv_barangay.setText(barangay);
        tv_email.setText(email);
        tv_full_name.setText(first_name+" "+middle_name+" "+last_name);
        tv_mobile_no.setText(mobile_no);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinishLast(last_activity,this, HealthWorkers.class);
    }

    public void callWorker(View v){
        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clinicPhoneNo));
        //startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile_no));// Initiates the Intent
        startActivity(intent);
    }

    public void messageWorker(View v){
        String number = mobile_no;  // The number on which you want to send SMS
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
    }

    public void messengerWorker(View v){
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://m.me/"+messenger_username));
        startActivity(browserIntent);
    }
}
