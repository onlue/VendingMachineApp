package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateVendingsInfo extends AppCompatActivity {

    EditText capacity_edit;
    Spinner machinename, products;

    Button delete, update;

    MyDataBaseHelper dbhelper;
    SQLiteDatabase sqlite;

    String amount;
    int tempIdMachine = 0, tempIdProduct = 0;

    String extra_id, extra_machine_id, extra_product_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendings_info);

        init();

        ArrayList<String> machinesName = new ArrayList<>();
        ArrayList<String> machineId = new ArrayList<>();
        ArrayList<String> productsName = new ArrayList<>();
        ArrayList<String> productId = new ArrayList<>();

        dbhelper = new MyDataBaseHelper(this);
        sqlite = dbhelper.getWritableDatabase();


        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        dbhelper = new MyDataBaseHelper(this);
        Cursor cursor = null;

        cursor = dbhelper.readMachineInfoDataForSpinner(userid);
        while(cursor.moveToNext()){
            machinesName.add(cursor.getString(0));
            machineId.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, machinesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machinename.setAdapter(adapter);

        cursor = dbhelper.readProductDataForSpinner();
        while(cursor.moveToNext()){
            productsName.add(cursor.getString(0));
            productId.add(cursor.getString(1));
        }

        ArrayAdapter<String> ProductAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productsName);
        ProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        products.setAdapter(ProductAdapter);

        for (int i = 0; i < machinesName.size(); i+=1){
            if(machinesName.get(i).contains(extra_machine_id)){
                tempIdMachine = i;
            }
        }

        for (int i = 0; i < productsName.size(); i+=1){
            if(productsName.get(i).contains(extra_product_id)){
                tempIdProduct = i;
            }
        }

        products.setSelection(tempIdProduct);
        machinename.setSelection(tempIdMachine);

        capacity_edit.setText(amount);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("DELETE FROM machine_capacity WHERE _id = " + Integer.valueOf(extra_id));
                Toast.makeText(UpdateVendingsInfo.this, "Удалено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempMachineId = machineId.get(Integer.valueOf(String.valueOf(machinename.getSelectedItemId())));
                String tempProductId = productId.get(Integer.valueOf(String.valueOf(products.getSelectedItemId())));

                Cursor cursor = null;
                String query = "select capacity from vending_machines where _id = " + tempMachineId;
                cursor = sqlite.rawQuery(query,null);
                cursor.moveToFirst();
                int machine_capacity = cursor.getInt(0);

                if(Integer.valueOf(capacity_edit.getText().toString().trim()) > machine_capacity){
                    Toast.makeText(UpdateVendingsInfo.this, "Машина переполнена!", Toast.LENGTH_SHORT).show();
                    return;
                }

                sqlite.execSQL("UPDATE machine_capacity SET machine_id = " + tempMachineId +
                        ", product_id = " + tempProductId +
                        ", AmountInStock = " + capacity_edit.getText().toString().trim() + " WHERE _id = " + extra_id);
                Toast.makeText(UpdateVendingsInfo.this, "Обновлено!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init(){
        capacity_edit = findViewById(R.id.capacityAddEdit);
        machinename = findViewById(R.id.spinnerMachinenameEdit);
        products = findViewById(R.id.spinnerProdcuctNameEdit);
        delete = findViewById(R.id.deleteMachineInfo);
        update = findViewById(R.id.editMachineInfo);
        extra_id = getIntent().getStringExtra("id");
        extra_machine_id = getIntent().getStringExtra("machine");
        extra_product_id = getIntent().getStringExtra("product");
        amount = getIntent().getStringExtra("amount");

    }
}