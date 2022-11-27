package com.example.vendingmachineapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customSevicesDescAdapter extends RecyclerView.Adapter<customSevicesDescAdapter.MyServicesDescViewHolder> {

    private Context context;
    Activity activity;
    public ArrayList id, name, desc,price;

    public customSevicesDescAdapter(Context context,Activity activity, ArrayList id, ArrayList name, ArrayList desc,ArrayList price){
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public void filterLists(ArrayList id, ArrayList name, ArrayList desc,ArrayList price){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public customSevicesDescAdapter.MyServicesDescViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.services_desc_row, parent, false);
        return new MyServicesDescViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customSevicesDescAdapter.MyServicesDescViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name_text.setText(name.get(position).toString());
        holder.desc_text.setText(desc.get(position).toString());
        holder.id_text.setText(id.get(position).toString());
        holder.price_text.setText(price.get(position).toString());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ServiceDescUpdate.class);
                intent.putExtra("id",id.get(position).toString());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyServicesDescViewHolder extends RecyclerView.ViewHolder {
        TextView name_text, desc_text, id_text,price_text;
        LinearLayout layout;

        public MyServicesDescViewHolder(@NonNull View itemView) {
            super(itemView);
            name_text = itemView.findViewById(R.id.sevicesdesc_text_name);
            desc_text = itemView.findViewById(R.id.servicesdesc_desc);
            id_text = itemView.findViewById(R.id.servicesdesc_id_text);
            price_text = itemView.findViewById(R.id.servicesdescprice_price_text);
            layout = itemView.findViewById(R.id.ServicesDescMainLayout);
        }
    }
}
