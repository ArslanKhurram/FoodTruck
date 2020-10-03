package com.example.foodtruck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.VendorsContract;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class VendorsignupFragment2 extends Fragment {
    public activity_signup signupAct;

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
        signupAct = (activity_signup) getActivity();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupAct.newVen.setM_VendorName("NewVendor");
                signupAct.newVen.setM_StreetName(etStreet.getText().toString());
                signupAct.newVen.setM_HouseNumber(etStreetNum.getText().toString());
                signupAct.newVen.setM_City(etCity.getText().toString());
                signupAct.newVen.setM_State(etState.getText().toString());
                signupAct.newVen.setM_ZipCode(etZip.getText().toString());
                signupAct.newVen.setM_PhoneNumber(etPhone.getText().toString());
                signupAct.newVen.setM_DateAdded(" ");
                signupAct.newVen.setM_Category(" ");
                VendorsContract vc = new VendorsContract(getActivity());
                vc.addVendor(signupAct.newVen);
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


}