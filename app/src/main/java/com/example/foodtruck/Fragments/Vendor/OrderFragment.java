package com.example.foodtruck.Fragments.Vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.OrderAdapter;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class OrderFragment extends Fragment implements OrderAdapter.OnOrderListener, View.OnClickListener {
    private RecyclerView pendingRecyclerView;
    private RecyclerView completedRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.Adapter recyclerAdapter2;
    private OrderAdapter pendingOrderAdapter;
    private OrderAdapter CompletedOrderAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;
    private SharedPreferences sharedPref;
    private Order order;
    private ArrayList<Order> pendingOrderList = new ArrayList<>();
    private ArrayList<Order> completedOrderList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);


        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        //first recycler
        pendingRecyclerView = v.findViewById(R.id.pendingRecycler);
        pendingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); //use linear layout on cards
        pendingRecyclerView.setLayoutManager(mLayoutManager);
        pendingOrderAdapter = new OrderAdapter(getContext(), this);
        pendingOrderAdapter.submitList(getOrderList("Preparing"));
        recyclerAdapter = pendingOrderAdapter;//specify adapter and pass in item list
        pendingRecyclerView.setAdapter(recyclerAdapter);

        //second recycler
        completedRecyclerView = v.findViewById(R.id.completedRecycler);
        completedRecyclerView.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        completedRecyclerView.setLayoutManager(mLayoutManager2);
        CompletedOrderAdapter = new OrderAdapter(getContext(), this);
        CompletedOrderAdapter.submitList(getOrderList("Completed"));
        recyclerAdapter2 = CompletedOrderAdapter;
        completedRecyclerView.setAdapter(recyclerAdapter2);


        return v;
    }
    //need two arrayList, orderPending and orderComplete
    //get order list from order database
    public ArrayList<Order> getOrderList(String status) {

        String email = sharedPref.getString("Email", "");

        VendorsContract vc = new VendorsContract(getContext());
        OrdersContract oc = new OrdersContract(getContext());

        Vendor vendor = vc.getVendorIdByEmail(email);
        Order order = oc.getOrderById(vendor.getM_Id());

       switch (status)
       {
           case "Preparing":
               pendingOrderList = oc.getOrderListByStatus(vendor.getM_Id(), status); //vendor = 3  status = preparing
               return pendingOrderList;
           case "Completed":
               completedOrderList = oc.getOrderListByStatus(vendor.getM_Id(), status);
               return completedOrderList;
       }
       return null;
    }

    @Override
    public void onClick(View v) {
        //showOrderDialog();
    }

    @Override
    public void onOrderClick(int position) {
        //pendingOrderAdapter.getOrder(position);
    }
/*
    private void showOrderDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("OrderNum");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

 */
}
