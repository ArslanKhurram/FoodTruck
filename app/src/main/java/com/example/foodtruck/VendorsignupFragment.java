package com.example.foodtruck;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class VendorsignupFragment extends Fragment {
    public activity_signup signupAct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendorsignup,container,false);
        Button btnNext = (Button) getActivity().findViewById(R.id.btnNext);
        Button btnBack = (Button) getActivity().findViewById(R.id.btnBack);
        final EditText etFName = (EditText) view.findViewById(R.id.etVenFName);
        final EditText etLName = (EditText) view.findViewById(R.id.etVenLName);
        final EditText etEmail = (EditText) view.findViewById(R.id.etVenEmail);
        final EditText etPass = (EditText) view.findViewById(R.id.etVenPass);
        EditText etPassConfirm = (EditText) view.findViewById(R.id.etVenPassConfirm);

        btnBack.setEnabled(false);

        signupAct = (activity_signup) getActivity();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupAct.newVen.setM_FirstName(etFName.getText().toString());
                signupAct.newVen.setM_LastName(etLName.getText().toString());
                signupAct.newVen.setM_Email(etEmail.getText().toString());
                signupAct.newVen.setM_Password(etPass.getText().toString());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Signup_container, new VendorsignupFragment2()).commit();
            }
        });

        return view;
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