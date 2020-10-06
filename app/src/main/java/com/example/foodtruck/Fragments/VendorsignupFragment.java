package com.example.foodtruck.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.R;

public class VendorSignUpFragment extends Fragment implements View.OnClickListener {

    public SignUpActivity signupAct;
    EditText etFName;
    EditText etLName;
    EditText etEmail;
    EditText etPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendorsignup, container, false);
        view.findViewById(R.id.btnNext).setOnClickListener(this); //set onclick listener to next button
        view.findViewById(R.id.btnBack).setOnClickListener(this); //set onclick listener to back button

        //add reference to edit texts
        etFName = (EditText) view.findViewById(R.id.etVenFName);
        etLName = (EditText) view.findViewById(R.id.etVenLName);
        etEmail = (EditText) view.findViewById(R.id.etVenEmail);
        etPass = (EditText) view.findViewById(R.id.etVenPass);

        signupAct = (SignUpActivity) getActivity(); //reference to the activity holding the fragment, allows us to use the global object Customer we set

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext: //set data to customer object in SignUp activity
                signupAct.vendor.setM_FirstName(etFName.getText().toString());
                signupAct.vendor.setM_LastName(etLName.getText().toString());
                signupAct.vendor.setM_Email(etEmail.getText().toString());
                signupAct.vendor.setM_Password(etPass.getText().toString());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorSignUpFragment()).commit();
                break;
            case R.id.btnBack: //go back to the previous fragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
        }
    }
}