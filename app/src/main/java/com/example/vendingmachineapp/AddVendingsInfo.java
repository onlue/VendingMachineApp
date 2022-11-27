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
import java.util.HashMap;
import java.util.Map;

public class AddVendingsInfo extends AppCompatActivity {

    Button addButton;
    EditText addCapity;
    Spinner productSpinner, machineSpinner;

    MyDataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendings_info);
        ArrayList<String> machinesName = new ArrayList<>();
        ArrayList<String> machineId = new ArrayList<>();
        ArrayList<String> productsName = new ArrayList<>();
        ArrayList<String> productId = new ArrayList<>();

        addButton = findViewById(R.id.addMachineInfo);
        addCapity = findViewById(R.id.capacityAdd);

        productSpinner = findViewById(R.id.spinnerProdcuctName);
        machineSpinner = findViewById(R.id.spinnerMachinename);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        mydb = new MyDataBaseHelper(this);
        Cursor cursor = null;

        cursor = mydb.readMachineInfoDataForSpinner(userid);
        while(cursor.moveToNext()){
            machinesName.add(cursor.getString(0));
            machineId.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, machinesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machineSpinner.setAdapter(adapter);

        cursor = mydb.readProductDataForSpinner();
        while(cursor.moveToNext()){
            productsName.add(cursor.getString(0));
            productId.add(cursor.getString(1));
        }

        ArrayAdapter<String> ProductAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productsName);
        ProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(ProductAdapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addCapity.getText().toString().isEmpty()){
                    Toast.makeText(AddVendingsInfo.this, "Введите все значения!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tempMachineId = machineId.get(Integer.valueOf(String.valueOf(machineSpinner.getSelectedItemId())));
                String tempProductId = productId.get(Integer.valueOf(String.valueOf(productSpinner.getSelectedItemId())));

                Cursor cursor = null;
                SQLiteDatabase db = mydb.getWritableDatabase();
                String query = "select capacity from vending_machines where _id = " + tempMachineId;
                cursor = db.rawQuery(query,null);
                cursor.moveToFirst();
                int machine_capacity = cursor.getInt(0);

                query = "select COUNT(*) from machine_capacity where machine_id = " + tempMachineId;
                cursor = db.rawQuery(query,null);
                cursor.moveToFirst();
                if(cursor.getInt(0) > 0){
                    Toast.makeText(AddVendingsInfo.this, "Данный товар уже есть в таблице, обновите наличие!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Integer.valueOf(addCapity.getText().toString()) > machine_capacity){
                    Toast.makeText(AddVendingsInfo.this, "Невозможно добавить, машина переполнена!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    mydb.AddVendingInfo(tempMachineId,tempProductId,addCapity.getText().toString().trim());
                    Toast.makeText(AddVendingsInfo.this, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}