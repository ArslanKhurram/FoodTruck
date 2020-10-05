package com.example.foodtruck;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodtruck.Activities.MainActivity;
import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Fragments.SignUpAddFragment;


public class SignUpFragment extends Fragment {

    SignUpActivity SignUpA;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container,  false);

        SignUpA = (SignUpActivity) getActivity();

        Button nextBtn = v.findViewById(R.id.btnNext);
        Button backBtn = v.findViewById(R.id.btnBack);
        final EditText firstN = v.findViewById(R.id.etFirstName);
        final EditText lastN = v.findViewById(R.id.etLastName);
        final EditText eMail = v.findViewById(R.id.etEmailAdd);
        final EditText passWd = v.findViewById(R.id.etNewPassword);


        //Moves From signUpFragment to signUPAddFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpA.customer.setM_FirstName(firstN.getText().toString());
                SignUpA.customer.setM_LastName(lastN.getText().toString());
                SignUpA.customer.setM_Email(eMail.getText().toString());
                SignUpA.customer.setM_Password(passWd.getText().toString());


                //try

                Bundle bundle = new Bundle();
                bundle.putString("Try", eMail.getText().toString());



                //try
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignUpAddFragment()).commit();

            } });

        //Moves From signUPAddFragment to signUpFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
            }
        });




        return v;
    }
}