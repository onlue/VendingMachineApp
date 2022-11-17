package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddProductsActivity extends AppCompatActivity {

    EditText name, desc, price, amount, amountinbox, weight;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        name = findViewById(R.id.nameInput);
        desc = findViewById(R.id.descInput);
        price = findViewById(R.id.priceInput);
        amount = findViewById(R.id.amountInput);
        amountinbox = findViewById(R.id.amountinboxInput);
        weight = findViewById(R.id.weightInput);

        addBtn = findViewById(R.id.addingProductbutton);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper mydb = new MyDataBaseHelper(AddProductsActivity.this);
                mydb.addProduct(
                        name.getText().toString().trim(),
                        desc.getText().toString().trim(),
                        Float.valueOf(price.getText().toString().trim()),
                        Integer.valueOf(amount.getText().toString().trim()),
                        Integer.valueOf(amountinbox.getText().toString().trim()),
                        Float.valueOf(weight.getText().toString().trim()));
            }
        });
    }
}