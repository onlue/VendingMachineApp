package com.example.vendingmachineapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class ProductsOption extends AppCompatActivity {

    SearchView product_SV;
    RecyclerView products_RV;
    FloatingActionButton addProductsButton;


    MyDataBaseHelper mydb;
    ArrayList<String> id, name, desc, price;
    ArrayList<byte[]> images;
    customProductAdapter adapter;

    ImageView updateProducts;
    Button btn;

    EditText borderOne, borderTwo;
    Button sortPriceASC,sortPriceDESC,sortWeightASC,sortWeightDESC, sortBetween;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_layout);

        DrawerLayout drawerLayout = findViewById(R.id.ProductDrawer);

        sortPriceASC = findViewById(R.id.buttonSort1);
        sortPriceASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortFunction("SELECT * FROM products ORDER BY price");
            }
        });

        sortPriceDESC = findViewById(R.id.buttonSort2);
        sortPriceDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortFunction("SELECT * FROM products ORDER BY price DESC");
            }
        });

        sortWeightASC = findViewById(R.id.buttonSort3);
        sortWeightASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortFunction("SELECT * FROM products ORDER BY weight");
            }
        });

        sortWeightDESC = findViewById(R.id.buttonSort4);
        sortWeightDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortFunction("SELECT * FROM products ORDER BY weight DESC");
            }
        });

        btn = findViewById(R.id.backMainProduct);
        updateProducts = findViewById(R.id.updateProduct);

        updateProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = new ArrayList<>();
                name = new ArrayList<>();
                desc = new ArrayList<>();
                price = new ArrayList<>();
                images = new ArrayList<byte[]>();

                Cursor cursor = mydb.readProductsData();
                while (cursor.moveToNext()) {
                    id.add(cursor.getString(0));
                    name.add(cursor.getString(1));
                    desc.add(cursor.getString(2));
                    price.add(cursor.getString(3));
                    images.add(cursor.getBlob(7));
                }
                cursor.close();
                adapter.filterLists(id, name, price, desc, images);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        product_SV = findViewById(R.id.product_search);
        product_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
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
        adapter = new customProductAdapter(ProductsOption.this, ProductsOption.this, name, price, id, desc, images);
        products_RV.setAdapter(adapter);
        products_RV.setLayoutManager(new LinearLayoutManager(ProductsOption.this));

        findViewById(R.id.sortBetween).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borderOne = findViewById(R.id.fisrtBorder);
                borderTwo = findViewById(R.id.secondBorder);
                SortFunction("SELECT * FROM products WHERE price BETWEEN " + borderOne.getText().toString() + " AND " + borderTwo.getText().toString());
            }
        });
    }

    private void filter(String newText) {
        ArrayList<String> idFilter, nameFilter, descFilter, priceFilter;
        ArrayList<byte[]> imagesFilter;
        idFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        descFilter = new ArrayList<>();
        priceFilter = new ArrayList<>();
        imagesFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            if (name.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT))) {
                idFilter.add(id.get(i));
                nameFilter.add(name.get(i));
                descFilter.add(desc.get(i));
                priceFilter.add(price.get(i));
                imagesFilter.add(images.get(i));
            }
        }
        if (idFilter.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        }
            adapter.filterLists(idFilter, nameFilter, priceFilter, descFilter, images);

    }


    public void storeProductsDataInArrays() {
        id = new ArrayList<>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        price = new ArrayList<>();
        images = new ArrayList<byte[]>();

        Cursor cursor = mydb.readProductsData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                desc.add(cursor.getString(2));
                price.add(cursor.getString(3));
                images.add(cursor.getBlob(7));
            }
        }
        cursor.close();
    }

    public void SortFunction(String query) {
        SQLiteDatabase db = mydb.getReadableDatabase();
        id = new ArrayList<>();
        name = new ArrayList<>();
        desc = new ArrayList<>();
        price = new ArrayList<>();
        images = new ArrayList<byte[]>();

        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            id.add(cursor.getString(0));
            name.add(cursor.getString(1));
            desc.add(cursor.getString(2));
            price.add(cursor.getString(3));
            images.add(cursor.getBlob(7));
        }
        cursor.close();
        adapter.filterLists(id,name,price,desc,images);
    }

}


