package com.orbitsoftlabs.vitals.DiseaseUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.MedSolutionUtils.MedViewModel;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class DiseaseDetailedView extends AppCompatActivity {
    private Class aClass;
    private String diseaseName, diseaseDescription, diseaseMedications;
    private TextView tv_diseaseName, tv_diseaseDescription, tv_diseaseMedications;
    private Toolbar toolbar;

    private String last_activity;

    private RecyclerView recyclerView;

    private MedViewModel medViewModel;
    private ArrayList<MedViewModel> medViewModelArrayList;
    private DiseaseMedSolutionRecyclerViewAdapter recyclerViewAdapter;

    private LinearLayoutManager llm;

    private String documentId;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_disease);
        toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        diseaseName = i.getStringExtra("diseaseName");
        diseaseDescription = i.getStringExtra("diseaseDescription");
        //diseaseMedications = i.getStringExtra("medForIllness");

        tv_diseaseName = findViewById(R.id.diseaseName);
        tv_diseaseDescription = findViewById(R.id.diseaseDescription);
        //tv_diseaseMedications = findViewById(R.id.medForIllness);

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
        documentId = diseaseName.replace(" ", "");
        Log.d("docId", tv_diseaseName.getText().toString()+"");
        medViewModelArrayList = new ArrayList<>();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);


        llm.setItemPrefetchEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


    }

    @Override
    public void onStart(){
        super.onStart();
        tv_diseaseName.setText(diseaseName);
        tv_diseaseDescription.setText(diseaseDescription);
        //tv_diseaseMedications.setText(diseaseMedications);
        toolbar.setTitle(diseaseName);


        medViewModelArrayList.clear();
        firestoreDB.collection("diseases")
                .document(documentId)
                .collection("med_solutions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                medViewModel = documentSnapshot.toObject(MedViewModel.class);
                                medViewModelArrayList.add(medViewModel);

                                recyclerViewAdapter  = new DiseaseMedSolutionRecyclerViewAdapter(last_activity,getApplicationContext(),medViewModelArrayList);
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
        HelperUtil.backNavigationFinishLast(last_activity,this, Diseases.class);
    }
}
