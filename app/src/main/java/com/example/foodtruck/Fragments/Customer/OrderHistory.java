package com.example.foodtruck.Fragments.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodtruck.Adapter.InvoiceAdapter;
import com.example.foodtruck.Adapter.ItemsOrderedAdapter;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class OrderHistory extends Fragment implements InvoiceAdapter.OnInvoiceListener, View.OnClickListener {
    private RecyclerView invoiceRecyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private InvoiceAdapter invoiceAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayoutInflater dialogInflater;
    private SharedPreferences sharedPref;
    private ArrayList<Invoice> InvoiceList = new ArrayList<>();
    private TextView foodtruck, date, totalAmount, orderAmount, serviceCharge, taxAmount, cardName, cardNumber;
    private ListView listView;
    private ArrayList<OrderedItem> mOrderedItems;
    private ItemsOrderedAdapter listAdapter;
    View dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customer_order_histroy, container, false);
        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        //Invoice RecyclerView
        invoiceRecyclerView = v.findViewById(R.id.rvOrderHistory);
        mLayoutManager = new LinearLayoutManager(getContext());
        invoiceRecyclerView.setLayoutManager(mLayoutManager);
        invoiceAdapter = new InvoiceAdapter(getContext(), this);
        invoiceAdapter.submitList(getInvoiceList());
        recyclerAdapter = invoiceAdapter;
        invoiceRecyclerView.setAdapter(recyclerAdapter);

        return v;
    }

    public ArrayList<Invoice> getInvoiceList() {

        String email = sharedPref.getString("Email", "");
        CustomersContract cc = new CustomersContract(getContext());
        Customer customer = cc.getCustomerIdByEmail(email);
        InvoiceContract ic = new InvoiceContract(getContext());

        InvoiceList = ic.getInvoiceListByCustomerId(customer.getM_Id());
        Log.i("123", String.valueOf(InvoiceList.size()));
        return InvoiceList;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onInvoiceClick(View view, int position) {
        Invoice invoice;
        invoice = invoiceAdapter.getInvoiceAt(position);
        showInvoiceDialog(invoice);
    }

    private void showInvoiceDialog(Invoice invoice) {
        dialogInflater = getLayoutInflater();
        dv = dialogInflater.inflate(R.layout.dialog_invoice, null);

        foodtruck = dv.findViewById(R.id.tvFoodTruck);
        date = dv.findViewById(R.id.tvDate);
        totalAmount = dv.findViewById(R.id.tvTotalAmount);
        orderAmount = dv.findViewById(R.id.tvOrderTotal);
        serviceCharge = dv.findViewById(R.id.tvServiceCharge);
        taxAmount = dv.findViewById(R.id.tvTax);
        cardName = dv.findViewById(R.id.tvCardName);
        cardNumber = dv.findViewById(R.id.tvCardNumber);

        foodtruck.setText(invoice.getM_Order().getM_FoodTruck().getM_Name());
        date.setText(invoice.getM_InvoiceDate());
        totalAmount.setText(invoice.getM_TotalInvoiceAmount());
        orderAmount.setText(invoice.getM_Total());
        serviceCharge.setText(invoice.getM_ServiceCharge());
        taxAmount.setText(invoice.getM_TaxAmount());
        cardName.setText(invoice.getM_Payment().getM_NameOnCard());
        cardNumber.setText("************" + invoice.getM_Payment().getM_CreditCardNumber().substring(invoice.getM_Payment().getM_CreditCardNumber().length() - 4));
        Button btn_Review = dv.findViewById(R.id.btnReview);

        //orderedItems listview
        listView = dv.findViewById(R.id.itemsList);
        OrderedItemsContract oic = new OrderedItemsContract(getContext());
        mOrderedItems = oic.getOrderedItems(invoice.getM_Order().getM_Id());
        listAdapter = new ItemsOrderedAdapter(getContext(), mOrderedItems);
        listView.setAdapter(listAdapter);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setNegativeButton("Cancel", null)
                .show();

        RatingsContract ratingsContract = new RatingsContract(getContext());
        OrdersContract ordersContract = new OrdersContract(getContext());
        Order order = ordersContract.getOrderById(invoice.getM_Order().getM_Id());

        if (ratingsContract.checkForExistingRating(order.getM_Customer().getM_Id(), order.getM_FoodTruck().getM_ID())) {
            btn_Review.setVisibility(View.INVISIBLE);
        }
        btn_Review.setOnClickListener(v -> {
            LayoutInflater dialogInflater2 = getLayoutInflater();
            View dv2 = dialogInflater2.inflate(R.layout.dialog_review, null);

            TextView titleCounter, descriptionCounter;
            EditText title, description;
            RatingBar rating;

            rating = dv2.findViewById(R.id.ratingBar);
            title = dv2.findViewById(R.id.reviewTitle);
            description = dv2.findViewById(R.id.reviewDescription);
            titleCounter = dv2.findViewById(R.id.titleCounter);
            descriptionCounter = dv2.findViewById(R.id.descriptionCounter);

            final AlertDialog alertDialog2 = new AlertDialog.Builder(getContext()).setView(dv2)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

            Button btnOk = alertDialog2.getButton(AlertDialog.BUTTON_POSITIVE);
            rating.setOnRatingBarChangeListener((ratingBar, rating1, fromUser) -> {
                if (rating1 < 1)
                    ratingBar.setRating(1);
            });

            btnOk.setOnClickListener(v1 -> {
                float rate = rating.getRating();
                if (rate < 1)
                    rate = 1.0F;
                ratingsContract.createRating(String.valueOf(rate), title.getText().toString(), description.getText().toString(), order.getM_FoodTruck().getM_ID(), order.getM_Customer().getM_Id());
                Toast.makeText(getContext(), "Review Submitted", Toast.LENGTH_SHORT).show();
                alertDialog2.dismiss();
                btn_Review.setVisibility(View.INVISIBLE);
            });

            //counter for text limit
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    titleCounter.setText(s.length() + "/50");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //counter for text limit
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    descriptionCounter.setText(s.length() + "/150");

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        });
    }
}