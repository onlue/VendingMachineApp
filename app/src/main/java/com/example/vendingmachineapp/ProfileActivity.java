package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView email,name,fio;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    public void init(){
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        email = findViewById(R.id.profile_Mail);
        name = findViewById(R.id.profile_Name);
        fio = findViewById(R.id.profile_FIO);

        email.setText(sharedPreferences.getString("user_email",""));
        name.setText(sharedPreferences.getString("user_login",""));
        fio.setText(sharedPreferences.getString("user_fio",""));
    }
}