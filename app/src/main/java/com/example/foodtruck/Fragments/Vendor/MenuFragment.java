package com.example.foodtruck.Fragments.Vendor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodtruck.Adapter.ItemsAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements ItemsAdapter.OnItemListener, View.OnClickListener {
    private RecyclerView itemsRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Item> itemList = new ArrayList<>();


    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        v.findViewById(R.id.AddItem).setOnClickListener(this);
        //accessing contracts
        VendorsContract vc = new VendorsContract(getContext());
        MenusContract mc = new MenusContract(getContext());
        ItemsContract ic = new ItemsContract(getContext());
        FoodTrucksContract fc = new FoodTrucksContract(getContext());
        //get vendor email from shared preference
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("Email", "");
        Log.i("Email", "Email, " + email);
        //get vendor id
        Vendor vendor = vc.getVendorIdByEmail(email);
        //get menu id from foodTruck
        Menu menu = mc.getMenuByFoodTruckId(fc.getFoodTruckByVendorId(vendor.getM_Id()).getM_ID());

        itemsRecycler = v.findViewById(R.id.ItemsRecyclerView);
        //get items from database
        itemList = ic.ItemsList(menu.getM_Id());

        itemsRecycler.setHasFixedSize(true);
        //use linear layout on cards
        mLayoutManager = new LinearLayoutManager(getContext());
        itemsRecycler.setLayoutManager(mLayoutManager);
        //specify adapter and pass in item list
        mAdapter = new ItemsAdapter(itemList, getContext(), this);
        itemsRecycler.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onItemClick(int position) {
        Item item = itemList.get(position);
    }

    @Override
    public void onClick(View view) {

    }
}