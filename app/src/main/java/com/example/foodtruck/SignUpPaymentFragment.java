package com.example.foodtruck;

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
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Fragments.MainFragment;

public class SignUpPaymentFragment extends Fragment{
    @Nullable

    CustomersContract cc;


    public SignUpActivity SignUpA;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup_payment,container,false);
        //cast
        SignUpA = (SignUpActivity) getActivity();

        Button backBtn = v.findViewById(R.id.btnBack);
        Button subBtn = v.findViewById(R.id.btnSubmit);

        final Spinner payType = v.findViewById(R.id.spnState);
        final EditText fullName = v.findViewById(R.id.etFullName);
        final EditText ccv = v.findViewById(R.id.etCv);
        final EditText expDate = v.findViewById(R.id.etExDate);


        // Moves From singUpPaymentFragment to signUpAddFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpAddFragment()).commit();
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cc = new CustomersContract(getContext());
                cc.addCustomerByObject(SignUpA.customer);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

            }
        });


        return v;
    }

}
