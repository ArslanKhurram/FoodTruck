package com.example.foodtruck.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.R;
import com.example.foodtruck.SignUpFragment;


public class SignUpAddFragment extends Fragment{

    public SignUpActivity SignUpA;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

     View v = inflater.inflate(R.layout.fragment_signup_address, container, false);

     SignUpA = (SignUpActivity) getActivity();

     final EditText stName = v.findViewById(R.id.etStreetName);
     final EditText houseNum = v.findViewById(R.id.etHouseNum);
     final EditText phoneNum = v.findViewById(R.id.etPhone);
     final EditText city = v.findViewById(R.id.etCity);
     final EditText zipCode = v.findViewById(R.id.etZip);
     final Spinner spnSate = v.findViewById(R.id.spnState);

     Button nextBtn = v.findViewById(R.id.btnNext);
     Button backBtn = v.findViewById(R.id.btnBack);

     final Spinner spnState = v.findViewById(R.id.spnState);

        //Moves From signUpAddFragment to signUPPaymentFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpA.customer.setM_StreetName(stName.getText().toString());
                SignUpA.customer.setM_HouseNumber(houseNum.getText().toString());
                SignUpA.customer.setM_PhoneNumber(phoneNum.getText().toString());
                SignUpA.customer.setM_City(city.getText().toString());
                SignUpA.customer.setM_ZipCode(zipCode.getText().toString());
                SignUpA.customer.setM_State(spnState.getSelectedItem().toString());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignUpPaymentFragment()).commit();

            } });

        //Moves From signUPAddFragment to signUpFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignUpFragment()).commit();
            }
        });





        return v ;
    }




}
