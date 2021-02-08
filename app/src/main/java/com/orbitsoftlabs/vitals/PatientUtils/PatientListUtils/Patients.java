package com.orbitsoftlabs.vitals.PatientUtils.PatientListUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.PatientUtils.PatientActivity;
import com.orbitsoftlabs.vitals.PatientUtils.PatientAdd;
import com.orbitsoftlabs.vitals.PatientUtils.PatientsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.orbitsoftlabs.vitals.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class Patients extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientsViewModel patientsViewModel;
    private ArrayList<PatientsViewModel> patientsViewModelArrayList;
    private PatientsRecyclerViewAdapter recyclerViewAdapter;

    private LinearLayoutManager llm;

    private Toolbar toolbar;

    private String diseases_list_id;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        patientsViewModelArrayList = new ArrayList<>();
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            HelperUtil.nextActivity(Patients.this, PatientAdd.class);
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        patientsViewModelArrayList.clear();
        firestoreDB.collection("patient_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                patientsViewModel = document.toObject(PatientsViewModel.class);
                                patientsViewModelArrayList.add(patientsViewModel);

                                Log.d("Patients", ""+(String) document.get("patientFName"));

                                recyclerViewAdapter = new PatientsRecyclerViewAdapter(getApplicationContext(), patientsViewModelArrayList);
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
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this, PatientActivity.class);
    }
}
