package com.example.foodtruck.Fragments.Vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorSignUpFragment2 extends Fragment implements View.OnClickListener {

    public SignUpActivity signUpAct;
    EditText etStreet;
    EditText etStreetNum;
    EditText etCity;
    Spinner spnState;
    EditText etZip;
    EditText etPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendorsignupstep2, container, false);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        etStreet = (EditText) view.findViewById(R.id.etVenStreetName);
        etStreetNum = (EditText) view.findViewById(R.id.etVenStreetNum);
        etCity = (EditText) view.findViewById(R.id.etVenCity);
        spnState = (Spinner) view.findViewById(R.id.spnState);
        etZip = (EditText) view.findViewById(R.id.etVenZip);
        etPhone = (EditText) view.findViewById(R.id.etVenPhone);

        signUpAct = (SignUpActivity) getActivity();

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (checkEmptyFields() & nameRegEx(etCity) & nameRegEx(etStreet) & validateNumber(etStreetNum) & validateNumber(etZip) & validatePhone(etPhone) ) {
                    signUpAct.vendor.setM_StreetName(etStreet.getText().toString());
                    signUpAct.vendor.setM_HouseNumber(etStreetNum.getText().toString());
                    signUpAct.vendor.setM_City(etCity.getText().toString());
                    signUpAct.vendor.setM_State(spnState.getSelectedItem().toString());
                    signUpAct.vendor.setM_ZipCode(etZip.getText().toString());
                    signUpAct.vendor.setM_PhoneNumber(etPhone.getText().toString());
                    VendorsContract vc = new VendorsContract(getActivity());
                    vc.addVendor(signUpAct.vendor); //create a record in the database
                    saveKeyData(signUpAct.vendor); //save the email and user type for later use
                    Toast.makeText(getContext(), "Account Created", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                  }
                break;
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorSignUpFragment()).commit();
                break;
        }
    }

    private boolean checkEmptyFields() {
        boolean pass = TextUtils.isEmpty(etCity.getText().toString()) ||
                TextUtils.isEmpty(etPhone.getText().toString()) ||
                TextUtils.isEmpty(etStreet.getText().toString()) ||
                TextUtils.isEmpty(etStreetNum.getText().toString()) ||
                TextUtils.isEmpty(etZip.getText().toString());

        if(pass){
            Snackbar.make(getView(), "                                 Please Fill All Fields" , Snackbar.LENGTH_SHORT).show();
        }

        return !pass;
    }

    private boolean validatePhone(EditText phoneNum) {
        Pattern p = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(phoneNum.getText().toString());
        boolean pass = m.find();

        if (!pass) {
            phoneNum.setError("Format ###-###-####");
        }

        return pass;
    }

    private boolean validateNumber(EditText houseNum) {
        Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(houseNum.getText().toString());
        boolean pass = m.find();

        if (!pass) {
            houseNum.setError("Invalid Input");
        }

        return pass;
    }

    private boolean nameRegEx(EditText editText) {
        boolean pass;
        Pattern p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(editText.getText().toString());
        pass = m.find();

        if(!pass){
            editText.setError("Invalid Input");
        }
        return pass;
    }

    public void saveKeyData(Vendor vendor) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserType", "Vendor");
        editor.putString("Email", vendor.getM_Email());
    }
}