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

public class VendingsOption extends AppCompatActivity {

    SearchView vending_SV;
    RecyclerView products_RV;
    FloatingActionButton addVengingButton;

    MyDataBaseHelper mydb;
    ArrayList<String> location, name, id, capacity;
    customVendingAdapter adapter;

    Button backButton;
    ImageView updateMachines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendings_option);

        vending_SV = findViewById(R.id.vendings_search);

        backButton = findViewById(R.id.backMainVendings);
        updateMachines = findViewById(R.id.updatevendingmachines);

        updateMachines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = new ArrayList<>();
                name = new ArrayList<>();
                location = new ArrayList<>();
                capacity = new ArrayList<>();

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                long userid = sharedPreferences.getLong("user_id", -1);

                Cursor cursor = mydb.readVendingsData(userid);
                while (cursor.moveToNext()) {
                    id.add(cursor.getString(0));
                    capacity.add(cursor.getString(2));
                    name.add(cursor.getString(3));
                    location.add(cursor.getString(4));
                }
                adapter.filterLists(id, name, capacity, location);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        products_RV = findViewById(R.id.vendings_view);
        addVengingButton = findViewById(R.id.vendings_add);

        addVengingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendingsOption.this, AddVendingsActivity.class);
                startActivity(intent);
            }
        });

        mydb = new MyDataBaseHelper(VendingsOption.this);
        storeVendingDataInArrays();
    }

    private void filter(String newText) {
        ArrayList idFilter, nameFilter, locationFilter, capacityFilter;
        idFilter = new ArrayList<>();
        nameFilter = new ArrayList<>();
        locationFilter = new ArrayList<>();
        capacityFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            if (name.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT))) {
                idFilter.add(id.get(i));
                nameFilter.add(name.get(i));
                locationFilter.add(location.get(i));
                capacityFilter.add(capacity.get(i));
            }
        }
        if (idFilter.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterLists(idFilter, nameFilter, capacityFilter, locationFilter);
        }
    }

    public void storeVendingDataInArrays() {
        id = new ArrayList<>();
        name = new ArrayList<>();
        location = new ArrayList<>();
        capacity = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Cursor cursor = mydb.readVendingsData(userid);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                capacity.add(cursor.getString(2));
                name.add(cursor.getString(3));
                location.add(cursor.getString(4));
            }
        }

        adapter = new customVendingAdapter(VendingsOption.this, VendingsOption.this, id, name, capacity, location);
        products_RV.setAdapter(adapter);
        products_RV.setLayoutManager(new LinearLayoutManager(VendingsOption.this));
    }
}