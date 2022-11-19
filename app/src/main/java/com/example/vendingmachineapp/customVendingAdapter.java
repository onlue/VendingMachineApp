package com.example.vendingmachineapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customVendingAdapter extends RecyclerView.Adapter<customVendingAdapter.MyVendigsViewHolder> {
    private Context context;
    Activity activity;
    public ArrayList id, name, capacity, location;

    public customVendingAdapter(Context context,Activity activity, ArrayList id, ArrayList name, ArrayList capacity, ArrayList location){
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    public void filterLists(ArrayList id, ArrayList name, ArrayList capacity, ArrayList location){
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public customVendingAdapter.MyVendigsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vendings_row, parent, false);
        return new customVendingAdapter.MyVendigsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVendigsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.vendings_id_text.setText(String.valueOf(id.get(position)));
        holder.vendings_location_text.setText(String.valueOf(location.get(position)));
        holder.vendings_capacity_text.setText(String.valueOf(capacity.get(position)));
        holder.vendings_name_text.setText(String.valueOf(name.get(position)));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdateVendings.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyVendigsViewHolder extends RecyclerView.ViewHolder{

        TextView vendings_id_text,vendings_capacity_text,vendings_name_text,vendings_location_text;
        LinearLayout layout;

        public MyVendigsViewHolder(@NonNull View itemView) {
            super(itemView);
            vendings_id_text = itemView.findViewById(R.id.vendings_id_text);
            vendings_capacity_text = itemView.findViewById(R.id.vendings_capacity_text);
            vendings_name_text = itemView.findViewById(R.id.vendings_name_text);
            vendings_location_text = itemView.findViewById(R.id.vendings_location_text);
            layout = itemView.findViewById(R.id.VendingsMainLayout);
        }
    }
}
