package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.models.Customer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeEntry(View view) {

        CustomersContract cc = new CustomersContract(this);
        cc.open();
        cc.addCustomer("Arslan", "Adam", "khura6@farmingdale.edu","631-123-4567", "Apple St","1234","11777","Adams Town","PA");

        Customer cus;
        cus = cc.getCustomerIdByEmail("khura6@farmingdale.edu");

        PaymentsContract pc = new PaymentsContract(this);
        pc.open();
        pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20", cus.getM_Id());

    }
}