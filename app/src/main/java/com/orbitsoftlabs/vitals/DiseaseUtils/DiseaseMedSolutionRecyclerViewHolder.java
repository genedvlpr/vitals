package com.orbitsoftlabs.vitals.DiseaseUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class DiseaseMedSolutionRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView medname, medForIllness;
    public MaterialCardView item_card;

    public DiseaseMedSolutionRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        medname = itemView.findViewById(R.id.medName);


    }
}
