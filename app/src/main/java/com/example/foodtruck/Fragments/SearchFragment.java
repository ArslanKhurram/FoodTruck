package com.example.foodtruck.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.Adapter.MyAccountAdapter;
import com.example.foodtruck.Adapter.MySearchAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.Customer.MenuCustomerViewFragment;
import com.example.foodtruck.Fragments.Vendor.MenuFragment;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class SearchFragment extends Fragment implements MySearchAdapter.onCardClickListener, View.OnClickListener {

    MySearchAdapter searchAdapter = new MySearchAdapter(getContext(), this);
    ArrayList<FoodTruck> searchList = new ArrayList<>();
    ArrayList<FoodTruck> resultsList = new ArrayList<>();
    SearchView searchView;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        fillSearchList(searchList);
        resetList(resultsList);
        searchAdapter.submitList(resultsList);

        recyclerView = v.findViewById(R.id.search_recycler);
        searchView = v.findViewById(R.id.search_view);

        // Search view's X button set by Android Studio
        ImageView btnClear = (ImageView) searchView.findViewById(R.id.search_close_btn);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(searchAdapter);

        searchView.setOnClickListener(v1 -> {
            searchView.setQuery("", false);
            resetList(resultsList);
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                resetList(resultsList);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Conduct a search through entire list when search button is pressed (Case-sensitive for now)
            @Override
            public boolean onQueryTextSubmit(String query) {
                QuerySearch(resultsList, query);
                searchAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update search query automatically when at least 3 letters have been entered (BAD PERFORMANCE)
               // if(newText.length() >= 3) {
               //     QuerySearch(resultsList, newText);
               //     searchAdapter.submitList(resultsList);
               // }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
    }

    // TODO: Set card clicks to go to that Foodtruck's menu
    @Override
    public void onCardClick(int pos) {
        GotoMenu(resultsList.get(pos));
    }

    private void fillSearchList(ArrayList<FoodTruck> ftList) {
        FoodTrucksContract fc = new FoodTrucksContract(getActivity());
        VendorsContract vc = new VendorsContract(getActivity());
        ftList.clear();
      // Loop through each vendor for every food truck in database
        for(int c = 1; c <= vc.CountContracts(); c++) {
            if (fc.FoodTruckList(c) != null)
                ftList.addAll(fc.FoodTruckList(c));
        }
    }

    private void resetList(ArrayList<FoodTruck> rList) {
        rList.clear();
        rList.addAll(searchList);
        searchAdapter.notifyDataSetChanged();
    }

    private void QuerySearch(ArrayList<FoodTruck> ftList, String query) {
            ftList.clear();
            for (int i = 0; i < searchList.size(); i++) {
                if (searchList.get(i).getM_Name().contains(query)) {
                    ftList.add(searchList.get(i));
                }
            }
        }

    private void GotoMenu(FoodTruck ft) {
        MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("mKey", ft.getM_ID());
        MenusContract mc = new MenusContract(getContext());
        menuFrag.setArguments(bundle);
        transaction.replace(R.id.mainFragment_container, menuFrag).commit();
    }



}
