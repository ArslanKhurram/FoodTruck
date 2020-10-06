package com.example.foodtruck.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

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
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you want to cancel sign up?");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertdialog.create();
        alertdialog.show();

    }

}