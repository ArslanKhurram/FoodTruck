package com.example.foodtruck.Fragments.Customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.R;

public class OrderConfirmationFragment extends Fragment implements View.OnClickListener {

    private Invoice m_Invoice;
    private Order m_Order;
    private Button btnReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_confirmation, container, false);

        TextView subtotal, serviceCharge, tax, total, orderNumber, status, date, greeting;
        Bundle bundle = getArguments();


        subtotal = v.findViewById(R.id.subTotal);
        serviceCharge = v.findViewById(R.id.serviceCharge);
        tax = v.findViewById(R.id.tax);
        total = v.findViewById(R.id.total);
        orderNumber = v.findViewById(R.id.orderNumber);
        status = v.findViewById(R.id.status);
        date = v.findViewById(R.id.date);
        btnReview = v.findViewById(R.id.btnReview);
        greeting = v.findViewById(R.id.greeting);

        OrdersContract ordersContract = new OrdersContract(getContext());
        if (bundle != null){
            m_Order = ordersContract.getOrderByOrderNumber(bundle.getString("orderNumber"));
        }
        //load invoice
        loadInvoice(m_Order);

        greeting.setText("Thank you for\nPlacing Your Order at\n" + m_Order.getM_FoodTruck().getM_Name());
        subtotal.setText(m_Invoice.getM_Total());
        serviceCharge.setText(m_Invoice.getM_ServiceCharge());
        tax.setText(m_Invoice.getM_TaxAmount());
        total.setText(m_Invoice.getM_TotalInvoiceAmount());
        orderNumber.setText(m_Order.getM_OrderNumber());
        status.setText(m_Order.getM_Status());
        date.setText(m_Order.getM_DateAdded());

        btnReview.setOnClickListener(this);

        return v;
    }

    private void loadInvoice(Order order) {
        InvoiceContract invoiceContract = new InvoiceContract(getContext());
        m_Invoice = invoiceContract.getInvoiceByOrderID(order.getM_Id());
    }

    @Override
    public void onClick(View v) {
        LayoutInflater dialogInflator = getLayoutInflater();
        View dv = dialogInflator.inflate(R.layout.dialog_review, null);

        TextView titleCounter, descriptionCounter;
        EditText title, description;
        RatingBar rating;

        rating = dv.findViewById(R.id.ratingBar);
        title = dv.findViewById(R.id.reviewTitle);
        description = dv.findViewById(R.id.reviewDescription);
        titleCounter = dv.findViewById(R.id.titleCounter);
        descriptionCounter = dv.findViewById(R.id.descriptionCounter);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        rating.setOnRatingBarChangeListener((ratingBar, rating1, fromUser) -> {
            if (rating1 < 1)
                ratingBar.setRating(1);
        });

        btnOk.setOnClickListener(v1 -> {
            RatingsContract ratingsContract = new RatingsContract(getContext());
            float rate = rating.getRating();
            if (rate < 1)
                rate = 1.0F;
            ratingsContract.createRating(String.valueOf(rate),title.getText().toString(),description.getText().toString(), m_Order.getM_FoodTruck().getM_ID(), m_Order.getM_Customer().getM_Id());
            Toast.makeText(getContext(), "Review Submitted", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            btnReview.setVisibility(View.INVISIBLE);
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
    }


}
