package com.example.vendingmachineapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AddAccount extends AppCompatActivity {
    EditText login, mail, pass, fio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        findViewById(R.id.registrationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper db = new MyDataBaseHelper(AddAccount.this);
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
}
