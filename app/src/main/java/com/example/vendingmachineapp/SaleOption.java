package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class SaleOption extends AppCompatActivity {

    SearchView sale_SV;
    RecyclerView sale_RV;
    FloatingActionButton addSale;

    MyDataBaseHelper mydb;
    ArrayList<String> amount, id, machine, product,date;
    customSaleAdapter adapter;

    Button backButton;
    ImageView updateSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_option);

        init();
        mydb = new MyDataBaseHelper(SaleOption.this);
        storeDataInArrays();

        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleOption.this,AddSale.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = new ArrayList<>();
                id = new ArrayList<>();
                machine = new ArrayList<>();
                product = new ArrayList<>();
                date = new ArrayList<>();

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                long userid = sharedPreferences.getLong("user_id", -1);

                Cursor cursor = mydb.readSaleData(userid);

                while (cursor.moveToNext()) {
                    id.add(cursor.getString(0));
                    machine.add(cursor.getString(1));
                    product.add(cursor.getString(2));
                    date.add(cursor.getString(3));
                    amount.add(cursor.getString(4));
                }
                adapter.filterLists(product,machine,date,id,amount);
            }
        });

        sale_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    public void filter(String newText){
        ArrayList<String> amountFilter, idFilter, machineFilter, productFilter, dateFilter;
        amountFilter = new ArrayList<>();
        idFilter = new ArrayList<>();
        machineFilter = new ArrayList<>();
        productFilter = new ArrayList<>();
        dateFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            if (product.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT)) || machine.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT)) ) {
                idFilter.add(id.get(i));
                machineFilter.add(machine.get(i));
                productFilter.add(product.get(i));
                dateFilter.add(date.get(i));
                amountFilter.add(amount.get(i));
            }
        }
        if (idFilter.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterLists(productFilter,machineFilter,dateFilter,idFilter,amountFilter);
        }
    }

    private void storeDataInArrays() {
        amount = new ArrayList<>();
        id = new ArrayList<>();
        machine = new ArrayList<>();
        product = new ArrayList<>();
        date = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = mydb.readSaleData(userid);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                machine.add(cursor.getString(1));
                product.add(cursor.getString(2));
                date.add(cursor.getString(3));
                amount.add(cursor.getString(4));
            }
        }

        adapter = new customSaleAdapter(product,machine,date,id,amount,SaleOption.this, SaleOption.this);
        sale_RV.setAdapter(adapter);
        sale_RV.setLayoutManager(new LinearLayoutManager(SaleOption.this));
    }

    public void init(){
        sale_RV = findViewById(R.id.sale_view);
        sale_SV = findViewById(R.id.sale_search);
        addSale = findViewById(R.id.sale_add);
        backButton = findViewById(R.id.backMainSale);
        updateSale = findViewById(R.id.updateSale);
    }
}