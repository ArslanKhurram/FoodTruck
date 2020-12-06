package com.example.foodtruck.Fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Activities.SignUpActivity;
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
    AppCompatRadioButton radioName, radioCategory;
    String searchType = "Name";
    SearchView searchView;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = v.findViewById(R.id.search_recycler);
        searchView = v.findViewById(R.id.search_view);
        radioName = v.findViewById(R.id.rbName);
        radioCategory = v.findViewById(R.id.rbCategory);
        v.findViewById(R.id.rbName).setOnClickListener(this);
        v.findViewById(R.id.rbCategory).setOnClickListener(this);
        fillSearchList(searchList);
        resetList(resultsList);
        searchAdapter.submitList(resultsList);

        // Search view's X button set by Android Studio
        ImageView btnClear = (ImageView) searchView.findViewById(R.id.search_close_btn);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(searchAdapter);

        searchView.setOnClickListener(v1 -> {
            resetList(resultsList);
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    QuerySearch(resultsList, newText);
                    searchAdapter.submitList(resultsList);
                return false;
            }
        });

        return v;
    }


    @Override
    public void onClick(View v) {
        boolean isSelected = ((AppCompatRadioButton) v).isChecked();
        switch(v.getId()) {
            case  R.id.rbName:
                if(isSelected) {
                    radioName.setTextColor(Color.WHITE);
                    radioCategory.setTextColor(Color.BLACK);
                    searchType = "Name";
                    searchView.setQueryHint("Search Food Truck");
                    searchView.setQuery("", false);
                }
                if(!searchView.isEnabled())
                    searchView.setEnabled(true);
                break;
            case R.id.rbCategory:
                if(isSelected) {
                    radioCategory.setTextColor(Color.WHITE);
                    radioName.setTextColor(Color.BLACK);
                    searchType = "Category";
                    searchView.setQueryHint("Search Category");
                    searchView.setQuery("", false);
                }
                if(!searchView.isEnabled())
                    searchView.setEnabled(true);
                break;
        }
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
        searchView.setQuery("", false);
        searchAdapter.notifyDataSetChanged();
    }

    // Reduce to list of food trucks according to the name/category search input by user
    // Triggers whenever the search view is changed or user presses search button
    private void QuerySearch(ArrayList<FoodTruck> ftList, String query) {
            ftList.clear();
            switch(searchType) { // Duplicate code, should refactor soon
                case "Name":
                    for (int i = 0; i < searchList.size(); i++) {
                        if (searchList.get(i).getM_Name().toLowerCase().contains(query.toLowerCase())) {
                            ftList.add(searchList.get(i));
                        }
                        searchAdapter.notifyDataSetChanged();
                    }
                    break;
                case "Category":
                    for (int i = 0; i < searchList.size(); i++) {
                        if (searchList.get(i).getM_Category().toLowerCase().contains(query.toLowerCase())) {
                            ftList.add(searchList.get(i));
                        }
                        searchAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }

        private void GotoMenu(FoodTruck ft) {
        MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("mKey", ft.getM_ID());
        MenusContract mc = new MenusContract(getContext());
        menuFrag.setArguments(bundle);
        transaction.replace(R.id.mainFragment_container, menuFrag).addToBackStack("tag").commit();
    }



}
