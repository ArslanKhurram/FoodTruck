package com.example.foodtruck.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtruck.Activities.ManualSignUpActivity;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class VendorsignupFragment2 extends Fragment {
    public ManualSignUpActivity signupAct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendorsignupstep2,container,false);
        Button btnNext = (Button) getActivity().findViewById(R.id.btnNext);
        Button btnBack = (Button) getActivity().findViewById(R.id.btnBack);
        TextView txtSkip = (TextView) getActivity().findViewById(R.id.txtSkip);
        final EditText etStreet = (EditText) view.findViewById(R.id.etVenStreetName);
        final EditText etStreetNum = (EditText) view.findViewById(R.id.etVenStreetNum);
        final EditText etCity = (EditText) view.findViewById(R.id.etVenCity);
        final EditText etState = (EditText) view.findViewById(R.id.etVenState);
        final EditText etZip = (EditText) view.findViewById(R.id.etVenZip);
        final EditText etPhone = (EditText) view.findViewById(R.id.etVenPhone);


        btnBack.setEnabled(true);
        signupAct = (ManualSignUpActivity) getActivity();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupAct.newVen.setM_StreetName(etStreet.getText().toString());
                signupAct.newVen.setM_HouseNumber(etStreetNum.getText().toString());
                signupAct.newVen.setM_City(etCity.getText().toString());
                signupAct.newVen.setM_State(etState.getText().toString());
                signupAct.newVen.setM_ZipCode(etZip.getText().toString());
                signupAct.newVen.setM_PhoneNumber(etPhone.getText().toString());
                VendorsContract vc = new VendorsContract(getActivity());
                vc.addVendor(signupAct.newVen);
                Toast.makeText(getContext(), "Vendor account successfully created!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.Signup_container, new VendorsignupFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private boolean validateStreet(EditText etStreet) {
        String strStreet = etStreet.getText().toString().trim();
        if (strStreet.isEmpty()) {
            etStreet.setError("Street field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strStreet).matches()) {
            etStreet.setError("You must enter a valid email address");
            return false;
        }
        return true;
    }


}