package com.example.foodtruck.Fragments.Customer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    SignUpActivity SignUpA;
    Pattern p;
    Matcher m;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        SignUpA = (SignUpActivity) getActivity();

        Button nextBtn = v.findViewById(R.id.btnNext);
        Button backBtn = v.findViewById(R.id.btnBack);
        final EditText firstN = v.findViewById(R.id.etFirstName);
        final EditText lastN = v.findViewById(R.id.etLastName);
        final EditText eMail = v.findViewById(R.id.etEmailAdd);
        final EditText passWd = v.findViewById(R.id.etNewPassword);
        final EditText confirmPassWd = v.findViewById(R.id.testNewPassword);


        //Moves From signUpFragment to signUPAddFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateNames(firstN) & validateNames(lastN) & validateEmail(eMail) & validatePassword(passWd, confirmPassWd)) {
                    SignUpA.customer.setM_FirstName(firstN.getText().toString());
                    SignUpA.customer.setM_LastName(lastN.getText().toString());
                    SignUpA.customer.setM_Email(eMail.getText().toString());
                    SignUpA.customer.setM_Password(passWd.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpAddFragment()).commit();
                }
            }
        });

        //Moves From signUPAddFragment to signUpFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            }
        });
        return v;
    }

    //regex validation  for first and last name
    private boolean validateNames(EditText firstN) {
        p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(firstN.getText().toString());
        boolean b = m.find();
        String strName = firstN.getText().toString().trim();

        if (TextUtils.isEmpty(strName)) {
            firstN.setError("Can Not Be Empty ");
            return false;
        } else if (!b) {
            firstN.setError("Numbers Not Allowed");
            return false;
        }
        return true;
    }

    //regex validation email
    private boolean validateEmail(EditText eMail) {
        p = Pattern.compile("[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        m = p.matcher(eMail.getText().toString());
        boolean e = m.find();
        String strEmail = eMail.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)) {
            eMail.setError("Can Not Be Empty");
            return false;
        } else if (!e) {
            eMail.setError("Not a Valid Email ");
            return false;
        }
        return true;
    }

    //regex validation password
    /*a digit must occur at least once
     * a lower case letter must be used once
     * an upper case letter must be used once
     * no spaces are allowed
     * at least 8 character*/
    private boolean validatePassword(EditText passWd, EditText confirmPass) {
        p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{1,}", Pattern.CASE_INSENSITIVE);
        m = p.matcher(passWd.getText().toString());
        boolean pw = m.find();
        String strPass = passWd.getText().toString();
        String strConfirmPass = confirmPass.getText().toString();

        if (TextUtils.isEmpty(strPass)) {
            passWd.setError("Can Not Be Empty ");
            return false;
        } else if (!pw) {
            passWd.setError("Password Does Not Meet Minimum Requirements");
            return false;
        } else if (!strPass.equals(strConfirmPass)) {
            confirmPass.setError("Passwords must match");
            return false;
        }

        return true;
    }
}