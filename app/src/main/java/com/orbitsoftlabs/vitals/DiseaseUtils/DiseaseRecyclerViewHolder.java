package com.orbitsoftlabs.vitals.DiseaseUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class DiseaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView diseaseName, diseaseMedications;
    public MaterialCardView item_card;

    public DiseaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        diseaseName = itemView.findViewById(R.id.diseaseName);
        //diseaseMedications = itemView.findViewById(R.id.diseaseMedications);


    }
}
