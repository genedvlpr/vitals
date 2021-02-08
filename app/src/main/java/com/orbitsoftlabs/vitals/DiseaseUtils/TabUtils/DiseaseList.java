package com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orbitsoftlabs.vitals.DiseaseUtils.DiseaseRecyclerViewAdapter;
import com.orbitsoftlabs.vitals.DiseaseUtils.DiseaseViewModel;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class DiseaseList extends Fragment {

    private RecyclerView recyclerView;
    private DiseaseViewModel diseaseViewModel;
    private ArrayList<DiseaseViewModel> diseaseViewModelArrayList;
    private DiseaseRecyclerViewAdapter recyclerViewAdapter;

    private String [] doc1 = {"Amlodepine"};

    private LinearLayoutManager llm;

    private String last_activity;

    private Class aClass;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);
        diseaseViewModelArrayList = new ArrayList<>();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView);

        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);


        llm.setItemPrefetchEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        try {
            Intent i = Objects.requireNonNull(getActivity()).getIntent();
            last_activity = i.getStringExtra("activity");
            Log.d("Last activity", last_activity+"");

            if (last_activity.contains("Worker")){
                aClass = HomeHealthWorker.class;
            }   else {
                aClass = HomeConstituents.class;
            }
        }catch (Exception e){

        }

        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        diseaseViewModelArrayList.clear();
        firestoreDB.collection("diseases_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                diseaseViewModel = documentSnapshot.toObject(DiseaseViewModel.class);
                                diseaseViewModelArrayList.add(diseaseViewModel);

                                recyclerViewAdapter  = new DiseaseRecyclerViewAdapter(last_activity, Objects.requireNonNull(getActivity()).getApplicationContext(),diseaseViewModelArrayList);
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
}