package com.orbitsoftlabs.vitals.MedSolutionUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;

public class MedSolutionsDetailedView extends AppCompatActivity {
    private Class aClass;
    private String medName, medDescription, medForIllness;
    private TextView tv_medName, tv_medDescription, tv_medForIllness;
    private Toolbar toolbar;

    private String last_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_med_solutions);

        toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        medName = i.getStringExtra("medName");
        medDescription = i.getStringExtra("medDescription");
        medForIllness = i.getStringExtra("medForIllness");

        tv_medName = findViewById(R.id.medName);
        tv_medDescription = findViewById(R.id.medDescription);
        tv_medForIllness = findViewById(R.id.medForIllness);
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
        tv_medName.setText(medName);
        tv_medDescription.setText(medDescription);
        tv_medForIllness.setText("Relief for "+medForIllness);
        toolbar.setTitle(medName);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        //HelperUtil.backNavigationFinishLast(last_activity,this,MedicalSolutions.class);
    }

}
