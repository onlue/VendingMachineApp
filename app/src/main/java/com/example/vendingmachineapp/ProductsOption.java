package com.example.vendingmachineapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductsOption extends AppCompatActivity {

    RecyclerView products_RV;
    FloatingActionButton addProductsButton;

    MyDataBaseHelper mydb;
    ArrayList<String> id, name, desc, price;
    customProductAdapter adapter;

    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_layout);

        btn = findViewById(R.id.backMainProduct);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        products_RV = findViewById(R.id.product_view);
        addProductsButton = findViewById(R.id.products_add);

        addProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsOption.this, AddProductsActivity.class);
                startActivity(intent);
            }
        });

        mydb = new MyDataBaseHelper(ProductsOption.this);
        storeProductsDataInArrays();
    }

    public void storeProductsDataInArrays() {
        id = new ArrayList<>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        price = new ArrayList<>();

        Cursor cursor = mydb.readProductsData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                desc.add(cursor.getString(2));
                price.add(cursor.getString(3));
            }
        }

        adapter = new customProductAdapter(ProductsOption.this, ProductsOption.this, name, price, id, desc);
        products_RV.setAdapter(adapter);
        products_RV.setLayoutManager(new LinearLayoutManager(ProductsOption.this));

        cursor.close();
    }

}


