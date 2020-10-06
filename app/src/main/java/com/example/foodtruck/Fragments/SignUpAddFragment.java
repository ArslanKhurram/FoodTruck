package com.example.foodtruck.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.R;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpAddFragment extends Fragment {

    public SignUpActivity SignUpA;
    Pattern p;
    Matcher m;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup_address, container, false);

        SignUpA = (SignUpActivity) getActivity();

        final EditText stName = v.findViewById(R.id.etStreetName);
        final EditText houseNum = v.findViewById(R.id.etHouseNum);
        final EditText phoneNum = v.findViewById(R.id.etPhone);
        final EditText city = v.findViewById(R.id.etCity);
        final EditText zipCode = v.findViewById(R.id.etZip);


        Button nextBtn = v.findViewById(R.id.btnNext);
        Button backBtn = v.findViewById(R.id.btnBack);

        final Spinner spnState = v.findViewById(R.id.spnState);

        //Moves From signUpAddFragment to signUPPaymentFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateStreetName(stName) & validateHouse(houseNum) & validatePhone(phoneNum) & validateCity(city) & validateZip(zipCode)) {
                    SignUpA.customer.setM_StreetName(stName.getText().toString());
                    SignUpA.customer.setM_HouseNumber(houseNum.getText().toString());
                    SignUpA.customer.setM_PhoneNumber(phoneNum.getText().toString());
                    SignUpA.customer.setM_City(city.getText().toString());
                    SignUpA.customer.setM_ZipCode(zipCode.getText().toString());
                    SignUpA.customer.setM_State(spnState.getSelectedItem().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpPaymentFragment()).commit();
                }
            }
        });

        //Moves From signUPAddFragment to signUpFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpFragment()).commit();
            }
        });


        return v;
    }

    //Validation For Street Name
    private boolean validateStreetName(EditText stName) {
        p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(stName.getText().toString());
        boolean st = m.find();
        String strStreet = stName.getText().toString();

        if (TextUtils.isEmpty(strStreet)) {
            stName.setError("Can Not Be Empty ");
            return false;
        } else if (!st) {
            stName.setError("Only Letters Allowed");
            return false;
        }

        return true;
    }
    // Validation For House Number
    private boolean validateHouse(EditText houseNum) {
        p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(houseNum.getText().toString());
        boolean hn = m.find();
        String strHouse = houseNum.getText().toString();
        if (TextUtils.isEmpty(strHouse)) {
            houseNum.setError("Can Not Be Empty ");
            return false;
        } else if (!hn) {
            houseNum.setError("Only Numbers Allowed");
            return false;
        }

        return true;
    }
    // Validation For Phone Number
    private boolean validatePhone(EditText phoneNum) {
        p = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(phoneNum.getText().toString());
        boolean pn = m.find();
        String strPhone = phoneNum.getText().toString();

        if (TextUtils.isEmpty(strPhone)) {
            phoneNum.setError("Can Not Be Empty ");
            return false;
        } else if (!pn) {
            phoneNum.setError("Format ###-###-####");
            return false;
        }
        return true;
    }
    //Validation For City
    private boolean validateCity(EditText city) {
        p = Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(city.getText().toString());
        boolean cv= m.find();
        String strCity = city.getText().toString();

        if (TextUtils.isEmpty(strCity)) {
            city.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            city.setError("City Name Only");
            return false;
        }
        return true;
    }
    //Validation For Zip
    private boolean validateZip(EditText zipCode) {
        p = Pattern.compile("^\\d{5}(?:[-\\s]\\d{4})?$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(zipCode.getText().toString());
        boolean zp= m.find();
        String strZip = zipCode.getText().toString();

        if (TextUtils.isEmpty(strZip)) {
            zipCode.setError("Can Not Be Empty ");
            return false;
        } else if (!zp) {
            zipCode.setError("Not A Valid Zip Code");
            return false;
        }
        return true;
    }
}