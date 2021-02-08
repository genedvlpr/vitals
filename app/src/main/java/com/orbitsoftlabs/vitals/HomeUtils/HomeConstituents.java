package com.orbitsoftlabs.vitals.HomeUtils;

import android.os.Bundle;

import com.orbitsoftlabs.vitals.About;
import com.orbitsoftlabs.vitals.AssistantUtils.HealthCareAssistant;
import com.orbitsoftlabs.vitals.ClinicUtils.Clinics;
import com.orbitsoftlabs.vitals.DiseaseUtils.Diseases;
import com.orbitsoftlabs.vitals.HealthWorkersUtils.HealthWorkers;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.MedSolutionUtils.MedicalSolutions;
import com.orbitsoftlabs.vitals.R;
import com.orbitsoftlabs.vitals.TermsAndConditions;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class HomeConstituents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_constituents);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        HelperUtil.destroyActivity(HomeConstituents.this);
        //android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }

    @Override
    public void onPause(){
        super.onPause();
        HelperUtil.destroyActivity(HomeConstituents.this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        HelperUtil.destroyActivity(HomeConstituents.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_constituents, menu);
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
            HelperUtil.nextActivity(HomeConstituents.this, MedicalSolutions.class);
        } else if (id == R.id.brgy_workers) {
            HelperUtil.nextActivity(HomeConstituents.this, HealthWorkers.class);
        } else if (id == R.id.nav_diseases) {
            HelperUtil.nextActivity(HomeConstituents.this, Diseases.class);
        } else if (id == R.id.nav_health_care_assistant) {
            HelperUtil.nextActivity(HomeConstituents.this, HealthCareAssistant.class);
        } else if (id == R.id.nav_terms_condition) {
            HelperUtil.nextActivity(HomeConstituents.this, TermsAndConditions.class);
        } else if (id == R.id.clinic_location) {
            HelperUtil.nextActivity(this, Clinics.class);
        } else if (id == R.id.about) {
            HelperUtil.nextActivity(this, About.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void med_solution_cons(View v){
        HelperUtil.nextActivity(this, MedicalSolutions.class);
    }
    public void brgy_workers_cons(View v){
        HelperUtil.nextActivity(this, HealthWorkers.class);
    }
    public void diseases_cons(View v){
        HelperUtil.nextActivity(this, Diseases.class);
    }
    public void health_care_assistant_cons(View v){
        HelperUtil.nextActivity(this, HealthCareAssistant.class);
    }
    public void clinic_location_cons(View v){
        HelperUtil.nextActivity(this, Clinics.class);
    }
    public void about_cons(View v){
        HelperUtil.nextActivity(this, About.class);
    }

}
