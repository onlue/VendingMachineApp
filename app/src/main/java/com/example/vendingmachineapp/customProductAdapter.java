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

public class customProductAdapter extends RecyclerView.Adapter<customProductAdapter.MyProductViewHolder> {
    private Context contex;
    Activity activity;
    private ArrayList name, desc, price, id;


    public customProductAdapter(Activity activity,
                                Context contex,
                                ArrayList name,
                                ArrayList price,
                                ArrayList id,
                                ArrayList desc) {
        this.activity = activity;
        this.contex = contex;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.id = id;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contex);
        View view = inflater.inflate(R.layout.my_product_row, parent, false);
        return new MyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.product_id_text.setText(String.valueOf(id.get(position)));
        holder.product_name_text.setText(String.valueOf(name.get(position)));
        holder.product_description_text.setText(String.valueOf(desc.get(position)));
        holder.product_price_text.setText(String.valueOf(price.get(position)));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contex, UpdateProducts.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder {

        TextView product_id_text, product_name_text, product_description_text, product_price_text;
        LinearLayout layout;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_id_text = itemView.findViewById(R.id.product_id_text);
            product_name_text = itemView.findViewById(R.id.product_name_text);
            product_description_text = itemView.findViewById(R.id.product_description_text);
            product_price_text = itemView.findViewById(R.id.product_price_text);
            layout = itemView.findViewById(R.id.ProductMainLayout);
        }
    }
}
