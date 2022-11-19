package com.example.vendingmachineapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class customProductAdapter extends RecyclerView.Adapter<customProductAdapter.MyProductViewHolder> {
    private Context contex;
    Activity activity;
    private ArrayList name, desc, price, id;
    ArrayList<byte[]> images;


    public customProductAdapter(Activity activity,
                                Context contex,
                                ArrayList name,
                                ArrayList price,
                                ArrayList id,
                                ArrayList desc,
                                ArrayList<byte[]> images) {
        this.activity = activity;
        this.contex = contex;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.id = id;
        this.images = images;
    }

    public void filterLists(ArrayList id, ArrayList name, ArrayList price, ArrayList desc,ArrayList<byte[]> images){
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.images = images;
        notifyDataSetChanged();
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

        try {
            holder.product_id_text.setText(String.valueOf(id.get(position)));
            holder.product_name_text.setText(String.valueOf(name.get(position)));
            holder.product_description_text.setText(String.valueOf(desc.get(position)));
            holder.product_price_text.setText(String.valueOf(price.get(position)));
            Bitmap bmp = BitmapFactory.decodeByteArray(images.get(position), 0, images.get(position).length);
            holder.image_layout.setImageBitmap(Bitmap.createScaledBitmap(bmp,80, 80, false));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contex, UpdateProducts.class);
                    intent.putExtra("id", String.valueOf(id.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
            });
        }
        catch (Exception e){
            Toast.makeText(contex, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder {

        TextView product_id_text, product_name_text, product_description_text, product_price_text;
        ImageView image_layout;
        LinearLayout layout;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image_layout = itemView.findViewById(R.id.product_row_image);
            product_id_text = itemView.findViewById(R.id.product_id_text);
            product_name_text = itemView.findViewById(R.id.product_name_text);
            product_description_text = itemView.findViewById(R.id.product_description_text);
            product_price_text = itemView.findViewById(R.id.product_price_text);
            layout = itemView.findViewById(R.id.ProductMainLayout);
        }
    }
}
