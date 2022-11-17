package com.example.vendingmachineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText login_field, password_field;

    Button logbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_field = findViewById(R.id.authLoginField);
        password_field = findViewById(R.id.authPassField);
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper db = new MyDataBaseHelper(LoginActivity.this);
                db.Authorize(login_field.getText().toString(), password_field.getText().toString());
            }
        });

    }
}