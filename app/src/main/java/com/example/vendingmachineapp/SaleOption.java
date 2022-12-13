package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SaleOption extends AppCompatActivity {

    SearchView sale_SV;
    RecyclerView sale_RV;
    FloatingActionButton addSale;

    MyDataBaseHelper mydb;
    ArrayList<String> amount, id, machine, product,date;
    customSaleAdapter adapter;

    Button sortDateASC, sortDateDESC;
    Button backButton, dateOneBtn, dateTwoBtn, submitDates;
    TextView dateOne, dateTwo;
    ImageView updateSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_option);

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        init();
        mydb = new MyDataBaseHelper(SaleOption.this);
        storeDataInArrays();

        addSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleOption.this,AddSale.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = new ArrayList<>();
                id = new ArrayList<>();
                machine = new ArrayList<>();
                product = new ArrayList<>();
                date = new ArrayList<>();

                Cursor cursor = mydb.readSaleData(userid);

                while (cursor.moveToNext()) {
                    id.add(cursor.getString(0));
                    machine.add(cursor.getString(1));
                    product.add(cursor.getString(2));
                    date.add(cursor.getString(3));
                    amount.add(cursor.getString(4));
                }
                adapter.filterLists(product,machine,date,id,amount);
            }
        });

        sale_SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        dateOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePickerFirst();
            }
        });

        dateTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePickerSecond();
            }
        });

        submitDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByDates();
            }
        });

        sortDateASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortMethod("select sale._id,vending_machines.name,products.name,sale.mydate,sale.amount from sale inner join vending_machines on sale.machineId = vending_machines._id inner join products on sale.productId = products._id WHERE saleCustomer = " + userid + " ORDER BY date(sale.mydate) ASC");
            }
        });

        sortDateDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortMethod("select sale._id,vending_machines.name,products.name,sale.mydate,sale.amount from sale inner join vending_machines on sale.machineId = vending_machines._id inner join products on sale.productId = products._id WHERE saleCustomer = " + userid + " ORDER BY date(sale.mydate) DESC");
            }
        });
    }

    private void filterByDates() {
        amount = new ArrayList<>();
        id = new ArrayList<>();
        machine = new ArrayList<>();
        product = new ArrayList<>();
        date = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SQLiteDatabase sqlite = mydb.getWritableDatabase();

        String query = "select sale._id,vending_machines.name,products.name,sale.mydate,sale.amount from sale inner join vending_machines on sale.machineId = vending_machines._id inner join products on sale.productId = products._id WHERE saleCustomer = " + userid + " AND date(sale.mydate) >= date('" + dateOne.getText().toString() + "') AND date(sale.mydate) <= date('" + dateTwo.getText().toString() + "')";
        Cursor cursor = sqlite.rawQuery(query,null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                machine.add(cursor.getString(1));
                product.add(cursor.getString(2));
                date.add(cursor.getString(3));
                amount.add(cursor.getString(4));
            }
        }
        adapter.filterLists(product,machine,date,id,amount);
    }

    public void filter(String newText){
        ArrayList<String> amountFilter, idFilter, machineFilter, productFilter, dateFilter;
        amountFilter = new ArrayList<>();
        idFilter = new ArrayList<>();
        machineFilter = new ArrayList<>();
        productFilter = new ArrayList<>();
        dateFilter = new ArrayList<>();

        for (int i = 0; i < id.size(); i++) {
            if (product.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT)) || machine.get(i).toLowerCase().contains(newText.toLowerCase(Locale.ROOT)) ) {
                idFilter.add(id.get(i));
                machineFilter.add(machine.get(i));
                productFilter.add(product.get(i));
                dateFilter.add(date.get(i));
                amountFilter.add(amount.get(i));
            }
        }
        if (idFilter.isEmpty()) {
            Toast.makeText(this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
        }
            adapter.filterLists(productFilter,machineFilter,dateFilter,idFilter,amountFilter);

    }

    private void storeDataInArrays() {
        amount = new ArrayList<>();
        id = new ArrayList<>();
        machine = new ArrayList<>();
        product = new ArrayList<>();
        date = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        long userid = sharedPreferences.getLong("user_id", -1);

        if (userid == -1) {
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = mydb.readSaleData(userid);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                machine.add(cursor.getString(1));
                product.add(cursor.getString(2));
                date.add(cursor.getString(3));
                amount.add(cursor.getString(4));
            }
        }

        adapter = new customSaleAdapter(product,machine,date,id,amount,SaleOption.this, SaleOption.this);
        sale_RV.setAdapter(adapter);
        sale_RV.setLayoutManager(new LinearLayoutManager(SaleOption.this));
    }

    public void sortMethod(String query){
        amount = new ArrayList<>();
        id = new ArrayList<>();
        machine = new ArrayList<>();
        product = new ArrayList<>();
        date = new ArrayList<>();

        SQLiteDatabase sqlite = mydb.getWritableDatabase();

        Cursor cursor = sqlite.rawQuery(query,null);

        while (cursor.moveToNext()) {
            id.add(cursor.getString(0));
            machine.add(cursor.getString(1));
            product.add(cursor.getString(2));
            date.add(cursor.getString(3));
            amount.add(cursor.getString(4));
        }
        adapter.filterLists(product,machine,date,id,amount);
    }

    public void init(){
        sortDateDESC = findViewById(R.id.sortByDateDESC);
        sortDateASC = findViewById(R.id.sortByDateASC);
        submitDates = findViewById(R.id.selectDate);
        dateOneBtn = findViewById(R.id.sale_date_first);
        dateTwoBtn = findViewById(R.id.sale_date_second);
        dateOne = findViewById(R.id.dateOne);
        dateTwo = findViewById(R.id.dateTwo);
        sale_RV = findViewById(R.id.sale_view);
        sale_SV = findViewById(R.id.sale_search);
        addSale = findViewById(R.id.sale_add);
        backButton = findViewById(R.id.backMainSale);
        updateSale = findViewById(R.id.updateSale);
    }

    public void callDatePickerFirst(){
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
                        dateOne.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void callDatePickerSecond(){
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
                        dateTwo.setText(editTextDateParam);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}