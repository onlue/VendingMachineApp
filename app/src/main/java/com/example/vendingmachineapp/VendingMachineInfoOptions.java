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

public class VendingMachineInfoOptions extends AppCompatActivity {

    SearchView vending_SV;
    RecyclerView products_RV;
    FloatingActionButton addVengingButton;

    MyDataBaseHelper mydb;
    ArrayList<String> id, nameMachine, productName, capacity;
    customVendingInfoAdapter adapter;

    Button backButton;
    ImageView updateBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_machine_info_options);

        updateBase = findViewById(R.id.updateVendingsInfo);

        updateBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = new ArrayList<>();
                nameMachine = new ArrayList<>();
                productName = new ArrayList<>();
                capacity = new ArrayList<>();

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                long userid = sharedPreferences.getLong("user_id", -1);

                Cursor cursor = mydb.readVendingInfoData(userid);

                    while (cursor.moveToNext()) {
                        id.add(cursor.getString(0));
                        capacity.add(cursor.getString(2));
                        nameMachine.add(cursor.getString(3));
                        productName.add(cursor.getString(1));
                    }
                adapter.filterLists(id,nameMachine,productName,capacity);
            }
        });

        backButton = findViewById(R.id.backMainVendingsInfo);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        vending_SV = findViewById(R.id.vendingsinfo_search);

        vending_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        products_RV = findViewById(R.id.vendingsinfo_view);
        addVengingButton = findViewById(R.id.vendingsinfo_add);

        mydb = new MyDataBaseHelper(VendingMachineInfoOptions.this);
        storeVendingDataInArrays();

        addVengingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendingMachineInfoOptions.this,AddVendingsInfo.class);
                startActivity(intent);
            }
        });
    }

    private void filter(String newText) {
        ArrayList idFilter,nameFilter,locationFilter,capacityFilter;
        idFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        locationFilter = new ArrayList<>();
        capacityFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++){
            if(nameMachine.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT))){
                idFilter.add(id.get(i));
                nameFilter.add(nameMachine.get(i));
                locationFilter.add(productName.get(i));
                capacityFilter.add(capacity.get(i));
            }
        }
        if(idFilter.isEmpty()){
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.filterLists(idFilter,nameFilter,locationFilter,capacityFilter);
        }
    }

    public void storeVendingDataInArrays() {
        id = new ArrayList<>();
        nameMachine = new ArrayList<>();
        productName = new ArrayList<>();
        capacity = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Cursor cursor = mydb.readVendingInfoData(userid);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                capacity.add(cursor.getString(2));
                nameMachine.add(cursor.getString(3));
                productName.add(cursor.getString(1));
            }
        }

        adapter = new customVendingInfoAdapter(VendingMachineInfoOptions.this, VendingMachineInfoOptions.this, id, nameMachine, productName, capacity);
        products_RV.setAdapter(adapter);
        products_RV.setLayoutManager(new LinearLayoutManager(VendingMachineInfoOptions.this));
    }
}