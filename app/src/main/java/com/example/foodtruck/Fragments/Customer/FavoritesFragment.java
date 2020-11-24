package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MyFoodTruckAdapter;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FavoritesContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements MyFoodTruckAdapter.onFoodTruckCardListener {

    private RecyclerView recyclerView;
    private MyFoodTruckAdapter foodTruckAdapter;
    private RecyclerView.LayoutManager foodTruckLayoutManager;
    private TextView tv;
    private ArrayList<FoodTruck> foodTruckList = new ArrayList<>();
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        foodTruckAdapter = new MyFoodTruckAdapter(getContext(), this);
        tv = v.findViewById(R.id.noFoodTruckPrompt);

        //check is list is null(empty)
        if (getFoodTruckList() != null) {
            foodTruckAdapter.submitList(getFoodTruckList());
            tv.setVisibility(View.INVISIBLE);
        } else
            tv.setVisibility(View.VISIBLE);

        recyclerView = v.findViewById(R.id.foodtruck_recycler);
        recyclerView.setHasFixedSize(true);
        foodTruckLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(foodTruckLayoutManager);
        recyclerView.setAdapter(foodTruckAdapter);

        return v;

    }

    @Override
    public void onCardClick(int pos) {
        MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("mKey", foodTruckList.get(pos).getM_ID());
        menuFrag.setArguments(bundle);
        transaction.replace(R.id.mainFragment_container, menuFrag).commit();
    }

    private ArrayList<FoodTruck> getFoodTruckList() {
        String email = sharedPref.getString("Email", "");

        //get customer by SP email
        CustomersContract customersContract = new CustomersContract(getContext());
        Customer customer = customersContract.getCustomerIdByEmail(email);

        if (customer != null) {
            //set foodtruckList to saved Trucks
            FavoritesContract favoritesContract = new FavoritesContract(getContext());
            foodTruckList = favoritesContract.getSavedFoodTrucks(customer.getM_Id());
        }

        return foodTruckList;
    }
}
