package com.orbitsoftlabs.vitals.MedSolutionUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;

import java.util.ArrayList;

public class MedSolutionRecyclerViewAdapter extends RecyclerView.Adapter<MedSolutionRecyclerViewHolder> {

    private MedViewModel medViewModel;
    private ArrayList<MedViewModel> medViewModelArrayList;

    private Context context;

    private String mName, mDescription, mForIllness;

    private String lastIntent;

    public MedSolutionRecyclerViewAdapter(String lastIntent, Context context, ArrayList<MedViewModel> medViewModels){
        this.context = context;
        this.medViewModelArrayList = medViewModels;
        this.lastIntent = lastIntent;
    }

    @NonNull
    @Override
    public MedSolutionRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_med_solutions, parent, false);
        return new MedSolutionRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedSolutionRecyclerViewHolder holder, int position) {

        if (medViewModelArrayList.size() > 1){
            holder.medname.setText(medViewModelArrayList.get(position).medName);
            holder.medForIllness.setText(medViewModelArrayList.get(position).medForIllness);
        }else if(medViewModelArrayList.size() == 1){
            holder.medname.setText(medViewModelArrayList.get(0).medName);
            holder.medForIllness.setText(medViewModelArrayList.get(0).medForIllness);
        }


        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MedSolutionsDetailedView.class);
                mName = medViewModelArrayList.get(holder.getAdapterPosition()).medName;
                mDescription = medViewModelArrayList.get(holder.getAdapterPosition()).medDescription;
                mForIllness = medViewModelArrayList.get(holder.getAdapterPosition()).medForIllness;
                i.putExtra("medName", mName);
                i.putExtra("medDescription", mDescription);
                i.putExtra("medForIllness", mForIllness);
                i.putExtra("activity", lastIntent);
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return medViewModelArrayList.size();
    }
}
