package com.example.vendingmachineapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customServicesAdapter extends RecyclerView.Adapter<customServicesAdapter.MyServicesViewHolder>{
    private Context context;
    Activity activity;

    public ArrayList id, customerid, machine,services;

    public customServicesAdapter(Context context, Activity activity,ArrayList id, ArrayList customerid, ArrayList machine,ArrayList services){
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.customerid = customerid;
        this.machine = machine;
        this.services = services;
    }

    public void filterLists(ArrayList id, ArrayList customerid, ArrayList machine, ArrayList services){
        this.id = id;
        this.customerid = customerid;
        this.machine = machine;
        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.services_row, parent, false);
        return new customServicesAdapter.MyServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServicesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.customer_text.setText(customerid.get(position).toString());
        holder.id_text.setText(id.get(position).toString());
        holder.service_text.setText(services.get(position).toString());
        holder.machine_text.setText(machine.get(position).toString());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,UpdateService.class);
                intent.putExtra("id",id.get(position).toString());
                intent.putExtra("extra_machine_id", machine.get(position).toString());
                intent.putExtra("extra_service_id", services.get(position).toString());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyServicesViewHolder extends RecyclerView.ViewHolder{

        TextView customer_text, id_text, machine_text, service_text;
        LinearLayout layout;

        public MyServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            id_text = itemView.findViewById(R.id.services_id_text);
            customer_text = itemView.findViewById(R.id.services_customer_text);
            machine_text = itemView.findViewById(R.id.servicesmachine_name_text);
            service_text = itemView.findViewById(R.id.services_name_text);
            layout = itemView.findViewById(R.id.ServiceMainLayout);
        }
    }
}
