package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.foodtruck.DataBase.PaymentsContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeEntry(View view) {
        PaymentsContract pc = new PaymentsContract(this);
        pc.open();
        pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20");
    }
}