package com.orbitsoftlabs.vitals.PatientUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.PatientUtils.PatientListUtils.Patients;
import com.orbitsoftlabs.vitals.R;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

    }

    public void patient_list(View v){
        HelperUtil.nextActivity(this, Patients.class);
    }
    public void patient_add(View v){
        HelperUtil.nextActivity(this, PatientAdd.class);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this,HomeHealthWorker.class);
    }
}
