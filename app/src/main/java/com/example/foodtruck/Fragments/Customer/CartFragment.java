package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CartFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private MyCartAdapter cMenuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Cart> cartList = new ArrayList<>();
    private TextView subTotal, totalTax, seeMenu, orderTotal,servicePrice;
    private Button btnPlaceOrder;
    private double tempSub, tempTax, tempOrderTotal;
    private double sCharge = 0.99;


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
        servicePrice = v.findViewById(R.id.service);
        servicePrice.setText(String.format(" $%.2f", sCharge));

        if (cartList != null) {
            double tempSub = calSubTotal(getCartOrders());
            subTotal.setText(String.format(" $%.2f", tempSub));

            double tempTax = calSalesTax(calSubTotal(getCartOrders()));
            totalTax.setText(String.format(" $%.2f", tempTax));

            double tempOrdertoal = tempSub+tempTax+sCharge;
            orderTotal.setText(String.format(" $%.2f", tempOrdertoal));
        }


        btnPlaceOrder = v.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentsContract paymentsContract = new PaymentsContract(getContext());
                ArrayList<Payment> payment =  paymentsContract.paymentsList(customer.getM_Id());
                if(payment != null){
                placeOrder(tempOrderTotal,tempTax,tempSub,1,1,customer.getM_Id());
                clearCart(customer.getM_Id());
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }
                else Toast.makeText(getContext(),"Please Add a Payment Method", Toast.LENGTH_SHORT).show();
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

    public void placeOrder(double tempOrdertoal, double tempTax, double tempSub,  long orderId, long paymentId, long customerId ) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        String todayDate = dateFormat.format(cal.getTime());
        InvoiceContract invoiceContract = new InvoiceContract(getContext());
        Invoice invoice = invoiceContract.createInvoice(todayDate,String.valueOf(tempOrdertoal),String.valueOf(sCharge),String.valueOf(tempTax),String.valueOf(tempSub),orderId,paymentId,customerId);
///if cart list empty dont place order
    }

    public void clearCart(long id){
       CheckOutContract mc = new CheckOutContract(getContext());
       if (mc.getEntireCart(id) != null){
       mc.clearTable(id);
       int size = cartList.size();
       cartList.clear();
       cMenuAdapter.submitList(cartList);
       cMenuAdapter.notifyDataSetChanged();}
    }

}

