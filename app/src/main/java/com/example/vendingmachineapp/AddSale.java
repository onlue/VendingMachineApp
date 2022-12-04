package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddSale extends AppCompatActivity {

    EditText date_edit, amount;

    Spinner machine_spin, product_spin;

    ArrayList<String> productName, productId;
    ArrayList<String> machinesName, machineId;
    
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        date_edit = findViewById(R.id.dateEdit);
        amount = findViewById(R.id.SaleEditAmount);

        initSpinners();

        findViewById(R.id.PickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        });
        
        findViewById(R.id.AddSale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date_edit.getText().toString().isEmpty()){
                    Toast.makeText(AddSale.this, "Выберите дату!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(AddSale.this, "Введите количество!", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyDataBaseHelper db = new MyDataBaseHelper(AddSale.this);

                SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                long userid = sharedPreferences.getLong("user_id", -1);

                db.AddSale(machineId.get(machine_spin.getSelectedItemPosition()),productId.get(product_spin.getSelectedItemPosition()),amount.getText().toString(),date_edit.getText().toString(),String.valueOf(userid));
            }
        });

        machine_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initProducts(Integer.valueOf(machineId.get(machine_spin.getSelectedItemPosition())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    public void callDatePicker(){
        int mYear, mMonth, mDay;
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day,month;
                        monthOfYear +=1;
                        month = monthOfYear < 10 ? "0"+monthOfYear : String.valueOf(monthOfYear);
                        day = dayOfMonth < 10 ? "0"+dayOfMonth : String.valueOf(dayOfMonth);
                        String editTextDateParam = year + "-" + month + "-" + day;
                        date_edit.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void initProducts(int id){
        MyDataBaseHelper db = new MyDataBaseHelper(AddSale.this);
        SQLiteDatabase sqlite = db.getWritableDatabase();

        productName = new ArrayList<>();
        productId = new ArrayList<>();

        String query = "select products.name, machine_capacity.product_id from machine_capacity inner join products on machine_capacity.product_id = products._id WHERE machine_capacity.machine_id = " + id;
        Cursor cursor = sqlite.rawQuery(query,null);


        while(cursor.moveToNext()){
            productName.add(cursor.getString(0));
            productId.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapterSpin_ = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productName);
        adapterSpin_.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_spin.setAdapter(adapterSpin_);
    }

    public void initSpinners(){
        MyDataBaseHelper db = new MyDataBaseHelper(AddSale.this);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        machinesName = new ArrayList<>();
        machineId = new ArrayList<>();

        machine_spin = findViewById(R.id.SaleMachineSpin);
        product_spin = findViewById(R.id.SaleProductSpin);

        String query = "select DISTINCT machine_capacity.machine_id, vending_machines.name from machine_capacity inner join vending_machines on machine_capacity.machine_id = vending_machines._id;";
        Cursor cursor = sqlite.rawQuery(query,null);
        while(cursor.moveToNext()){
            machinesName.add(cursor.getString(1));
            machineId.add(cursor.getString(0));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, machinesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machine_spin.setAdapter(adapter);

        initProducts(Integer.valueOf(machineId.get(machineId.size()-1)));

    }
}