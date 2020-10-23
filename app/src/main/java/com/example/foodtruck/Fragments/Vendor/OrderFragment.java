package com.example.foodtruck.Fragments.Vendor;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.Adapter.OrderAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class OrderFragment extends Fragment implements OrderAdapter.OnOrderListener, View.OnClickListener {
    private RecyclerView pendingRecyclerView;
    private RecyclerView completedRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.Adapter recyclerAdapter2;
    private OrderAdapter orderAdapter;
    private OrderAdapter orderAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;
    private SharedPreferences sharedPref;
    private Order order;
    private ArrayList<Order> orderList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);


        pendingRecyclerView = v.findViewById(R.id.pendingRecycler);
        pendingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); //use linear layout on cards
        pendingRecyclerView.setLayoutManager(mLayoutManager);
        orderAdapter = new OrderAdapter(getContext(), this);
        orderAdapter.submitList(orderList);
        recyclerAdapter = orderAdapter;//specify adapter and pass in item list
        pendingRecyclerView.setAdapter(recyclerAdapter);

        completedRecyclerView = v.findViewById(R.id.completedRecycler);
        completedRecyclerView.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        completedRecyclerView.setLayoutManager(mLayoutManager2);
        orderAdapter2 = new OrderAdapter(getContext(), this);
        recyclerAdapter2 = orderAdapter2;
        completedRecyclerView.setAdapter(recyclerAdapter2);


        return v;
    }

    //get order list from order database
    public ArrayList<Order> getOrderList() {
        String email = sharedPref.getString("Email", "");

        VendorsContract vc = new VendorsContract(getContext());
        OrdersContract oc = new OrdersContract(getContext());

        Vendor vendor = vc.getVendorIdByEmail(email);
        orderList = oc.getOrdersList(vendor.getM_Id());
        return orderList;
    }

    @Override
    public void onClick(View v) {
        //addOrderDialog();
    }

    @Override
    public void onOrderClick(int position) {
     orderAdapter.getOrder(position);
    }
/*
    private void addOrderDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("OrderNum");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

 */
}
