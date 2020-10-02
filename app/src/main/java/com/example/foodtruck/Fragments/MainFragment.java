package com.example.foodtruck.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //reference to bottom navigation bar
        BottomNavigationView navigationView = v.findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this); //set a listener on the navigation bar

        return v;
    }


    //method will handel pressed menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String selectedItem = item.getTitle().toString();
        //Log.d("Menu","This is the Selected Item: " + selectedItem);

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new AccountFragment()).commit();
                break;
            case "cart":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new CartFragment()).commit();
                break;
        }

        return true;
    }
}
