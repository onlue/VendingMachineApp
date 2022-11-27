package com.example.vendingmachineapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class ServicesDescOption extends AppCompatActivity {

    SearchView services_SV;
    RecyclerView services_RV;
    FloatingActionButton addServices;

    MyDataBaseHelper mydb;
    ArrayList<String> name, id, desc,price;
    customSevicesDescAdapter adapter;

    Button backButton;
    ImageView updateSevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_desc_option);

        init();

        mydb = new MyDataBaseHelper(ServicesDescOption.this);
        storeVendingDataInArrays();

        services_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateSevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = new ArrayList<>();
                desc = new ArrayList<>();
                id = new ArrayList<>();
                price = new ArrayList<>();

                Cursor cursor = mydb.readServicesData();
                    while (cursor.moveToNext()) {
                        id.add(cursor.getString(0));
                        desc.add(cursor.getString(1));
                        name.add(cursor.getString(2));
                        price.add(cursor.getString(3));
                }
                adapter.filterLists(id,name,desc,price);
            }
        });

        addServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServicesDescOption.this, AddServicesDesc.class);
                startActivity(intent);
            }
        });
    }

    private void filter(String newText) {
        ArrayList idFilter,nameFilter,descFilter,priceFilter;
        idFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        descFilter = new ArrayList<>();
        priceFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++){
            if(name.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT))){
                idFilter.add(id.get(i));
                nameFilter.add(name.get(i));
                descFilter.add(desc.get(i));
                priceFilter.add(price.get(i));
            }
        }
        if(idFilter.isEmpty()){
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.filterLists(idFilter,nameFilter,descFilter,priceFilter);
        }
    }

    private void storeVendingDataInArrays() {
        name = new ArrayList<>();
        desc = new ArrayList<>();
        id = new ArrayList<>();
        price = new ArrayList<>();

        Cursor cursor = mydb.readServicesData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(2));
                desc.add(cursor.getString(1));
                price.add(cursor.getString(3));
            }
        }

        adapter = new customSevicesDescAdapter(ServicesDescOption.this, ServicesDescOption.this, id, name, desc,price);
        services_RV.setAdapter(adapter);
        services_RV.setLayoutManager(new LinearLayoutManager(ServicesDescOption.this));
    }

    public void init() {
        services_SV = findViewById(R.id.servicesdesc_search);
        services_RV = findViewById(R.id.ServicesDesc_rv);
        backButton = findViewById(R.id.backMainServicesDesc);
        updateSevices = findViewById(R.id.updateServicesDesc);
        addServices = findViewById(R.id.ServicesDesc_add);
    }
}