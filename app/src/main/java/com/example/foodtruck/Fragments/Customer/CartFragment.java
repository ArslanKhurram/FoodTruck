package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.Adapter.MyCartAdapter;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class CartFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private MyCartAdapter cMenuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Cart> cartList = new ArrayList<>();
    private TextView subTotal, totalTax, seeMenu, orderTotal;
    private Button btnPlaceOrder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");

        Bundle bundle = getArguments();

        CustomersContract customersContract = new CustomersContract(getContext());
        Customer customer = customersContract.getCustomerIdByEmail(email);


        cMenuAdapter = new MyCartAdapter(getContext(), this::onItemClick);
        cMenuAdapter.submitList(getCartOrders());
        seeMenu = v.findViewById(R.id.seeMenu);
        seeMenu.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); // LinearLayout for cards
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(cMenuAdapter);
        totalTax = v.findViewById(R.id.taxTxt2);
        subTotal = v.findViewById(R.id.totalTxt2);
        orderTotal = v.findViewById(R.id.orderTotal);

        if (cartList != null) {
            double tempSub = calSubTotal(getCartOrders());
            subTotal.setText(String.format(" $%.2f", tempSub));

            double tempTax = calSalesTax(calSubTotal(getCartOrders()));
            totalTax.setText(String.format(" $%.2f", tempTax));

            double tempOrdertoal = tempSub+tempTax;
            orderTotal.setText(String.format(" $%.2f", tempOrdertoal));
        }

        //capp view height
        // add tax


        btnPlaceOrder = v.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear cart when order place
                placeOrder();
                clearCart(customer.getM_Id());
                //transfer to order items and options display options //and total //delete delivery add text view totals as stuff get added in dynamically
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }
        });


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


    public double calSubTotal(ArrayList<Cart> cart) {
        double total = 0;

        for (int i = 0; i < cart.size(); i++) {

            total += Double.parseDouble(cart.get(i).getM_Item().getM_Price()) * Double.parseDouble(cart.get(i).getM_Quantity());
        }


        return total;
    }

    public double calSalesTax(double d) {
        double salesTax = .08875;
        double tax = d * salesTax;
        return tax;
    }

    public void placeOrder() {
        OrderedItemsContract orderedItemsContract = new OrderedItemsContract(getContext());
        OrderedItem order = orderedItemsContract.addOrderedItem("12", 123, 13);


    }

    public void clearCart(long id){
        CheckOutContract mc = new CheckOutContract(getContext());

        mc.clearTable(id);


    }

}

