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

public class customSaleAdapter extends RecyclerView.Adapter<customSaleAdapter.MySaleViewHolder> {

    private Context context;
    Activity activity;

    ArrayList product, machine, date, id,amount;

    public customSaleAdapter(ArrayList product, ArrayList machine, ArrayList date, ArrayList id, ArrayList amount, Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        this.product = product;
        this.machine = machine;
        this.date = date;
        this.id = id;
        this.amount = amount;
    }

    public void filterLists(ArrayList product, ArrayList machine, ArrayList date, ArrayList id, ArrayList amount) {
        this.product = product;
        this.machine = machine;
        this.date = date;
        this.id = id;
        this.amount = amount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public customSaleAdapter.MySaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sale_row, parent, false);
        return new customSaleAdapter.MySaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customSaleAdapter.MySaleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.machine_text.setText(machine.get(position).toString());
        holder.product_text.setText(product.get(position).toString());
        holder.id_text.setText(id.get(position).toString());
        holder.date_text.setText(date.get(position).toString());
        holder.amount_text.setText(amount.get(position).toString());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,UpdateSale.class);
                intent.putExtra("id",id.get(position).toString());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MySaleViewHolder extends RecyclerView.ViewHolder {

        TextView machine_text, product_text, id_text, date_text, amount_text;
        LinearLayout layout;


        public MySaleViewHolder(@NonNull View itemView) {
            super(itemView);
            machine_text = itemView.findViewById(R.id.saleMachine_text_name);
            product_text = itemView.findViewById(R.id.saleProduct_text);
            id_text = itemView.findViewById(R.id.sale_id_text);
            date_text = itemView.findViewById(R.id.saleDate_text);
            amount_text = itemView.findViewById(R.id.saleAmount_text);
            layout = itemView.findViewById(R.id.saleMainLayout);
        }
    }
}
