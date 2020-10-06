package com.example.foodtruck.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodtruck.PaymentAdapter;
import com.example.foodtruck.Models.PaymentCard;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class PaymentsFragment extends Fragment implements PaymentAdapter.onPaymentCardListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter paymentAdapter;
    private RecyclerView.LayoutManager paymentLayoutManager;

    private ArrayList<PaymentCard> paymentsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_payments, container, false);

        paymentsList.add(new PaymentCard(R.drawable.ic_creditcard, "Testing name", "Testing number"));
        paymentsList.add(new PaymentCard(R.drawable.ic_creditcard, "Testing name 2", "Testing number 2"));

        recyclerView = v.findViewById(R.id.payments_recycler);
        recyclerView.setHasFixedSize(true);
        paymentLayoutManager = new LinearLayoutManager(getContext());
        paymentAdapter = new PaymentAdapter(paymentsList, getContext(), this);
        recyclerView.setLayoutManager(paymentLayoutManager);
        recyclerView.setAdapter(paymentAdapter);

        return v;
    }

    @Override
    public void onCardClick(int pos) {
        PaymentCard paymentCard = paymentsList.get(pos);
    }
}