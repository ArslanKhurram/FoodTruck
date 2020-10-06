package com.example.foodtruck.Fragments.Vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import androidx.fragment.app.Fragment;

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
                break;
            case R.id.btnBack:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorSignUpFragment()).commit();
                break;
        }
    }

    public void saveKeyData(Vendor vendor) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserType", "Vendor");
        editor.putString("Email", vendor.getM_Email());
    }
}