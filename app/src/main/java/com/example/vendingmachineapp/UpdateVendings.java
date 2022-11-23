package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateVendings extends AppCompatActivity {

    String extra_id;
    EditText location,name,courier,capacity;
    Button delMachine, updateMachine;

    MyDataBaseHelper dbhelper;
    SQLiteDatabase sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendings);

        init();

        dbhelper = new MyDataBaseHelper(this);
        sqlite = dbhelper.getWritableDatabase();

        Cursor cursor = null;

        if(sqlite != null){
            cursor = sqlite.rawQuery("SELECT * FROM vending_machines WHERE _id = " + extra_id, null);
            cursor.moveToFirst();
            capacity.setText(cursor.getString(2));
            name.setText(cursor.getString(3));
            location.setText(cursor.getString(4));
            courier.setText(cursor.getString(5));
        }
        cursor.close();

        delMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("DELETE FROM vending_machines WHERE _id = " + Integer.valueOf(extra_id));
                Toast.makeText(UpdateVendings.this, "Удалено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        updateMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.execSQL("UPDATE vending_machines SET name = '" + name.getText().toString().trim() +
                        "', location = '" + location.getText().toString().trim() +
                        "', courier = '" + courier.getText().toString().trim() +
                        "', capacity = " + capacity.getText().toString().trim() +
                        " WHERE _id = " + extra_id);
                Toast.makeText(UpdateVendings.this, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init(){
        location = findViewById(R.id.machineLocationEdit);
        name = findViewById(R.id.machinenameEdit);
        courier = findViewById(R.id.machineCourierEdit);
        capacity = findViewById(R.id.machineCapacityEdit);

        delMachine = findViewById(R.id.deleteMachine);
        updateMachine = findViewById(R.id.editMachine);

        extra_id = getIntent().getStringExtra("id");
    }
}