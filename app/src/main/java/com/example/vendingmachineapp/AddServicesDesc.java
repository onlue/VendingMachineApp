package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddServicesDesc extends AppCompatActivity {

    Button addButton;
    EditText desc,name,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_desc);

        init();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper db = new MyDataBaseHelper(AddServicesDesc.this);

                db.AddServicesDesc(name.getText().toString().trim(),desc.getText().toString().trim(),price.getText().toString().trim());
            }
        });
    }

    public void init(){
        desc = findViewById(R.id.servicesdesc_desc_add);
        name = findViewById(R.id.servicesdesc_name_add);
        price = findViewById(R.id.servicesdesc_price_add);
        addButton = findViewById(R.id.addServicesDescBtn);
    }
}