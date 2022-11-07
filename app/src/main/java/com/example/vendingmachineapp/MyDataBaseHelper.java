package com.example.vendingmachineapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "VendingDB";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_AMOUNTINBOX = "amountinbox";
    public static final String COLUMN_WEIGHT = "weight";

    public MyDataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_PRICE + " REAL, " +
                        COLUMN_AMOUNT + " INTEGER, " +
                        COLUMN_AMOUNTINBOX + " INTEGER, " +
                        COLUMN_WEIGHT + " REAL);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addProduct(String name, String description,float price, int amount, int amountinbox, float weight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME,name);
        values.put(COLUMN_DESCRIPTION,description);
        values.put(COLUMN_PRICE,price);
        values.put(COLUMN_AMOUNT,amount);
        values.put(COLUMN_AMOUNTINBOX,amountinbox);
        values.put(COLUMN_WEIGHT,weight);

        long result = db.insert(TABLE_NAME,null,values);
        
        if(result == -1){
            Toast.makeText(context, "Ошибка добавления!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readProductsData(){
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        return cursor;
    }
}
