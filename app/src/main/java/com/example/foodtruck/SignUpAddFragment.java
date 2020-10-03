package com.example.foodtruck;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;



public class SignUpAddFragment extends Fragment{

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

     View v = inflater.inflate(R.layout.fragment_signup_address, container, false);

     Button nextBtn = v.findViewById(R.id.btnNext);
     Button backBtn = v.findViewById(R.id.btnBack);

     final Spinner spnState = v.findViewById(R.id.spnState);

        //Moves From signUpAddFragment to signUPPaymentFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
