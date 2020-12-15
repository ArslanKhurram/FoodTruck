package com.example.foodtruck.Fragments.Vendor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorSignUpFragment extends Fragment implements View.OnClickListener {

    public SignUpActivity signupAct;
    EditText etFName;
    EditText etLName;
    EditText etEmail;
    EditText etPass, etPassCopy;

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
        etPassCopy = (EditText) view.findViewById(R.id.etVenPassConfirm);

        signupAct = (SignUpActivity) getActivity(); //reference to the activity holding the fragment, allows us to use the global object Customer we set

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext: //set data to customer object in SignUp activity
                if (checkEmptyFields() & checkPasswordsMatch() & nameRegEx(etFName) & nameRegEx(etLName) & validateEmail(etEmail) & validatePassword(etPass)) {
                    signupAct.vendor.setM_FirstName(etFName.getText().toString());
                    signupAct.vendor.setM_LastName(etLName.getText().toString());
                    signupAct.vendor.setM_Email(etEmail.getText().toString());
                    signupAct.vendor.setM_Password(etPass.getText().toString());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorSignUpFragment2()).commit();
                }
                break;
            case R.id.btnBack: //go back to the previous fragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
        }
    }

    private boolean checkEmptyFields() {
        boolean pass = TextUtils.isEmpty(etFName.getText().toString()) ||
                TextUtils.isEmpty(etLName.getText().toString()) ||
                TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etPass.getText().toString()) ||
                TextUtils.isEmpty(etPassCopy.getText().toString());

        if(pass){
            Snackbar.make(getView(), "                                 Please Fill All Fields" , Snackbar.LENGTH_SHORT).show();
        }

        return !pass;
    }

    private boolean checkPasswordsMatch() {
        boolean pass = etPass.getText().toString().equals(etPassCopy.getText().toString());
        if (!pass)
            etPass.setError("Passwords Do Not Match");
        return pass;
    }

    private boolean nameRegEx(EditText editText) {
        boolean pass;
        Pattern p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(editText.getText().toString());
        pass = m.find();

        if(!pass){
            editText.setError("Invalid Input");
        }
        return pass;
    }

    private boolean validateEmail(EditText eMail) {
        Pattern p = Pattern.compile("[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(eMail.getText().toString());
        boolean pass = m.find();

        if (!pass) {
            eMail.setError("Invalid Format");
        }

        return pass;
    }

    private boolean validatePassword(EditText passWd) {
        Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{1,}", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(passWd.getText().toString());
        boolean pass = m.find();

        if (!pass) {
            passWd.setError("Password Does Not Meet Minimum Requirements" +
                    "\n\t-1 Special Character(!@#$%^&+=)" +
                    "\n\t-1 Number" +
                    "\n\t-1 Uppercase Letter" +
                    "\n\t-Minimum 8 Characters");
        }

        return pass;
    }
}