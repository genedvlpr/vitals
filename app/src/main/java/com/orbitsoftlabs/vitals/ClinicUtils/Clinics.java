package com.orbitsoftlabs.vitals.ClinicUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;


public class Clinics extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private RecyclerView recyclerView;
    private ClinicViewModel clinicViewModel;
    private ArrayList<ClinicViewModel> clinicViewModelArrayList;
    private ClinicsRecyclerViewAdapter recyclerViewAdapter;

    private String [] doc1 = {"Amlodepine"};

    private LinearLayoutManager llm;

    private String last_activity;
    private Class aClass;
    private Toolbar toolbar;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinics);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clinicViewModelArrayList = new ArrayList<>();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setItemPrefetchEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        toolbar.setBackgroundColor( getResources().getColor(android.R.color.transparent) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

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
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this,aClass);
    }

    @Override
    public void onStart(){
        super.onStart();
        clinicViewModelArrayList.clear();
        firestoreDB.collection("clinics_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                clinicViewModel = documentSnapshot.toObject(ClinicViewModel.class);
                                clinicViewModelArrayList.add(clinicViewModel);

                                recyclerViewAdapter  = new ClinicsRecyclerViewAdapter(last_activity,getApplicationContext(),clinicViewModelArrayList);
                                recyclerViewAdapter.setHasStableIds(true);
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ErroCode", "Error fetching data.");
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
        {
            // Fully expanded
                //appBarLayout.setExpanded(false, true);
                //fab.setImageResource( R.drawable.ic_outline_fullscreen_exit_24px );
                toolbar.setBackgroundColor( getResources().getColor(R.color.colorPrimaryDark) );
                toolbar.setTitleTextColor(getResources().getColor(R.color.my_app_color_on_primary));
                //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_app_color_on_primary)));

        }
        else
        {
                //appBarLayout.setExpanded(true, true);
                //fab.setImageResource( R.drawable.ic_outline_fullscreen_24px );
                toolbar.setBackgroundColor( getResources().getColor(android.R.color.transparent) );
                toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
                //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_app_color_on_primary)));
                // Not fully expanded or collapsed
        }
    }
}
