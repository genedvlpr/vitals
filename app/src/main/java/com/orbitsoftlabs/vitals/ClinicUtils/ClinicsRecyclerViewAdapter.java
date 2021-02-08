package com.orbitsoftlabs.vitals.ClinicUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;

import java.util.ArrayList;

public class ClinicsRecyclerViewAdapter extends RecyclerView.Adapter<ClinicsRecyclerViewHolder> {

    private ClinicViewModel clinicViewModel;
    private ArrayList<ClinicViewModel> clinicViewModelArrayList;

    private Context context;

    private String mName, mDescription, mBarangay, mPhoneNo;

    private String lastIntent;

    public ClinicsRecyclerViewAdapter(String lastIntent, Context context, ArrayList<ClinicViewModel> clinicViewModelArrayList){
        this.context = context;
        this.clinicViewModelArrayList = clinicViewModelArrayList;
        this.lastIntent = lastIntent;
    }

    @NonNull
    @Override
    public ClinicsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_clinics, parent, false);
        return new ClinicsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClinicsRecyclerViewHolder holder, int position) {

        if (clinicViewModelArrayList.size() > 1){
            holder.clinicName.setText(clinicViewModelArrayList.get(position).clinicName);
            holder.clinicAddress.setText(clinicViewModelArrayList.get(position).clinicAddress);
        }else if(clinicViewModelArrayList.size() == 1){
            holder.clinicName.setText(clinicViewModelArrayList.get(0).clinicName);
            holder.clinicAddress.setText(clinicViewModelArrayList.get(0).clinicAddress);
        }


        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ClinicsDetailedView.class);
                mName = clinicViewModelArrayList.get(holder.getAdapterPosition()).clinicName;
                mDescription = clinicViewModelArrayList.get(holder.getAdapterPosition()).clinicAddress;
                mBarangay = clinicViewModelArrayList.get(holder.getAdapterPosition()).clinicBarangay;
                mPhoneNo = clinicViewModelArrayList.get(holder.getAdapterPosition()).clinicPhoneNo;

                i.putExtra("clinicName", mName);
                i.putExtra("clinicAddress", mDescription);
                i.putExtra("clinicBarangay", mBarangay);
                i.putExtra("clinicPhoneNo", mPhoneNo);
                //i.putExtra("medForIllness", mForIllness);
                i.putExtra("activity", lastIntent);
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return clinicViewModelArrayList.size();
    }
}
