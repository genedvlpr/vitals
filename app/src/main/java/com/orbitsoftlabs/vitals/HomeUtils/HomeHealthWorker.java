package com.orbitsoftlabs.vitals.HomeUtils;

import android.os.Bundle;

import com.orbitsoftlabs.vitals.About;
import com.orbitsoftlabs.vitals.AssistantUtils.HealthCareAssistant;
import com.orbitsoftlabs.vitals.ChartActivity;
import com.orbitsoftlabs.vitals.ClinicUtils.Clinics;
import com.orbitsoftlabs.vitals.DiseaseUtils.Diseases;
import com.orbitsoftlabs.vitals.HealthWorkersUtils.HealthWorkers;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.Login;
import com.orbitsoftlabs.vitals.MedSolutionUtils.MedicalSolutions;
import com.orbitsoftlabs.vitals.PatientUtils.PatientActivity;
import com.orbitsoftlabs.vitals.R;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.orbitsoftlabs.vitals.TermsAndConditions;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import static com.orbitsoftlabs.vitals.HelperUtil.mAuth;

public class HomeHealthWorker extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name, barangay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_health_worker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View v = navigationView.getHeaderView(0);
        name = v.findViewById(R.id.fName);
        barangay = v.findViewById(R.id.barangay);

        HelperUtil.displayUserCredentials(this, name, barangay);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeHealthWorker.this.finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_health_worker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            HelperUtil.nextActivity(this, About.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_medical_solution) {
            // Handle the camera action
            HelperUtil.nextActivity(this, MedicalSolutions.class);
        } else if (id == R.id.brgy_workers) {
            HelperUtil.nextActivity(this, HealthWorkers.class);
        } else if (id == R.id.nav_diseases) {
            HelperUtil.nextActivity(this, Diseases.class);
        } else if (id == R.id.nav_health_care_assistant) {
            HelperUtil.nextActivity(this, HealthCareAssistant.class);
        } else if (id == R.id.nav_persons_diseased) {
            HelperUtil.nextActivity(this, PatientActivity.class);
        } else if (id == R.id.clinic_location) {
            HelperUtil.nextActivity(this, Clinics.class);
        } else if (id == R.id.nav_log_out) {
            mAuth.signOut();
            HelperUtil.nextActivity(this, Login.class);
        } else if (id == R.id.nav_terms_condition) {
            HelperUtil.nextActivity(HomeHealthWorker.this, TermsAndConditions.class);
        } else if (id == R.id.about) {
            HelperUtil.nextActivity(this, About.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void med_solution(View v){
        HelperUtil.nextActivity(this, MedicalSolutions.class);
    }
    public void brgy_workers(View v){
        HelperUtil.nextActivity(this, HealthWorkers.class);
    }
    public void diseases(View v){
        HelperUtil.nextActivity(this, Diseases.class);
    }
    public void health_care_assistant(View v){
        HelperUtil.nextActivity(this, HealthCareAssistant.class);
    }
    public void new_patient(View v){
        HelperUtil.nextActivity(this, PatientActivity.class);
    }
    public void clinic_location(View v){
        HelperUtil.nextActivity(this, Clinics.class);
    }
    public void about(View v){
        HelperUtil.nextActivity(this, About.class);
    }


}
