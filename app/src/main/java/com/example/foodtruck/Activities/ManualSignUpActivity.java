package com.example.foodtruck.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.example.foodtruck.Fragments.VendorsignupFragment;

public class ManualSignUpActivity extends AppCompatActivity {
    public Customer newCust = new Customer();
    public Vendor newVen = new Vendor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualsignup);
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

                ManualSignUpActivity.this.finish();
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