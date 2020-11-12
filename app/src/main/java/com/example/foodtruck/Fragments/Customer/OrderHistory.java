package com.example.foodtruck.Fragments.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
;

import androidx.fragment.app.Fragment;


import com.example.foodtruck.R;

public class OrderHistory extends Fragment implements View.OnClickListener {

    private LayoutInflater dialogInflater;
    View dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cust_order_histroy, container, false);

        return v;
    }


    @Override
    public void onClick(View v) {

    }



}
