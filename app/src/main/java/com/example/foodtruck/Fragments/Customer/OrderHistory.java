package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodtruck.Adapter.InvoiceAdapter;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class OrderHistory extends Fragment implements InvoiceAdapter.OnInvoiceListener, View.OnClickListener {
    private RecyclerView invoiceRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private InvoiceAdapter invoiceAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutInflater dialogInflater;
    private SharedPreferences sharedPref;
    private ArrayList<Invoice> InvoiceList = new ArrayList<>();

    View dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customer_order_histroy, container, false);
        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        //Invoice RecyclerView
        invoiceRecyclerView = v.findViewById(R.id.rvOrderHistory);
        mLayoutManager = new LinearLayoutManager(getContext());
        invoiceRecyclerView.setLayoutManager(mLayoutManager);
        invoiceAdapter = new InvoiceAdapter(getContext(), this);
        invoiceAdapter.submitList(getInvoiceList());
        recyclerAdapter = invoiceAdapter;
        invoiceRecyclerView.setAdapter(recyclerAdapter);

        return v;
    }
    public ArrayList<Invoice> getInvoiceList() {

        String email = sharedPref.getString("Email", "");
        CustomersContract cc = new CustomersContract(getContext());
        OrdersContract oc = new OrdersContract(getContext());
        InvoiceContract ic = new InvoiceContract(getContext());


        return null;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onInvoiceClick(View view, int position) {
        showInvoiceDialog();
    }

    private void showInvoiceDialog()
    {
        
    }
}