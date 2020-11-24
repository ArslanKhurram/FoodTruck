package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.Fragments.MapFragment;
import com.example.foodtruck.Fragments.SearchFragment;
import com.example.foodtruck.Fragments.Vendor.MenuFragment;
import com.example.foodtruck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerMainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_main, container, false);

        //reference to bottom navigation bar
        BottomNavigationView navigationView = v.findViewById(R.id.bottom_navigation);
        if (savedInstanceState == null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new FavoritesFragment()).commit();
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
            case "favorites":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new FavoritesFragment()).commit();
                break;
            case "search":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new SearchFragment()).commit();
                break;
            case "map":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MapFragment()).commit();
                break;
            case "account":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new CustomerAccountFragment()).commit();
                break;
            case "cart":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new CartFragment()).commit();
                break;
        }

        return true;
    }
}
