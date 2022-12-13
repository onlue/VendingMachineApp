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

public class ServiceOption extends AppCompatActivity {

    SearchView service_SV;
    RecyclerView service_RV;
    FloatingActionButton addService;

    MyDataBaseHelper mydb;
    ArrayList<String> name, id, machine, customer;
    customServicesAdapter adapter;

    Button backButton;
    ImageView updateServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_option);

        init();
        mydb = new MyDataBaseHelper(ServiceOption.this);
        storeVendingDataInArrays();

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceOption.this, AddService.class);
                startActivity(intent);
            }
        });

        service_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        updateServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = new ArrayList<>();
                name = new ArrayList<>();
                machine = new ArrayList<>();
                customer = new ArrayList<>();

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                long userid = sharedPreferences.getLong("user_id", -1);

                Cursor cursor = mydb.readServices(userid);

                while (cursor.moveToNext()) {
                    id.add(cursor.getString(0));
                    name.add(cursor.getString(1));
                    machine.add(cursor.getString(2));
                    customer.add(cursor.getString(3));
                }
                adapter.filterLists(id,customer,machine,name);
            }
        });

    }

    public void filter(String newText) {
        ArrayList idFilter, nameFilter, machineFilter, customerFilter;
        idFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        machineFilter = new ArrayList<>();
        customerFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            if (name.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT))) {
                idFilter.add(id.get(i));
                nameFilter.add(name.get(i));
                machineFilter.add(machine.get(i));
                customerFilter.add(customer.get(i));
            }
        }
        if (idFilter.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        }
            adapter.filterLists(idFilter, customerFilter, machineFilter, nameFilter);

    }

    private void storeVendingDataInArrays() {
        id = new ArrayList<>();
        name = new ArrayList<>();
        machine = new ArrayList<>();
        customer = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = mydb.readServices(userid);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                machine.add(cursor.getString(2));
                customer.add(cursor.getString(3));
            }
        }

        adapter = new customServicesAdapter(ServiceOption.this, ServiceOption.this, id, customer, machine, name);
        service_RV.setAdapter(adapter);
        service_RV.setLayoutManager(new LinearLayoutManager(ServiceOption.this));
    }


    public void init() {
        service_RV = findViewById(R.id.service_view);
        service_SV = findViewById(R.id.service_search);
        addService = findViewById(R.id.service_add);
        backButton = findViewById(R.id.backMainService);
        updateServices = findViewById(R.id.update_service);
    }
}