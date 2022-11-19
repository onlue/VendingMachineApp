package com.example.vendingmachineapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customVendingInfoAdapter extends RecyclerView.Adapter<customVendingInfoAdapter.MyVendingInfoViewHolder>{
    private Context context;
    Activity activity;
    public ArrayList id, vendingMachineId, productId, amountAvailable;

    public customVendingInfoAdapter(Context context,Activity activity, ArrayList id, ArrayList vendingMachineId, ArrayList productId, ArrayList amountAvailable){
        this.context = context;
        this.activity = activity;
        this.id = id;
        this.vendingMachineId = vendingMachineId;
        this.productId = productId;
        this.amountAvailable = amountAvailable;
    }

    public void filterLists(ArrayList id, ArrayList vendingMachineId, ArrayList productId, ArrayList amountAvailable){
        this.id = id;
        this.vendingMachineId = vendingMachineId;
        this.productId = productId;
        this.amountAvailable = amountAvailable;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public customVendingInfoAdapter.MyVendingInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_vendinginfo_row, parent, false);
        return new customVendingInfoAdapter.MyVendingInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customVendingInfoAdapter.MyVendingInfoViewHolder holder, int position) {
        holder.product_text.setText(String.valueOf(productId.get(position)));
        holder.id_text.setText(String.valueOf(id.get(position)));
        holder.amoutnAvailable_text.setText(String.valueOf(amountAvailable.get(position)));
        holder.machine_text.setText(String.valueOf(vendingMachineId.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyVendingInfoViewHolder extends RecyclerView.ViewHolder{

        EditText product_text,machine_text,id_text,amoutnAvailable_text;
        LinearLayout layout;

        public MyVendingInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            product_text = itemView.findViewById(R.id.productvendinginfo_id_text_name_text);
            machine_text = itemView.findViewById(R.id.vendingmachine_text);
            id_text = itemView.findViewById(R.id.vendinginfo_id_text);
            amoutnAvailable_text = itemView.findViewById(R.id.vendinginfo_amount_text);
            layout = itemView.findViewById(R.id.VendingInfoMainLayout);
        }
    }
}
