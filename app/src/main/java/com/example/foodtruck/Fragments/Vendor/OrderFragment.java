package com.example.foodtruck.Fragments.Vendor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.ItemsOrderedAdapter;
import com.example.foodtruck.Adapter.OptionsAdapter;
import com.example.foodtruck.Adapter.OrderAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private LayoutInflater dialogInflater;
    private ArrayList<Order> pendingOrderList = new ArrayList<>();
    private ArrayList<Order> completedOrderList = new ArrayList<>();
    private ListView listView;
    private ArrayList<OrderedItem> mOrderedItems;
    private ItemsOrderedAdapter listAdapter;
    private OptionsAdapter optionsAdapter;
    private ListView optionsList;
    private ArrayList<Option> mOptions;
    View dv;
    private TextView customerName;
    private Spinner statusSpinner;
    private Pattern p;
    private Matcher m;

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
        FoodTrucksContract fc = new FoodTrucksContract(getContext());
        FoodTruck foodTruck = fc.getFoodTruckByVendorId(vendor.getM_Id());

        switch (status) {
            case "Preparing":
                pendingOrderList = oc.getOrderListByStatus(foodTruck.getM_ID(), status);
                return pendingOrderList;
            case "Completed":
                completedOrderList = oc.getOrderListByStatus(foodTruck.getM_ID(), status);
                return completedOrderList;
        }
       return null;
    }

    @Override
    public void onClick(View v) {
    }

    //call showOrderDialog based on what recyclerView is clicked
    @Override
    public void onOrderClick(View v, int position) {
        Order order;
        if(v.getParent() == pendingRecyclerView)
        {
            order = pendingOrderAdapter.getOrderAt(position);
        }
        else
        {
            order = CompletedOrderAdapter.getOrderAt(position);
        }
        showOrderDialog(order);
    }

    //show order dialog
    private void showOrderDialog(Order order)
    {
        dialogInflater = getLayoutInflater();
        dv = dialogInflater.inflate(R.layout.dialog_show_order, null);
        //ListView
        listView = dv.findViewById(R.id.itemsInOrder);
        optionsList = dv.findViewById(R.id.ItemsOptions);


        //calls items from orderedItemsContract
        OrderedItemsContract oic = new OrderedItemsContract(getContext());
        mOrderedItems = oic.getOrderedItems(order.getM_Id());

        //submits list and sets adapter
        listAdapter =  new ItemsOrderedAdapter(getContext(), mOrderedItems);
        listView.setAdapter(listAdapter);

        //show options
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OptionsContract oc = new OptionsContract(getContext());
                mOptions = oc.getOptionsListByItemID(order.getM_Id());
                optionsAdapter = new OptionsAdapter(getContext(), mOptions);
                optionsList.setAdapter(optionsAdapter);
            }
        });

        customerName = dv.findViewById(R.id.tvCustomerName);
        statusSpinner = dv.findViewById(R.id.statusSpinner);


        int selection = ((order.getM_Status().equals("Preparing")) ? 0 : 1);
        //set customer name and status
        customerName.setText(order.getM_Customer().getM_FirstName()+order.getM_Customer().getM_LastName());
        statusSpinner.setSelection(selection);

        //update and cancel buttons
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .show();

        //sets what happens if status is changed
        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(v -> {
            if (updateOrderInDatabase(order)) {
                pendingOrderAdapter.submitList(getOrderList("Preparing"));
                CompletedOrderAdapter.submitList(getOrderList("Completed"));
                alertDialog.cancel();
                Toast.makeText(getContext(),"Order Updated", Toast.LENGTH_LONG).show();
            }
        });
    }
    //method to update order
    private boolean updateOrderInDatabase(Order order) {
        //references to all Dialog views
        customerName = dv.findViewById(R.id.tvCustomerName);
        statusSpinner = dv.findViewById(R.id.statusSpinner);

        if (validateCustomerName(customerName)) {
            OrdersContract oc = new OrdersContract(getContext());
            oc.updateOrder(order.getM_Id(), statusSpinner.getSelectedItem().toString());
            return true;
        }
        return false;
    }

    //method to validate customer
    private boolean validateCustomerName(TextView customerName) {
        p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(customerName.getText().toString());
        boolean cv = m.find();
        String name = customerName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            customerName.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            customerName.setError("Invalid Entry (Only Letters)");
            return false;
        }

        return true;
    }

}
