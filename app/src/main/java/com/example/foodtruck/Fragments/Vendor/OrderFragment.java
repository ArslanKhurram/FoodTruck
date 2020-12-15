package com.example.foodtruck.Fragments.Vendor;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.ItemsOrderedAdapter;
import com.example.foodtruck.Adapter.OptionsAdapter;
import com.example.foodtruck.Adapter.OrderAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;

import com.example.foodtruck.DataBase.OrderedItemOptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderFragment extends Fragment implements OrderAdapter.OnOrderListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
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
    private Spinner statusSpinner, foodtruckSpinner;
    private FoodTruck currentFoodTruck;
    private Pattern p;
    private Matcher m;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);


        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        //set spinner and generate data
        foodtruckSpinner = v.findViewById(R.id.spinnerFoodtruck);
        generateFoodTruckSpinnerData();

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

        foodtruckSpinner.setOnItemSelectedListener(this);

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
        FoodTruck foodTruck = (FoodTruck) foodtruckSpinner.getSelectedItem();

        if (foodTruck != null) {
            switch (status) {
                case "Preparing":
                    ArrayList<Order> orderPList = oc.getOrderListByStatus(foodTruck.getM_ID(), status);
                    if (orderPList != null)
                        pendingOrderList = orderPList;
                    return pendingOrderList;
                case "Completed":
                    ArrayList<Order> orderCList = oc.getOrderListByStatus(foodTruck.getM_ID(), status);
                    if (orderCList != null)
                        completedOrderList = orderCList;
                    completedOrderList = orderCList;
                    return completedOrderList;
            }
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
        if (v.getParent() == pendingRecyclerView) {
            order = pendingOrderAdapter.getOrderAt(position);
        } else {
            order = CompletedOrderAdapter.getOrderAt(position);
        }
        showOrderDialog(order);
    }

    //show order dialog
    private void showOrderDialog(Order order) {
        dialogInflater = getLayoutInflater();
        dv = dialogInflater.inflate(R.layout.dialog_show_order, null);
        //ListView
        listView = dv.findViewById(R.id.itemsInOrder);
        optionsList = dv.findViewById(R.id.ItemsOptions);


        //calls items from orderedItemsContract
        OrderedItemsContract oic = new OrderedItemsContract(getContext());
        mOrderedItems = oic.getOrderedItems(order.getM_Id());

        //submits list and sets adapter
        listAdapter = new ItemsOrderedAdapter(getContext(), mOrderedItems);
        listView.setAdapter(listAdapter);

        //show options
        listView.setClickable(true);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            OrderedItemOptionsContract oioc = new OrderedItemOptionsContract(getContext());
            OrderedItemsContract oic1 = new OrderedItemsContract(getContext());

            //get ordered item
            OrderedItem currentOrderedItem = listAdapter.getItem(i);

            //get options for current ordered item
            mOptions = oioc.getOrderedItemOptions(currentOrderedItem.getM_Item().getM_Id(), currentOrderedItem.getM_id());

            //set adapter
            optionsAdapter = new OptionsAdapter(getContext(), mOptions);
            optionsList.setAdapter(optionsAdapter);
        });

        customerName = dv.findViewById(R.id.tvCustomerName);
        statusSpinner = dv.findViewById(R.id.statusSpinner);


        int selection = ((order.getM_Status().equals("Preparing")) ? 0 : 1);
        //set customer name and status
        customerName.setText(order.getM_Customer().getM_FirstName() + order.getM_Customer().getM_LastName());
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
                if(selection == 0 && statusSpinner.getSelectedItem().equals("Completed")) { // If selection was initially preparing and has just completed after updating
                    // Notifications require an android version check before initializing notifications
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("Completed", "Completed Orders", NotificationManager.IMPORTANCE_HIGH);
                        NotificationManager manager = getContext().getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                        // Notification builder with the information on what becomes displayed
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "Completed")
                                .setSmallIcon(R.drawable.ic_food_truck_icon_21)
                                .setColor(Color.CYAN)
                                .setContentTitle("Order Completed")
                                .setContentText("Hope you're hungry, because your order is completed!")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("" + order.getM_Customer().getM_FirstName() + "! Your order from " + currentFoodTruck.toString() + " is completed and ready for pickup!"))
                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                        NotificationManagerCompat notifManager = NotificationManagerCompat.from(getContext());
                        notifManager.notify(1, builder.build());
                    }
                }
                alertDialog.cancel();
                Toast.makeText(getContext(), "Order Updated", Toast.LENGTH_LONG).show();
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
    private void generateFoodTruckSpinnerData() {
        VendorsContract vendorsContract = new VendorsContract(getContext());
        Vendor vendor = vendorsContract.getVendorIdByEmail(sharedPref.getString("Email", ""));
        FoodTrucksContract foodTrucksContract = new FoodTrucksContract(getContext());
        ArrayList<FoodTruck> foodTrucks = foodTrucksContract.FoodTruckList(vendor.getM_Id());

        if (foodTrucks != null) {
            ArrayAdapter<FoodTruck> spinnerAdapter = new ArrayAdapter<FoodTruck>(getContext(), android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapter.addAll(foodTrucks);
            foodtruckSpinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentFoodTruck = (FoodTruck) parent.getSelectedItem();
        Log.i("123: ", currentFoodTruck.getM_Name());
        pendingOrderAdapter.submitList(getOrderList("Preparing"));
        CompletedOrderAdapter.submitList(getOrderList("Completed"));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}