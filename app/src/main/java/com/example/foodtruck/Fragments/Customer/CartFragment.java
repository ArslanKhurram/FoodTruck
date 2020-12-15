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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.Adapter.MyCartAdapter;
import com.example.foodtruck.DataBase.CartOptionsContract;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OrderedItemOptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.CartOptions;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CartFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private MyCartAdapter cMenuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Cart> cartList = new ArrayList<>();
    private TextView subTotal, totalTax, seeMenu, orderTotal,servicePrice;
    private Button btnPlaceOrder;
    private double tempSub, tempTax, tempOrderTotal;
    private double sCharge = 0.99;
    private int orderNumber;


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
            calculateTotal();
        }


        btnPlaceOrder = v.findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentsContract paymentsContract = new PaymentsContract(getContext());
                ArrayList<Payment> payment =  paymentsContract.paymentsList(customer.getM_Id());
                if((payment != null)&&(tempSub != 0)){
                placeOrder(tempOrderTotal,tempTax,tempSub,payment.get(0).getM_ID(),customer.getM_Id());
                clearCart(customer.getM_Id());
                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
            }
                else if (payment == null) Toast.makeText(getContext(),"Please Add a Payment Method", Toast.LENGTH_SHORT).show();
                else if (tempSub == 0)Toast.makeText(getContext(),"Cart Cannot Be Empty", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Cart cart = cMenuAdapter.getItemAt(viewHolder.getAdapterPosition());

                CheckOutContract checkOutContract = new CheckOutContract(getContext());
                checkOutContract.removeCartById(cart.getM_ID());
                cMenuAdapter.submitList(getCartOrders());
                Toast.makeText(getContext(), "Item Removed", Toast.LENGTH_SHORT).show();



            }
        }).attachToRecyclerView(recyclerView);
                return v;
    }


    public void calculateTotal(){
        if (cartList == null) {
            tempSub = 0;
            tempTax = 0;
            tempOrderTotal = 0;
        } else{
        tempSub = calSubTotal(getCartOrders());
        subTotal.setText(String.format(" $%.2f", tempSub));

        tempTax = calSalesTax(calSubTotal(getCartOrders()));
        totalTax.setText(String.format(" $%.2f", tempTax));

        tempOrderTotal = tempSub+tempTax+sCharge;
        orderTotal.setText(String.format(" $%.2f", tempOrderTotal));
    }
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

    public void placeOrder(double tempOrderTotal, double tempTax, double tempSub, long paymentId, long customerId ) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        String todayDate = dateFormat.format(cal.getTime());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("foodTruck", Context.MODE_PRIVATE);

        //create order
        OrdersContract ordersContract = new OrdersContract(getContext());
        ordersContract.createOrder(getRandomNumberUsingNextInt(), todayDate, "Preparing", customerId, sharedPreferences.getLong("truck_Id", 0));

        Order order = ordersContract.getOrderByOrderNumber(String.valueOf(orderNumber));
        //Create invoice
        InvoiceContract invoiceContract = new InvoiceContract(getContext());
        invoiceContract.createInvoice(todayDate, String.format("%.2f", tempSub), String.format("%.2f", sCharge), String.format("%.2f", tempTax), String.format("%.2f", tempOrderTotal), order.getM_Id(), paymentId, customerId);


        CheckOutContract checkOutContract = new CheckOutContract(getContext());
        Cart cart = checkOutContract.getCart(customerId);

        addOrderItems(order);
        addOrderOptions(order);



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


    public String getRandomNumberUsingNextInt() {
        Random random = new Random();
        orderNumber = random.nextInt(9999 - 1000) + 1000;

        OrdersContract ordersContract = new OrdersContract(getContext());
        for (Order order : ordersContract.getAllOrders()) {
            if (orderNumber == Integer.parseInt(order.getM_OrderNumber()))
                getRandomNumberUsingNextInt();
        }
        return String.valueOf(orderNumber);
    }


    private void addOrderItems(Order order){
        for (Cart cart: cartList){
            OrderedItemsContract orderedItemsContract = new OrderedItemsContract(getContext());
             orderedItemsContract.addOrderedItem(cart.getM_Quantity(),cart.getM_Item().getM_Id(),order.getM_Id());
        }
    }

    private void addOrderOptions(Order order){
        ArrayList<CartOptions> cartOptions = new ArrayList<>();
        CartOptionsContract cartOptionsContract = new CartOptionsContract(getContext());
        for (Cart cart: cartList){

           CartOptions options = cartOptionsContract.getCartItemOptions(cart.getM_ID());
           if(options != null) {cartOptions.add(options);}
        }

        OrderedItemOptionsContract orderedItemOptionsContract = new OrderedItemOptionsContract(getContext());
        OrderedItemsContract orderedItemsContract = new OrderedItemsContract(getContext());

        for (CartOptions options: cartOptions){
            OrderedItem item = orderedItemsContract.getOrderedItemByOrderAndItemId(order.getM_Id(),options.getM_itemId().getM_Id());
            orderedItemOptionsContract.addOrderedItemOptions(options.getM_Option().getM_Id(),options.getM_itemId().getM_Id(),item.getM_id());
        }

    }
}



