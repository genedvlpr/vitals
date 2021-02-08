package com.orbitsoftlabs.vitals.HealthWorkersUtils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.card.MaterialCardView;

public class HealthWorkersRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView workerName, workerContactNo;
    public MaterialCardView item_card;

    public HealthWorkersRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        item_card = itemView.findViewById(R.id.item_card);

        workerName = itemView.findViewById(R.id.workerName);
        workerContactNo = itemView.findViewById(R.id.workerContactNo);


    }
}
