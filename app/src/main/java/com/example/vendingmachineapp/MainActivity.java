package com.example.vendingmachineapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView hello,logout;

    SharedPreferences sharedPreferences;

    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profilePic = findViewById(R.id.mainMenuProfilePic);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(this);
        SQLiteDatabase sqlite = myDataBaseHelper.getWritableDatabase();

        hello = findViewById(R.id.helloText);
        logout = findViewById(R.id.logout);
        boolean temp = sharedPreferences.getBoolean("isLogined", false);
        if (temp) {
            String query = "select image from Authorization where _id = " + sharedPreferences.getLong("user_id", -1);
            Cursor cursor = sqlite.rawQuery(query,null);
            cursor.moveToFirst();
            Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(0), 0, cursor.getBlob(0).length);
            profilePic.setImageBitmap(Bitmap.createScaledBitmap(bmp,300, 300, false));
            hello.setText("Здравствуйте, " + sharedPreferences.getString("user_fio", "") + "!");
            findViewById(R.id.authBtn).setVisibility(View.INVISIBLE);
            findViewById(R.id.regBtn).setVisibility(View.INVISIBLE);
            TextView loginText = findViewById(R.id.ProfileName);
            loginText.setText(sharedPreferences.getString("user_login","ERROR"));
        }
        else{
            findViewById(R.id.authBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.regBtn).setVisibility(View.VISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTwoButtonsAlertDialog("Выйти?","Желаете выйти?");
            }
        });

        findViewById(R.id.GoToProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Авторизуйтсь!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);

        findViewById(R.id.sale_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaleOption.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.authBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.VendingInfo_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,VendingMachineInfoOptions.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.regBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAccount.class);
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, ProductsOption.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.vendings_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,VendingsOption.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ServicesDesc_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ServicesDescOption.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.Services_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ServiceOption.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    public void createTwoButtonsAlertDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton("Нет",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        builder.setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        finishAffinity();
                    }
                });
        builder.show();
    }

}



