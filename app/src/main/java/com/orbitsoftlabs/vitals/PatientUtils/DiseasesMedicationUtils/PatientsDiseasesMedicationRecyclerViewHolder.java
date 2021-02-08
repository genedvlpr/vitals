package com.orbitsoftlabs.vitals.PatientUtils.DiseasesMedicationUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class PatientsDiseasesMedicationRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView patientName, label;
    public MaterialCardView item_card;

    public PatientsDiseasesMedicationRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        label = itemView.findViewById(R.id.label);


    }
}
