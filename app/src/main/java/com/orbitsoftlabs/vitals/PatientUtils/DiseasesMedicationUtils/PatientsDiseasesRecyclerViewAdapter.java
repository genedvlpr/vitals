package com.orbitsoftlabs.vitals.PatientUtils.DiseasesMedicationUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.PatientUtils.PatientsViewModel;
import com.orbitsoftlabs.vitals.R;

import java.util.ArrayList;
import java.util.List;

public class PatientsDiseasesRecyclerViewAdapter extends RecyclerView.Adapter<PatientsDiseasesMedicationRecyclerViewHolder> {

    private PatientsViewModel patientsViewModel;
    private ArrayList<String> diseasesArrayList;

    private Context context;

    private String patientFName, patientLName, patientAddress, patientAge, patientContactNo, dateDiagnosed,
            season,monthDiagnosed,patientBirthday,patientBarangay, patientDisease, pID;

    private String lastIntent;

    private List<String> disease_list;

    public PatientsDiseasesRecyclerViewAdapter(Context context, ArrayList<String> diseasesArrayList){
        this.context = context;
        this.diseasesArrayList = diseasesArrayList;
    }

    @NonNull
    @Override
    public PatientsDiseasesMedicationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_patient_diseases, parent, false);
        return new PatientsDiseasesMedicationRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PatientsDiseasesMedicationRecyclerViewHolder holder, int position) {
        if (diseasesArrayList.size() >= 2){
            holder.label.setText(diseasesArrayList.get(position));
        }else{
            holder.label.setText(diseasesArrayList.get(0));
        }

    }



    @Override
    public int getItemCount() {
        return diseasesArrayList.size();
    }
}
