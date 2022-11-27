package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ServiceDescUpdate extends AppCompatActivity {
    EditText name_text,price_text,desc_text;

    String extra_id;

    Button del,update;

    MyDataBaseHelper dbhelper;
    SQLiteDatabase sqlite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_desc_update);

        init();

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("DELETE FROM servicesDesc WHERE _id = " + extra_id);
                Toast.makeText(ServiceDescUpdate.this, "Удалено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Cursor cursor = sqlite.rawQuery("SELECT * FROM servicesDesc WHERE _id = " + extra_id,null);
        cursor.moveToFirst();
        name_text.setText(cursor.getString(1));
        price_text.setText(cursor.getString(3));
        desc_text.setText(cursor.getString(2));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_text.getText().toString().isEmpty() || price_text.getText().toString().isEmpty() || desc_text.getText().toString().isEmpty()){
                    Toast.makeText(ServiceDescUpdate.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    sqlite.execSQL("UPDATE servicesDesc SET name = '" + name_text.getText().toString().trim() +
                            "', servicesdesc = '" + desc_text.getText().toString().trim() +
                            "', price = '" + price_text.getText().toString().trim() +
                            "' WHERE _id = " + extra_id);
                    Toast.makeText(ServiceDescUpdate.this, "Обновлено!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void init(){
        dbhelper = new MyDataBaseHelper(this);
        sqlite = dbhelper.getWritableDatabase();
        extra_id = getIntent().getStringExtra("id");
        del = findViewById(R.id.delServicesDescBtn);
        update = findViewById(R.id.updateServicesDescBtn);
        name_text = findViewById(R.id.servicesdesc_name_Update);
        price_text = findViewById(R.id.servicesdesc_price_Update);
        desc_text = findViewById(R.id.servicesdesc_desc_Update);
    }
}