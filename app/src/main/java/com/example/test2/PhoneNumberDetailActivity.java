package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PhoneNumberDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_detail);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        TextView nameText = findViewById(R.id.DetailName);
        TextView numberText = findViewById(R.id.DetailNumber);

        nameText.setText(name);
        numberText.setText(number);
    }
}