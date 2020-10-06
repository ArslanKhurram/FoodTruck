package com.example.foodtruck.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

public class SignUpActivity extends AppCompatActivity {

    public Customer customer = new Customer();
    public Vendor vendor = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //open login fragment on start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }


    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}