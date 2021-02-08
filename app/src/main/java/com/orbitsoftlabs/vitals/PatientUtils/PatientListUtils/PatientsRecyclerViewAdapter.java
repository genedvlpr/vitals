package com.orbitsoftlabs.vitals.PatientUtils.PatientListUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.PatientUtils.PatientDetailedView;
import com.orbitsoftlabs.vitals.PatientUtils.PatientsViewModel;
import com.orbitsoftlabs.vitals.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.orbitsoftlabs.vitals.HelperUtil.firestoreDB;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewHolder> {

    private PatientsViewModel patientsViewModel;
    private ArrayList<PatientsViewModel> patientsViewModelArrayList;

    private Context context;

    private String patientFName, patientLName, patientAddress, patientAge, patientContactNo, dateDiagnosed,
            season,monthDiagnosed,patientBirthday,patientBarangay, patientDisease, pID;

    private String lastIntent;

    private List<String> disease_list;

    public PatientsRecyclerViewAdapter(Context context, ArrayList<PatientsViewModel> patientsViewModel){
        this.context = context;
        this.patientsViewModelArrayList = patientsViewModel;
    }

    @NonNull
    @Override
    public PatientsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_patients, parent, false);
        return new PatientsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PatientsRecyclerViewHolder holder, int position) {

        if (patientsViewModelArrayList.size() > 1){
            holder.patientName.setText(patientsViewModelArrayList.get(position).patientFName+" "+patientsViewModelArrayList.get(position).patientLName);
            //holder.patientDisease.setText(patientsViewModelArrayList.get(position).patientDisease);

            firestoreDB.collection("patient_lists")
                    .document(patientsViewModelArrayList.get(position).pID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                List<String> disease_list = (List<String>) document.get("patientDisease");
                                holder.patientDisease.setText(String.valueOf(disease_list));
                                Log.d("Patient", String.valueOf(disease_list));
                            }
                        }
                    });
        }else if(patientsViewModelArrayList.size() == 1){
            holder.patientName.setText(patientsViewModelArrayList.get(0).patientFName+" "+patientsViewModelArrayList.get(0).patientLName);


            firestoreDB.collection("patient_lists")
                    .document(patientsViewModelArrayList.get(0).pID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                List<String> disease_list = (List<String>) document.get("patientDisease");
                                holder.patientDisease.setText(String.valueOf(disease_list));
                                Log.d("Patient", String.valueOf(disease_list));
                            }
                        }
                    });
        }


        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PatientDetailedView.class);
                patientAddress = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientAddress;
                patientAge = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientAge;
                patientBarangay = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientBarangay;
                patientBirthday = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientBirthday;
                patientContactNo = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientContactNo;
                //patientDisease = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientDisease;
                patientFName = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientFName;
                patientLName = patientsViewModelArrayList.get(holder.getAdapterPosition()).patientLName;
                season = patientsViewModelArrayList.get(holder.getAdapterPosition()).seasonDiagnosed;
                monthDiagnosed = patientsViewModelArrayList.get(holder.getAdapterPosition()).monthDiagnosed;
                dateDiagnosed = patientsViewModelArrayList.get(holder.getAdapterPosition()).dateDiagnosed;
                pID = patientsViewModelArrayList.get(holder.getAdapterPosition()).pID;

                i.putExtra("patientAddress", patientAddress);
                i.putExtra("patientAge", patientAge);
                i.putExtra("patientBarangay", patientBarangay);
                i.putExtra("patientBirthday", patientBirthday);
                i.putExtra("patientContactNo", patientContactNo);
                //i.putExtra("patientDisease", patientDisease);
                i.putExtra("patientFName", patientFName);
                i.putExtra("patientLName", patientLName);
                i.putExtra("season", season);
                i.putExtra("monthDiagnosed", monthDiagnosed);

                i.putExtra("dateDiagnosed", dateDiagnosed);
                i.putExtra("pID", pID);
                i.putExtra("activity", lastIntent);
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return patientsViewModelArrayList.size();
    }
}
