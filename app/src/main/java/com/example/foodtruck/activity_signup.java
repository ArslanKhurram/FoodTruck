package com.example.foodtruck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.models.Vendor;

public class activity_signup extends AppCompatActivity {
    public Vendor newVen = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getIntent().getSerializableExtra("SignupType");
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.add(R.id.Signup_container,new VendorsignupFragment());
        fragTransaction.commit();
    }

    public void CreateAccount(View v){
    //   EditText txtFName = findViewById(R.id.etFirstName);
    //   EditText txtLname = findViewById(R.id.etLastName);
    //   EditText txtEmail = findViewById(R.id.etEmailAdd);
    //   EditText txtPhone = findViewById(R.id.etPhone);
    //   EditText txtStreet = findViewById(R.id.etStreetAdd);
    //   EditText txtCity = findViewById(R.id.etCity);
    //   EditText txtZip = findViewById(R.id.etZip);
    //   EditText txtPassword = findViewById(R.id.etPassword);
    //   Spinner txtState = findViewById(R.id.spnState);

        // New customer entry that will be filled in with the values of of the text boxes
        CustomersContract cc = new CustomersContract(this);
        cc.open();
        //cc.addCustomer(txtFName.getText().toString(), txtLname.getText().toString(), txtEmail.getText().toString(),txtPassword.getText().toString() ,txtPhone.getText().toString(), txtStreet.getText().toString(),"123", txtZip.getText().toString(), txtCity.getText().toString(), txtState.getSelectedItem().toString());
        cc.close();
        finish();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you want to cancel sign up?");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                activity_signup.this.finish();
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert=alertdialog.create();
        alertdialog.show();

    }
}