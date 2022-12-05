package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    TextView email, name, fio;
    SharedPreferences sharedPreferences;
    ImageView profilePic;

    TextView option_selled, option_machinecapacity, option_selledFor, option_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        initFields();
    }

    public void initFields() {
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();

        long userId = sharedPreferences.getLong("user_id", -1);

        if(userId == -1){
            Toast.makeText(this, "Авторизуйтесь!", Toast.LENGTH_SHORT).show();
            return;
        }

        String query = "select image from Authorization where _id = " + userId;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(0), 0, cursor.getBlob(0).length);
        profilePic.setImageBitmap(Bitmap.createScaledBitmap(bmp,350, 350, false));

        query = "SELECT SUM(amount) from sale WHERE saleCustomer = " + userId + " AND date(mydate) >= date('now', '-1 month')";

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        int tempAmountSelled = (cursor.getString(0) == "null") ? 0 : cursor.getInt(0);

        option_selled.setText("Продано за 30 дней: " + tempAmountSelled);

        query = "select Sum(machine_capacity.AmountInStock),Sum(vending_machines.capacity) from machine_capacity inner join vending_machines on machine_capacity.machine_id = vending_machines._id WHERE vending_machines.customerid = " + userId;

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        int tempCapacityOne = (cursor.getString(0) == "null") ? 0 : cursor.getInt(0);
        int tempCapacitySecond = (cursor.getString(1) == "null") ? 0 : cursor.getInt(1);

        option_machinecapacity.setText("Заполненность: " + tempCapacityOne + "/" + tempCapacitySecond);

        query = "select sum(products.price * sale.amount) from sale inner join products on sale.productId = products._id where date(sale.mydate) >= date('now','-1 month') and sale.saleCustomer = " + userId;

        cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        int selledFor = (cursor.getString(0) == "null") ? 0 : cursor.getInt(0);

        option_selledFor.setText("Продано на: " + selledFor + "₽");

        query = "select sum(servicesDesc.price) from servicesDesc inner join services on services.idServ = servicesDesc._id where services.mydate >= date('now', '-1 month') and services.servicesCustomer = " + userId;

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        int tempServiceCost = (cursor.getString(0) == "null") ? 0 : cursor.getInt(0);

        option_service.setText("Услуг на: " + tempServiceCost + "₽");
    }

    public void init() {
        profilePic = findViewById(R.id.profilePicture);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        email = findViewById(R.id.profile_Mail);
        name = findViewById(R.id.profile_Name);
        fio = findViewById(R.id.profile_FIO);

        email.setText(sharedPreferences.getString("user_email", ""));
        name.setText(sharedPreferences.getString("user_login", ""));
        fio.setText(sharedPreferences.getString("user_fio", ""));

        option_selled = findViewById(R.id.profile_selledAmount);
        option_machinecapacity = findViewById(R.id.profile_MachineCapacity);
        option_selledFor = findViewById(R.id.profile_moneyAmount);
        option_service = findViewById(R.id.profile_ServiceCost);
    }
}