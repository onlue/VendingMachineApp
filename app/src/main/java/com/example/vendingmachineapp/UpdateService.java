package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateService extends AppCompatActivity {

    EditText date;
    TextView address,desc;

    Spinner service_spin,machine_spin;

    MyDataBaseHelper myDataBaseHelper;

    Button del,update,dateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service);

        init();

        ArrayList<String> machinesName = new ArrayList<>();
        ArrayList<String> machineId = new ArrayList<>();
        ArrayList<String> serviceName = new ArrayList<>();
        ArrayList<String> serviceId = new ArrayList<>();

        myDataBaseHelper = new MyDataBaseHelper(UpdateService.this);
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        Cursor cursor = myDataBaseHelper.readMachineInfoDataForSpinner(userid);
        while(cursor.moveToNext()){
            machinesName.add(cursor.getString(0));
            machineId.add(cursor.getString(1));
        }

        cursor = myDataBaseHelper.readServiceDescData();
        while(cursor.moveToNext()){
            serviceName.add(cursor.getString(0));
            serviceId.add(cursor.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, machinesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machine_spin.setAdapter(adapter);

        ArrayAdapter<String> adapterSpin_ = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, serviceName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_spin.setAdapter(adapterSpin_);

        int idMachine = 0,IdService = 0;
        String extra_machine_id,extra_service_id;
        extra_machine_id = getIntent().getStringExtra("extra_machine_id");
        extra_service_id = getIntent().getStringExtra("extra_service_id");

        for (int i = 0; i < machinesName.size(); i+=1){
            if(machinesName.get(i).contains(extra_machine_id)){
                idMachine = i;
            }
        }

        for (int i = 0; i < serviceName.size(); i+=1){
            if(serviceName.get(i).contains(extra_service_id)){
                IdService = i;
            }
        }

        service_spin.setSelection(IdService);
        machine_spin.setSelection(idMachine);

        cursor = db.rawQuery("SELECT servicesdesc FROM servicesDesc WHERE _id = " + serviceId.get(IdService),null);
        cursor.moveToFirst();
        desc.setText(cursor.getString(0));

        cursor = db.rawQuery("SELECT location FROM vending_machines WHERE _id = " + machineId.get(idMachine),null);
        cursor.moveToFirst();
        address.setText(cursor.getString(0));

        cursor = db.rawQuery("SELECT mydate FROM services WHERE _id = " + getIntent().getStringExtra("id"),null);
        cursor.moveToFirst();
        date.setText(cursor.getString(0));

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("DELETE FROM services WHERE _id = " + getIntent().getStringExtra("id"));
                Toast.makeText(UpdateService.this, "Удалено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempMachineId = machineId.get(Integer.valueOf(String.valueOf(machine_spin.getSelectedItemId())));
                String tempServId = serviceId.get(Integer.valueOf(String.valueOf(service_spin.getSelectedItemId())));

                db.execSQL("UPDATE services SET idServ = " + tempServId +
                        ", servicesMachine = " + tempMachineId +
                        ", mydate = '" + date.getText().toString() + "' WHERE _id = " + getIntent().getStringExtra("id"));
                Toast.makeText(UpdateService.this, "Обновлено!", Toast.LENGTH_SHORT).show();
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
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
                        date.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void init(){
        dateBtn = findViewById(R.id.dateButtonUpdate);
        date = findViewById(R.id.date_text_update);
        address = findViewById(R.id.locationAdres);
        desc = findViewById(R.id.serviceDesc);
        service_spin = findViewById(R.id.service_spinnerUpdate);
        machine_spin = findViewById(R.id.machine_spinnerUpdate);
        del = findViewById(R.id.delService);
        update = findViewById(R.id.updateService);
    }
}