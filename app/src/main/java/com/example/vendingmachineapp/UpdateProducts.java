package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProducts extends AppCompatActivity {

    String extra_id;
    EditText name, desc, price, amount, amountinbox, weight;
    Button delete, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_products);
        getId();
        name = findViewById(R.id.nameInputEdit);
        desc = findViewById(R.id.descInputEdit);
        price = findViewById(R.id.priceInputEdit);
        amount = findViewById(R.id.amountInputEdit);
        amountinbox = findViewById(R.id.amountinboxInputEdit);
        weight = findViewById(R.id.weightInputEdit);
        delete = findViewById(R.id.deleteProductButton);
        edit = findViewById(R.id.editingProductButton);

        MyDataBaseHelper dbhelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqlite = dbhelper.getWritableDatabase();

        Cursor cursor = null;
        if (sqlite != null) {
            cursor = sqlite.rawQuery("SELECT * FROM products WHERE _id = " + extra_id, null);
            cursor.moveToFirst();
            name.setText(cursor.getString(1));
            desc.setText(cursor.getString(2));
            price.setText(String.valueOf(cursor.getFloat(3)));
            amount.setText(String.valueOf(cursor.getInt(4)));
            amountinbox.setText(String.valueOf(cursor.getInt(5)));
            weight.setText(String.valueOf(cursor.getFloat(6)));
        }
        cursor.close();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("DELETE FROM products WHERE _id = " + Integer.valueOf(extra_id));
                Toast.makeText(UpdateProducts.this, "Удалено!", Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("UPDATE products SET name = '" + name.getText().toString() +
                        "',description = '" + desc.getText().toString() +
                        "', price =  " + price.getText().toString() +
                        ", amount = " + amount.getText().toString() +
                        ", amountinbox = " + amountinbox.getText().toString() +
                        ", weight = " + weight.getText().toString() +
                        " WHERE _id = " + Integer.valueOf(extra_id));
                Toast.makeText(UpdateProducts.this, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getId() {
        extra_id = getIntent().getStringExtra("id");
    }
}