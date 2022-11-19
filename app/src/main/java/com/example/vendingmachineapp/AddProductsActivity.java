package com.example.vendingmachineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddProductsActivity extends AppCompatActivity {

    EditText name, desc, price, amount, amountinbox, weight;
    ImageView productImage;
    Button addBtn,chooseImage;

    final int REQUEST_CODE_GALLERY = 1030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        productImage = findViewById(R.id.productImage);
        chooseImage = findViewById(R.id.chooseProductImage);
        name = findViewById(R.id.nameInput);
        desc = findViewById(R.id.descInput);
        price = findViewById(R.id.priceInput);
        amount = findViewById(R.id.amountInput);
        amountinbox = findViewById(R.id.amountinboxInput);
        weight = findViewById(R.id.weightInput);

        addBtn = findViewById(R.id.addingProductbutton);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(AddProductsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBaseHelper mydb = new MyDataBaseHelper(AddProductsActivity.this);
                try {
                    mydb.addProduct(
                            name.getText().toString().trim(),
                            desc.getText().toString().trim(),
                            Float.valueOf(price.getText().toString().trim()),
                            Integer.valueOf(amount.getText().toString().trim()),
                            Integer.valueOf(amountinbox.getText().toString().trim()),
                            Float.valueOf(weight.getText().toString().trim()),
                            imageViewToByte(productImage));
                }
                catch (Exception e){
                    Toast.makeText(AddProductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,requestCode);
            }
            else{
                Toast.makeText(this, "Нет прав на загрузку изображений!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                productImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}