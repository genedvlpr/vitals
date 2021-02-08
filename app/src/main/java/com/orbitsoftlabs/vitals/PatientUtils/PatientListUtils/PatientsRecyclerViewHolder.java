package com.orbitsoftlabs.vitals.PatientUtils.PatientListUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class PatientsRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView patientName, patientDisease;
    public MaterialCardView item_card;

    public PatientsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        patientName = itemView.findViewById(R.id.patientName);
        patientDisease = itemView.findViewById(R.id.patientDisease);


    }
}
