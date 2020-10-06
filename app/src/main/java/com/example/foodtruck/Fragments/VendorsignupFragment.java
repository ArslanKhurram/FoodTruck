package com.example.foodtruck.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodtruck.Activities.ManualSignUpActivity;
import com.example.foodtruck.R;

import java.util.regex.Pattern;

public class VendorsignupFragment extends Fragment {
    public ManualSignUpActivity signupAct;
    public Pattern FIRST_LAST_NAME = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendorsignup,container,false);
        Button btnNext = (Button) getActivity().findViewById(R.id.btnNext);
        Button btnBack = (Button) getActivity().findViewById(R.id.btnBack);
        final EditText etFName = (EditText) view.findViewById(R.id.etVenFName);
        final EditText etLName = (EditText) view.findViewById(R.id.etVenLName);
        final EditText etEmail = (EditText) view.findViewById(R.id.etVenEmail);
        final EditText etPass = (EditText) view.findViewById(R.id.etVenPass);
        final EditText etPassConfirm = (EditText) view.findViewById(R.id.etVenPassConfirm);

        btnBack.setEnabled(false);

        signupAct = (ManualSignUpActivity) getActivity();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateNames(etFName) & validateNames(etLName) & validateEmail(etEmail) & validatePassword(etPass, etPassConfirm)) {
                    signupAct.newVen.setM_FirstName(etFName.getText().toString());
                    signupAct.newVen.setM_LastName(etLName.getText().toString());
                    signupAct.newVen.setM_Email(etEmail.getText().toString());
                    signupAct.newVen.setM_Password(etPass.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Signup_container, new VendorsignupFragment2()).commit();
                }
            }
        });

        return view;
    }

    private boolean validateNames(EditText etName){
        String strName = etName.getText().toString().trim();
        if(strName.isEmpty()){
            etName.setError("Name can not be empty");
            return false;
        } else if (!FIRST_LAST_NAME.matcher(strName).matches()) {
            etName.setError("Name can only contain letters");
            return false;
        }
        return true;
    }


    private boolean validateEmail(EditText etEmail){
        String strEmail = etEmail.getText().toString().trim();
        if (strEmail.isEmpty()) {
            etEmail.setError("Email field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            etEmail.setError("You must enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean validatePassword(EditText etPass, EditText etConfirmPass) {
        String strPass = etPass.getText().toString().trim(), strConfirmPass = etConfirmPass.getText().toString().trim();
        if (strPass.isEmpty()) {
            etPass.setError("Please enter a password");
            return false;
        } else if (!strPass.equals(strConfirmPass)) {
            etConfirmPass.setError("Passwords must match");
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Save input state when going to next steps
        // Save fragment state (if the user presses back on the next step and comes back to this step
        // Each text field will continue where you left off

    }



}