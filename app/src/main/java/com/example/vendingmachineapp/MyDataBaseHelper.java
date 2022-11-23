package com.example.vendingmachineapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    SharedPreferences settings;

    private Context context;
    public static final String DATABASE_NAME = "VendingDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_AMOUNTINBOX = "amountinbox";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_PRODUCTIMAGE = "image";

    public static final String TABLE_NAME_LOGIN = "Authorization";
    public static final String COLUNM_CUSTOMERID = "_id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIO = "fio";
    public static final String COLUMN_EMAIL = "mail";

    public static final String TABLE_NAME_MACHINES = "vending_machines";
    public static final String COLUMN_VENDINGMACHINEID = "_id";
    public static final String COLUMN_MACHINECUSTOMER = "customerid";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_MACHINENAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_COURIER = "courier";

    public static final String TABLE_NAME_MACHINESCAPACITY = "machine_capacity";
    public static final String COLUMN_MACHINESCAPACITYID = "_id";
    public static final String COLUMN_MACHINESCAPACITYVENDINGMACHINID = "machine_id";
    public static final String COLUMN_MACHINESCAPACITYPRODUCTID = "product_id";
    public static final String COLUMN_MACHINESCAPACITYANOUNTAVAILABLE = "AmountInStock";

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");

        String query =
                "CREATE TABLE " + TABLE_NAME_PRODUCTS +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_PRICE + " REAL, " +
                        COLUMN_AMOUNT + " INTEGER, " +
                        COLUMN_AMOUNTINBOX + " INTEGER, " +
                        COLUMN_WEIGHT + " REAL, " +
                        COLUMN_PRODUCTIMAGE + " BLOB);";

        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_LOGIN +
                "(" + COLUNM_CUSTOMERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_FIO + " TEXT, " +
                COLUMN_EMAIL + " TEXT);";

        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_MACHINES + "( " +
                COLUMN_VENDINGMACHINEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MACHINECUSTOMER + " INTEGER REFERENCES " + TABLE_NAME_LOGIN + "(" + COLUNM_CUSTOMERID + ") ON DELETE CASCADE, " +
                COLUMN_CAPACITY + " INTEGER, " +
                COLUMN_MACHINENAME  + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_COURIER + " TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_MACHINESCAPACITY + "( " +
                COLUMN_MACHINESCAPACITYID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MACHINESCAPACITYVENDINGMACHINID + " INTEGER REFERENCES " + TABLE_NAME_MACHINES + "(" + COLUMN_VENDINGMACHINEID + ") ON DELETE CASCADE, " +
                COLUMN_MACHINESCAPACITYPRODUCTID + " INTEGER REFERENCES " + TABLE_NAME_PRODUCTS + "(" + COLUMN_ID + ") ON DELETE CASCADE, "+
                COLUMN_MACHINESCAPACITYANOUNTAVAILABLE + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MACHINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MACHINESCAPACITY);
        onCreate(db);
    }

    public void AddMachine(int customerId, int capacity, String name, String location,String courier){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_MACHINECUSTOMER,customerId);
        values.put(COLUMN_CAPACITY,capacity);
        values.put(COLUMN_MACHINENAME,name);
        values.put(COLUMN_LOCATION,location);
        values.put(COLUMN_COURIER,courier);

        long result = db.insert(TABLE_NAME_MACHINES, null, values);

        if (result == -1) {
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddVendingInfo(String machineId, String productId, String amountInStock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(machineId.isEmpty() || productId.isEmpty() || amountInStock.isEmpty()){
            Toast.makeText(context, "Не все поля заполнены!", Toast.LENGTH_SHORT).show();
            return;
        }

        values.put(COLUMN_MACHINESCAPACITYVENDINGMACHINID, machineId);
        values.put(COLUMN_MACHINESCAPACITYPRODUCTID, productId);
        values.put(COLUMN_MACHINESCAPACITYANOUNTAVAILABLE, amountInStock);

        long result = db.insert(TABLE_NAME_MACHINESCAPACITY, null, values);

        if (result == -1) {
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddAccount(String login, String password, String FIO, String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FIO, FIO);
        values.put(COLUMN_EMAIL, mail);

        Cursor cursor = null;
        if (login.trim().length() == 0) {
            Toast.makeText(context, "Не все поля заполнены!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.trim().length() < 8) {
            Toast.makeText(context, "Пароль должен быть более 8 символов!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (FIO.trim().length() == 0) {
            Toast.makeText(context, "Не все поля заполнены!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mail.trim().length() == 0) {
            Toast.makeText(context, "Не все поля заполнены!", Toast.LENGTH_SHORT).show();
            return;
        }

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!mail.trim().matches(regex)) {
            Toast.makeText(context, "Почта имеет неверный формат!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_LOGIN + " WHERE " + COLUMN_LOGIN + " = " + "'" + login + "'", null);

            if (cursor.getCount() >= 1) {
                Toast.makeText(context, "Логин занят, попробуйте другой!", Toast.LENGTH_SHORT).show();
                return;
            }

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_LOGIN + " WHERE " + COLUMN_EMAIL + " = " + "'" + mail + "'", null);

            if (cursor.getCount() >= 1) {
                Toast.makeText(context, "Email занят, попробуйте другой!", Toast.LENGTH_SHORT).show();
                return;
            }
            db.insert(TABLE_NAME_LOGIN, null, values);
            Toast.makeText(context, "Успешно зарегистрировано!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addProduct(String name, String description, float price, int amount, int amountinbox, float weight,byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_AMOUNTINBOX, amountinbox);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_PRODUCTIMAGE,image);

        long result = db.insert(TABLE_NAME_PRODUCTS, null, values);

        if (result == -1) {
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Authorize(String Login,String Password){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        String trueLogin;
        String truePassword;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_LOGIN + " WHERE " + COLUMN_LOGIN + " = " + "'" + Login + "'",null);
        if(cursor.getCount() <= 0){
            Toast.makeText(context, "Данного пользователя не существует!", Toast.LENGTH_SHORT).show();
            return;
        }

        cursor.moveToFirst();
        long id = cursor.getInt(0);
        trueLogin = cursor.getString(1);
        truePassword = cursor.getString(2);
        String fio = cursor.getString(3);
        String mail = cursor.getString(4);

        settings = context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if(String.valueOf(trueLogin).equals(String.valueOf(Login)) && String.valueOf(truePassword).equals(String.valueOf(Password))){
            Toast.makeText(context, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
            editor.clear();
            editor.putString("user_login",Login);
            editor.putString("user_password",Password);
            editor.putString("user_fio",fio);
            editor.putString("user_email",mail);
            editor.putLong("user_id",id);
            editor.putBoolean("isLogined",true);
            editor.apply();
        }
        else{
            Toast.makeText(context, "Неправильный логин или пароль!", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor readProductsData() {
        String query = "SELECT * FROM " + TABLE_NAME_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readVendingsData(long userId){
        String query = "SELECT * FROM " + TABLE_NAME_MACHINES + " WHERE " + COLUMN_MACHINECUSTOMER + " = "  + userId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    public Cursor readVendingInfoData(long userId){
        String query = "select machine_capacity._id, products.name, machine_capacity.AmountInStock,vending_machines.name,vending_machines.customerid from machine_capacity inner join vending_machines on machine_capacity.machine_id = vending_machines._id inner join products on products._id = machine_capacity.product_id where vending_machines.customerid = " + String.valueOf(userId);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    public Cursor readMachineInfoDataForSpinner(long userId){
        String query = "select vending_machines.name,vending_machines._id from vending_machines inner join Authorization on vending_machines.customerid = Authorization._id where Authorization._id = " + userId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }

    public Cursor readProductDataForSpinner(){
        String query = "select name,_id from products";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }
}
