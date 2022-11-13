package com.example.vendingmachineapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView products_RV;
    FloatingActionButton addProductsButton;

    MyDataBaseHelper mydb;
    ArrayList<String> id, name, desc, price;
    customProductAdapter adapter;

    Button reg;
    Button log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);

        findViewById(R.id.regBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.registration_layout);
                findViewById(R.id.registrationButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyDataBaseHelper db = new MyDataBaseHelper(MainActivity.this);
                        EditText login, mail, pass, fio;
                        login = findViewById(R.id.loginField);
                        mail = findViewById(R.id.emailField);
                        pass = findViewById(R.id.passwordField);
                        fio = findViewById(R.id.fioField);
                        db.AddAccount(login.getText().toString(),
                                pass.getText().toString(),
                                fio.getText().toString(),
                                mail.getText().toString());
                    }
                });
            }
        });

        findViewById(R.id.imageM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.product_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.products_layout);
                products_RV = findViewById(R.id.product_view);
                addProductsButton = findViewById(R.id.products_add);

                addProductsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddProductsActivity.class);
                        startActivity(intent);
                    }
                });

                mydb = new MyDataBaseHelper(MainActivity.this);
                id = new ArrayList<>();
                name = new ArrayList<>();
                desc = new ArrayList<>();
                price = new ArrayList<>();
                storeProductsDataInArrays();
                adapter = new customProductAdapter(MainActivity.this,MainActivity.this,name,price,id,desc);
                products_RV.setAdapter(adapter);
                products_RV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    public void storeProductsDataInArrays(){
        Cursor cursor = mydb.readProductsData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "Нет данных!", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                desc.add(cursor.getString(2));
                price.add(cursor.getString(3));
            }
        }
        cursor.close();
    }

}



