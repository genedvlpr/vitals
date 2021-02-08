package com.orbitsoftlabs.vitals.DiseaseUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;

import java.util.ArrayList;

public class DiseaseRecyclerViewAdapter extends RecyclerView.Adapter<DiseaseRecyclerViewHolder> {

    private DiseaseViewModel diseaseViewModel;
    private ArrayList<DiseaseViewModel> diseaseViewModelArrayList;

    private Context context;

    private String mName, mDescription, mForIllness;

    private String lastIntent;

    public DiseaseRecyclerViewAdapter(String lastIntent, Context context, ArrayList<DiseaseViewModel> diseaseViewModels){
        this.context = context;
        this.diseaseViewModelArrayList = diseaseViewModels;
        this.lastIntent = lastIntent;
    }

    @NonNull
    @Override
    public DiseaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_diseases, parent, false);
        return new DiseaseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiseaseRecyclerViewHolder holder, int position) {

        if (diseaseViewModelArrayList.size() > 1){
            holder.diseaseName.setText(diseaseViewModelArrayList.get(position).diseaseName);
            //holder.diseaseMedications.setText(diseaseViewModelArrayList.get(position).medForIllness);
        }else if(diseaseViewModelArrayList.size() == 1){
            holder.diseaseName.setText(diseaseViewModelArrayList.get(0).diseaseName);
            //holder.diseaseMedications.setText(diseaseViewModelArrayList.get(0).d);
        }


        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DiseaseDetailedView.class);
                mName = diseaseViewModelArrayList.get(holder.getAdapterPosition()).diseaseName;
                mDescription = diseaseViewModelArrayList.get(holder.getAdapterPosition()).diseaseDesc;
                //mForIllness = diseaseViewModelArrayList.get(holder.getAdapterPosition()).medForIllness;
                i.putExtra("diseaseName", mName);
                i.putExtra("diseaseDescription", mDescription);
                //i.putExtra("medForIllness", mForIllness);
                i.putExtra("activity", lastIntent);
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return diseaseViewModelArrayList.size();
    }
}
