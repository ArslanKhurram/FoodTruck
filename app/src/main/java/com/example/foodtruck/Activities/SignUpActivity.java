package com.example.foodtruck.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

public class SignUpActivity extends AppCompatActivity {

    public Customer newCust = new Customer();
    public Vendor newVen = new Vendor();

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