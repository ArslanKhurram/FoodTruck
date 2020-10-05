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
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.R;

import java.util.Calendar;

public class SignUpPaymentFragment extends Fragment{
    @Nullable

    CustomersContract cc;
    PaymentsContract pc;

     SignUpActivity SignUpA;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup_payment,container,false);
        //cast
        SignUpA = (SignUpActivity) getActivity();

        Button backBtn = v.findViewById(R.id.btnBack);
        Button subBtn = v.findViewById(R.id.btnSubmit);

        final Spinner payType = v.findViewById(R.id.spnPaymentType);
        final EditText fullName = v.findViewById(R.id.etFullName);
        final EditText cardNumber = v.findViewById(R.id.etCardNum);
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
                //gets customer email and stores into cust1 to be used with pc, as a unique identifier
                Customer cust1;
                cust1 = cc.getCustomerIdByEmail(SignUpA.customer.getM_Email());

                //Add payments value to payment data base
                pc = new PaymentsContract(getContext());
                pc.createPayment(payType.getSelectedItem().toString(),fullName.getText().toString(),cardNumber.getText().toString(),expDate.getText().toString(),ccv.getText().toString(),Calendar.getInstance().getTime().toString(),cust1.getM_Id());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            }
        });
        return v;
    }

}
