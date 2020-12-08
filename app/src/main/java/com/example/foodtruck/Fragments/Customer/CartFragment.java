package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.CustomerMenuAdapter;
import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.Adapter.MyCartAdapter;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class CartFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private MyCartAdapter cMenuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Cart> cartList = new ArrayList<>();
    private EditText itemName, itemPrice;
    private TextView tv, itemNameDb, priceDb, seeMenu;
    private HorizontalScrollView hsv;
    private LayoutInflater dialogInflater;
    private Cart checkoutCart;
    View dV;
    private Spinner spnQnty;
    private CheckOutContract cart;
    private ArrayList<Cart> arrayCb = new ArrayList<>();
    private String selectedOptions = "";

    //hardcoded
    private Customer currentCustomer;
    private CustomersContract cC;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        cMenuAdapter = new MyCartAdapter(getContext(), this::onItemClick);
        cMenuAdapter.submitList(getCartOrders());
        seeMenu = v.findViewById(R.id.seeMenu);
        seeMenu.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); // LinearLayout for cards
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(cMenuAdapter);

        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.seeMenu) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("foodTruck", Context.MODE_PRIVATE);
            MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putLong("mKey", sharedPreferences.getLong("truck_Id", 0));
            MenusContract mc = new MenusContract(getContext());
            menuFrag.setArguments(bundle);
            if (bundle.getLong("truck_Id", 0) != 0)
                transaction.replace(R.id.mainFragment_container, menuFrag).commit();
        }
    }


    @Override
    public void onItemClick(int position) {

    }


    public ArrayList<Cart> getCartOrders() {
        SharedPreferences shp = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = shp.getString("Email", "");

        CheckOutContract mc = new CheckOutContract(getContext());
        CustomersContract cc = new CustomersContract(getContext());

        Customer cC = cc.getCustomerIdByEmail(email);

        if (cartList != null) {
            cartList = mc.getEntireCart(cC.getM_Id());
            return cartList;
        } else
            return null;
    }
}
