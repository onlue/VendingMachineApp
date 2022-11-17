package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddVendingsActivity extends AppCompatActivity {
    Button addButton;

    EditText name,capacity,location,courier;

    MyDataBaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendings);

        name = findViewById(R.id.machinename);
        capacity = findViewById(R.id.machineCapacity);
        location = findViewById(R.id.machineLocation);
        courier = findViewById(R.id.machineCourier);

        addButton = findViewById(R.id.addMachine);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb = new MyDataBaseHelper(AddVendingsActivity.this);
                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                int userid = (int)sharedPreferences.getLong("user_id",-1);
                if(userid == -1){
                    Toast.makeText(AddVendingsActivity.this, "Авторизируйтесь!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mydb.AddMachine(userid,
                        Integer.valueOf(capacity.getText().toString().trim()),
                        name.getText().toString().trim(),
                        location.getText().toString().trim(),
                        courier.getText().toString().trim());
            }
        });
    }
}