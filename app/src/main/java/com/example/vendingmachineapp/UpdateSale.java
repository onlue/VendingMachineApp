package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateSale extends AppCompatActivity {

    TextView price, amount, machine, product, date;

    Button del;

    MyDataBaseHelper myDataBaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sale);

        init();

        myDataBaseHelper = new MyDataBaseHelper(UpdateSale.this);
        db = myDataBaseHelper.getWritableDatabase();

        String intentId = getIntent().getStringExtra("id");

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("DELETE FROM sale WHERE _id = " + intentId);
                Toast.makeText(UpdateSale.this, "Успешно удалено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Cursor cursor = db.rawQuery("select vending_machines.name, products.name, sale.mydate, sale.amount, products.price * sale.amount as 'price' from sale inner join products on sale.productId = products._id inner join vending_machines on vending_machines._id = sale.machineId where sale._id = " + intentId, null);
        cursor.moveToFirst();

        machine.setText("Автомат: " + cursor.getString(0));
        product.setText("Товар: "+cursor.getString(1));
        date.setText("Дата: " + cursor.getString(2));
        amount.setText("Количество: " + cursor.getString(3));
        price.setText("Цена: " + cursor.getString(4));
    }

    public void init(){
        price = findViewById(R.id.saleUpdate_Price);
        amount = findViewById(R.id.saleUpdate_amount);
        machine = findViewById(R.id.saleUpdate_machine);
        product = findViewById(R.id.saleUpdate_product);
        date = findViewById(R.id.saleUpdate_date);
        del = findViewById(R.id.delSale);
    }
}