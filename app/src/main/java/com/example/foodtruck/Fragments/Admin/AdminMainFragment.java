package com.example.foodtruck.Fragments.Admin;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.Fragments.Customer.CartFragment;
import com.example.foodtruck.Fragments.Customer.CustomerAccountFragment;
import com.example.foodtruck.Fragments.Customer.FavoritesFragment;
import com.example.foodtruck.Fragments.MapFragment;
import com.example.foodtruck.Fragments.SearchFragment;
import com.example.foodtruck.Fragments.Vendor.VendorAccountFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.R;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AdminMainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_main, container, false);

        //reference to bottom navigation bar
        BottomNavigationView navigationView = v.findViewById(R.id.bottom_navigation);
       if (savedInstanceState == null)  {
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container,new AdminUserDataFragment()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        navigationView.setOnNavigationItemSelectedListener(this); //set a listener on the navigation bar
        return v;
    }


    //method will handel pressed menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String selectedItem = item.getTitle().toString();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        switch (selectedItem) {
            case "Report":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new AdminUserDataFragment()).commit();
                break;
            case "search":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new SearchFragment()).commit();
                break;
            case "map":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MapFragment()).commit();
                break;
            case "account":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new VendorAccountFragment()).commit();
                break;
            case "cart":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new CartFragment()).commit();
                break;
        }

        return true;
    }
}//close AdminMainFragment
