package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddService extends AppCompatActivity {

    EditText date_edit;

    Button dateButton,addButton;

    Spinner service_spin,machine_spin;

    MyDataBaseHelper mydb;

    ArrayList<String> machinesName,machineId;
    ArrayList<String> serviceName,serviceId;

    long userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        mydb = new MyDataBaseHelper(AddService.this);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        userid = sharedPreferences.getLong("user_id", -1);

        init();
        initSpinners();

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    public void add(){
        String tempMachineId = machineId.get(Integer.valueOf(String.valueOf(machine_spin.getSelectedItemId())));
        String tempProductId = serviceId.get(Integer.valueOf(String.valueOf(service_spin.getSelectedItemId())));

        if(date_edit.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Введите дату!", Toast.LENGTH_SHORT).show();
            return;
        }

        mydb.AddServices(tempProductId,String.valueOf(userid),tempMachineId,date_edit.getText().toString());
        Toast.makeText(AddService.this, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
    }

    public void init(){
        dateButton = findViewById(R.id.dateButton);
        date_edit = findViewById(R.id.date_text);
        service_spin = findViewById(R.id.service_spinner);
        machine_spin = findViewById(R.id.machine_spinner);
        addButton = findViewById(R.id.addService);
    }

    public void initSpinners(){

        machinesName = new ArrayList<>();
        machineId = new ArrayList<>();
        serviceName = new ArrayList<>();
        serviceId = new ArrayList<>();

        Cursor cursor = mydb.readMachineInfoDataForSpinner(userid);
        while(cursor.moveToNext()){
            machinesName.add(cursor.getString(0));
            machineId.add(cursor.getString(1));
        }

        cursor = mydb.readServiceDescData();
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
}