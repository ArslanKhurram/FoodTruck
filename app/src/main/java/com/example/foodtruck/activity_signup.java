package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.foodtruck.DataBase.CustomersContract;

public class activity_signup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_signup);
    }

    public void CreateAccount(View v){
        EditText txtFName = findViewById(R.id.etFirstName);
        EditText txtLname = findViewById(R.id.etLastName);
        EditText txtEmail = findViewById(R.id.etEmailAdd);
        EditText txtPhone = findViewById(R.id.etPhone);
        EditText txtStreet = findViewById(R.id.etStreetAdd);
        EditText txtCity = findViewById(R.id.etCity);
        EditText txtZip = findViewById(R.id.etZip);
        EditText txtPassword = findViewById(R.id.etPassword);
        Spinner txtState = findViewById(R.id.spnState);

        // New customer entry that will be filled in with the values of of the text boxes
        CustomersContract cc = new CustomersContract(this);
        cc.open();
        cc.addCustomer(txtFName.getText().toString(), txtLname.getText().toString(), txtEmail.getText().toString(),txtPassword.getText().toString() ,txtPhone.getText().toString(), txtStreet.getText().toString(),"123", txtZip.getText().toString(), txtCity.getText().toString(), txtState.getSelectedItem().toString());
        cc.close();
        finish();
    }
}