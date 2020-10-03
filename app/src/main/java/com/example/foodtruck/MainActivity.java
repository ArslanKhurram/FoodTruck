package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Spinner;

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
        cc.addCustomer("Arslan", "Adam", "khura6@farmingdale.edu","adbc", "631-123-4567","Apple St","1234","11632","Green" ,  "NY");

        Customer cus;
        cus = cc.getCustomerIdByEmail("khura6@farmingdale.edu");

        PaymentsContract pc = new PaymentsContract(this);
        pc.open();
        pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20", cus.getM_Id());

    }

    public void ToSignUp(View view) {
        Spinner spinChoice = findViewById(R.id.spnLoginType);
        SharedPreferences sharePref;
        //SharedPreferences.Editor editor = sharePref.edit();
        Intent i = new Intent(MainActivity.this, activity_signup.class);
        Fragment fragSignup = null;

      //  getSupportFragmentManager().beginTransaction().replace(R.id.fragSignUp)
      //  var fragV =
      //  switch(spinChoice.getSelectedItem().toString()){
      //    case "User":
      //         fragSignup = new UsersignupFragment();
      //         break;
      //    case "Vendor":
      //         fragSignup = new VendorsignupFragment();
      //         break;
      //     default:
      //         //bun = error message
      //         break;
      // }
      // if(fragSignup != null)
      //     i.putExtra("SignUpMethod", (Parcelable) fragSignup);
        startActivity(i);
    }
}