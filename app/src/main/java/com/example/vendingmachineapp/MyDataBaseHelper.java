package com.example.vendingmachineapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class MyDataBaseHelper extends SQLiteOpenHelper {

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
                        COLUMN_WEIGHT + " REAL);";

        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_LOGIN +
                "(" + COLUNM_CUSTOMERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_FIO + " TEXT, " +
                COLUMN_EMAIL + " TEXT);";

        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_NAME_MACHINES + "( " +
                COLUMN_VENDINGMACHINEID + " INTEGER PRIMARY KEY, " +
                COLUMN_MACHINECUSTOMER + "INTEGER REFERENCES " + TABLE_NAME_LOGIN + "(" + COLUNM_CUSTOMERID + ") ON DELETE CASCADE, " +
                COLUMN_CAPACITY + "INTEGER, " +
                COLUMN_MACHINENAME  + "TEXT, " +
                COLUMN_LOCATION + "TEXT, " +
                COLUMN_COURIER + "TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MACHINES);
        onCreate(db);
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

        if (password.trim().length() <= 8) {
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

    public void addProduct(String name, String description, float price, int amount, int amountinbox, float weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_AMOUNTINBOX, amountinbox);
        values.put(COLUMN_WEIGHT, weight);

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
        trueLogin = cursor.getString(1);
        truePassword = cursor.getString(2);

        if(String.valueOf(trueLogin).equals(String.valueOf(Login)) && String.valueOf(truePassword).equals(String.valueOf(Password))){
            Toast.makeText(context, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
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
}
