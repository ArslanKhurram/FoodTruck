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
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.R;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPaymentFragment extends Fragment{
    @Nullable

    CustomersContract cc;
    PaymentsContract pc;
    SignUpActivity SignUpA;
    Pattern p;
    Matcher m;
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
        payType.setDropDownHorizontalOffset(50);
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

                if(validateNames(fullName) && (validateCardNumber(cardNumber)  &&  validateExp(expDate)  && validateCcv(ccv))) {
                    cc = new CustomersContract(getContext());
                    cc.addCustomerByObject(SignUpA.customer);
                    //gets customer email and stores into cust1 to be used with pc, as a unique identifier
                    Customer cust1;
                    cust1 = cc.getCustomerIdByEmail(SignUpA.customer.getM_Email());

                    //Add payments value to payment data base
                    pc = new PaymentsContract(getContext());
                    pc.createPayment(payType.getSelectedItem().toString(), fullName.getText().toString(), cardNumber.getText().toString(), expDate.getText().toString(), ccv.getText().toString(), Calendar.getInstance().getTime().toString(), cust1.getM_Id());

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                }
            }
        });
        return v;
    }

    //regex validation  for full Name
    private boolean validateNames(EditText fullName) {
        p = Pattern.compile("^([a-zA-Z0-9]+|[a-zA-Z0-9]+\\s[a-zA-Z0-9]+|[a-zA-Z0-9]+\\s[a-zA-Z0-9]{3,}\\s[a-zA-Z0-9]+)$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(fullName.getText().toString());
        boolean fn = m.find();
        String strFullName = fullName.getText().toString().trim();

        if (TextUtils.isEmpty(strFullName)) {
            fullName.setError("Can Not Be Empty ");
            return false;
        } else if (!fn) {
            fullName.setError("Numbers Not Allowed");
            return false;
        }
        return true;
    }

    //regex validation card Number
    private boolean validateCardNumber(EditText cardNumber) {
        p = Pattern.compile("[0-9]{12}(?:[0-9]{3})?$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(cardNumber.getText().toString());
        boolean cn = m.find();
        String strCardNum = cardNumber.getText().toString().trim();

        if (TextUtils.isEmpty(strCardNum)) {
            cardNumber.setError("Can Not Be Empty ");
            return false;
        } else if (!cn) {
            cardNumber.setError("Please Enter Your 13 Digit Number");
            return false;
        }
        return true;
    }

    //regex validation CCV
    private boolean validateCcv(EditText ccv) {
        p = Pattern.compile("^[0-9]{3,4}$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(ccv.getText().toString());
        boolean cv = m.find();
        String strCCV = ccv.getText().toString().trim();

        if (TextUtils.isEmpty(strCCV)) {
            ccv.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            ccv.setError("Not A Valid CCV ");
            return false;
        }
        return true;
    }

    //regex validation EXP
    private boolean validateExp(EditText expDate) {
        p = Pattern.compile("^^(((0)[0-9])|((1)[0-2]))(\\/)\\d{2}$", Pattern.CASE_INSENSITIVE);
        m = p.matcher(expDate.getText().toString());
        boolean exp = m.find();
        String strExp = expDate.getText().toString().trim();

        if (TextUtils.isEmpty(strExp)) {
            expDate.setError("Can Not Be Empty ");
            return false;
        } else if (!exp) {
            expDate.setError("Format MM/YY ");
            return false;
        }
        return true;
    }

}
