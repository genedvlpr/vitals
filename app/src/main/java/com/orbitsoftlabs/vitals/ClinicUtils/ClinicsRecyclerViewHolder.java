package com.orbitsoftlabs.vitals.ClinicUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class ClinicsRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView clinicName, clinicAddress;
    public MaterialCardView item_card;

    public ClinicsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        clinicName = itemView.findViewById(R.id.clinicName);
        clinicAddress = itemView.findViewById(R.id.clinicAddress);

    }
}
