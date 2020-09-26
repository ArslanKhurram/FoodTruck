package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;

import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.view.View;

import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.models.Customer;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeEntry(View view) {

        CustomersContract cc = new CustomersContract(this);
        cc.addCustomer("Arslan", "Adam", "khura6@farmingdale.edu", "ILovePie","631-123-4567", "Apple St","1234","11777","Adams Town","PA");



        VendorsContract vc = new VendorsContract(this);
        vc.addVendor("Mike","Pear","mpwar@gmail.com", "MikeIstheBest1234","516-920-0202","Maple Ave","987","10393","Apple Village","VA", (Calendar.getInstance().getTime()).toString(), "Pizza")  ;


        Customer cus;
        cus = cc.getCustomerIdByEmail("khura6@farmingdale.edu");


        PaymentsContract pc = new PaymentsContract(this);
        pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20", cus.getM_Id());

    }
}