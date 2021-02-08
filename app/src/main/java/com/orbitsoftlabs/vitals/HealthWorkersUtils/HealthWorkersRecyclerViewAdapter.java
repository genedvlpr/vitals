package com.orbitsoftlabs.vitals.HealthWorkersUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orbitsoftlabs.vitals.R;

import java.util.ArrayList;

public class HealthWorkersRecyclerViewAdapter extends RecyclerView.Adapter<HealthWorkersRecyclerViewHolder> {

    private HealthWorkersViewModel healthWorkersViewModel;
    private ArrayList<HealthWorkersViewModel> healthWorkersViewModelArrayList;

    private Context context;

    public String address, barangay, email, first_name, last_name , middle_name, messenger_username, mobile_no, user_id;



    private String lastIntent;

    public HealthWorkersRecyclerViewAdapter(String lastIntent, Context context, ArrayList<HealthWorkersViewModel> healthWorkersViewModelArrayList){
        this.context = context;
        this.healthWorkersViewModelArrayList = healthWorkersViewModelArrayList;
        this.lastIntent = lastIntent;
    }

    @NonNull
    @Override
    public HealthWorkersRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_view_health_workers, parent, false);
        return new HealthWorkersRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HealthWorkersRecyclerViewHolder holder, int position) {

        if (healthWorkersViewModelArrayList.size() > 1){
            holder.workerName.setText(healthWorkersViewModelArrayList.get(position).first_name + " " + healthWorkersViewModelArrayList.get(position).last_name);
            holder.workerContactNo.setText(healthWorkersViewModelArrayList.get(position).mobile_no);
        }else if(healthWorkersViewModelArrayList.size() == 1){
            holder.workerName.setText(healthWorkersViewModelArrayList.get(0).first_name + " " + healthWorkersViewModelArrayList.get(0).last_name);
            holder.workerContactNo.setText(healthWorkersViewModelArrayList.get(0).mobile_no);
        }


        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,HealthWorkersDetailedView.class);
                address = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).address;
                barangay = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).barangay;
                email = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).email;
                first_name = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).first_name;
                last_name = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).last_name;
                messenger_username = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).messenger_username;
                middle_name = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).middle_name;
                mobile_no = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).mobile_no;
                user_id = healthWorkersViewModelArrayList.get(holder.getAdapterPosition()).user_id;

                i.putExtra("address", address);
                i.putExtra("barangay", barangay);
                i.putExtra("email", email);
                i.putExtra("first_name", first_name);
                i.putExtra("last_name", last_name);
                i.putExtra("messenger_username", messenger_username);
                i.putExtra("middle_name", middle_name);
                i.putExtra("mobile_no", mobile_no);
                i.putExtra("user_id", user_id);

                i.putExtra("activity", lastIntent);
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return healthWorkersViewModelArrayList.size();
    }
}
