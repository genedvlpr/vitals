package com.orbitsoftlabs.vitals.HealthWorkersUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

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
import static com.orbitsoftlabs.vitals.HelperUtil.mAuth;

public class HealthWorkers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HealthWorkersViewModel healthWorkersViewModel;
    private HealthWorkersRecyclerViewAdapter healthWorkersRecyclerViewAdapter;
    private ArrayList<HealthWorkersViewModel> healthWorkersViewModelArrayList;

    private LinearLayoutManager llm;

    private Class aClass;
    private String last_activity;

    private Toolbar toolbar;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_workers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        recyclerView = findViewById(R.id.recyclerView);

        healthWorkersViewModelArrayList = new ArrayList<>();
        healthWorkersViewModelArrayList.clear();

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setItemPrefetchEnabled(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

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

        healthWorkersViewModelArrayList.clear();

        firestoreDB.collection("users_lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                String all_user_id = (String) documentSnapshot.get("user_id");
                                String other_acc;
                                try {
                                    other_acc = all_user_id.replace(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()," ");
                                } catch (Exception e){
                                    Log.d("Account", "Constituent");
                                    other_acc = (String) documentSnapshot.get("user_id");
                                }
                                firestoreDB.collection("users")
                                        .document(Objects.requireNonNull(other_acc))
                                        .collection("account_details")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                                        healthWorkersViewModel = documentSnapshot.toObject(HealthWorkersViewModel.class);
                                                        healthWorkersViewModelArrayList.add(healthWorkersViewModel);

                                                        healthWorkersRecyclerViewAdapter  = new HealthWorkersRecyclerViewAdapter(last_activity,getApplicationContext(),healthWorkersViewModelArrayList);
                                                        healthWorkersRecyclerViewAdapter.setHasStableIds(true);
                                                        recyclerView.setAdapter(healthWorkersRecyclerViewAdapter);
                                                    }

                                                }
                                            }
                                        });
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
        HelperUtil.backNavigationFinish(HealthWorkers.this,aClass);
    }
}
