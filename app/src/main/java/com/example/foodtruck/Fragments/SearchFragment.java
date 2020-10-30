package com.example.foodtruck.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MySearchAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;


public class SearchFragment extends Fragment implements MySearchAdapter.onCardClickListener, View.OnClickListener {

    MySearchAdapter searchAdapter = new MySearchAdapter(getContext(), this);
    ArrayList<FoodTruck> searchList = new ArrayList<>();
    SearchView searchText;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        searchList.clear();
        fillSearchList(searchList);
        recyclerView = v.findViewById(R.id.search_recycler);
        searchText = v.findViewById(R.id.search_view);
        MySearchAdapter searchAdapter = new MySearchAdapter(getContext(), this);

        searchAdapter.submitList(searchList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(searchAdapter);

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Conduct a search through entire list when search button is pressed (Case-sensitive for now)
            @Override
            public boolean onQueryTextSubmit(String query) {
                QuerySearch(searchList, query);
                searchAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update search query automatically when at least 3 letters have been entered (BAD PERFORMANCE)
                //if(newText.length() >= 3) {
                //    QuerySearch(searchList, newText);
                //    searchAdapter.submitList(searchList);
//
                //}

                //Workaround for when
                if(newText.isEmpty()) {
                    searchList.clear();
                    fillSearchList(searchList);
                    searchAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return v;
    }

    // TODO: Set card clicks to go to that Foodtruck's menu
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCardClick(int pos) {

    }

    private void fillSearchList(ArrayList<FoodTruck> l) {
        FoodTrucksContract fc = new FoodTrucksContract(getActivity());
        VendorsContract vc = new VendorsContract(getActivity());

        // Loop through each vendor for every food truck in database
        for(int c = 1; c <= vc.CountContracts(); c++) {
            fc.FoodTruckList(c).forEach((n) ->  l.add(n) );
        }
    }

    private void QuerySearch(ArrayList<FoodTruck> ftList, String query) {
        if(!query.isEmpty()) {
            ArrayList<FoodTruck> tmp = new ArrayList<>(ftList);
            ftList.clear();
            for (int i = 0; i < tmp.size(); i++) {
                if (tmp.get(i).getM_Name().contains(query)) {
                    ftList.add(tmp.get(i));
                }
            }
        }
    }



}
