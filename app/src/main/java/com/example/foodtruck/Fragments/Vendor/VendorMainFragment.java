package com.example.foodtruck.Fragments.Vendor;

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

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.Fragments.Admin.AdminUserDataFragment;
import com.example.foodtruck.Fragments.Customer.FavoritesFragment;
import com.example.foodtruck.Fragments.MapFragment;
import com.example.foodtruck.Fragments.SearchFragment;
import com.example.foodtruck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VendorMainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vendor_main, container, false);
        SignUpActivity.currentFragment = "Fragment_VendorMain";
        SignUpActivity.currentFragmentView = v;

        //reference to bottom navigation bar
        BottomNavigationView navigationView = v.findViewById(R.id.bottom_navigation);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MenuFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setOnNavigationItemSelectedListener(this); //set a listener on the navigation bar

        //navigationView.
        return v;
    }


    //method will handel pressed menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String selectedItem = item.getTitle().toString();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        switch (selectedItem) {
            case "menu":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MenuFragment()).commitNow();
                break;
            case "search":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new SearchFragment()).commit();
                break;
            case "map":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MapFragment()).commit();
                break;
            case "account":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new VendorAccountFragment()).commit();
                break;
            case "order":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new OrderFragment()).commit();
                break;
        }

        return true;
    }
}
