package com.example.foodtruck.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.PaymentAdapter;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentsFragment extends Fragment implements PaymentAdapter.onPaymentCardListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private PaymentAdapter paymentAdapter;
    private RecyclerView.LayoutManager paymentLayoutManager;
    private ArrayList<Payment> paymentsList = new ArrayList<>();
    private Pattern p;
    private Matcher m;
    private EditText cardNumber, nameOnCard, ccv;
    private Spinner month, year, paymentType;
    private SharedPreferences sharedPref;
    private LayoutInflater dialogInflater;
    private Payment paymentModel = new Payment();
    View dV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payments, container, false);


        FloatingActionButton btnAddACard = v.findViewById(R.id.btnAddCard);
        btnAddACard.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String user = sharedPref.getString("UserType", "");
        String email = sharedPref.getString("Email", "");

        //if user is a customer then import the payment list
        paymentAdapter = new PaymentAdapter(getContext(), this);
        paymentAdapter.submitList(getPaymentsList());

        if (paymentsList.size() < 1)
            showDialog();

        //recycler view setup
        recyclerView = v.findViewById(R.id.payments_recycler);
        recyclerView.setHasFixedSize(true);
        paymentLayoutManager = new LinearLayoutManager(getContext());
        recyclerAdapter = paymentAdapter;
        recyclerView.setLayoutManager(paymentLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Payment payment = paymentAdapter.getPaymentAt(viewHolder.getAdapterPosition());
                PaymentsContract pc = new PaymentsContract(getContext());
                pc.removePayment(payment.getM_ID());
                paymentAdapter.submitList(getPaymentsList());
                Toast.makeText(getContext(), "Payment Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        return v;
    }

    //handel process when a card is clicked
    @Override
    public void onCardClick(int pos) {
        Payment payment = paymentsList.get(pos);
    }

    public void showDialog() {
        //Initialize Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setter for message and title
        builder.setMessage("Please enter at least One Payment Method before ordering")
                .setTitle("No Payment Methods Found");

        //add button
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //create/show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //method to return updated payments list
    public ArrayList<Payment> getPaymentsList() {
        String email = sharedPref.getString("Email", "");

        CustomersContract cc = new CustomersContract(getContext()); //initialize Customer Contract
        Customer customer = cc.getCustomerIdByEmail(email); //set customer object to signed in customer by using email from SP
        PaymentsContract pc = new PaymentsContract(getContext()); //initialize Payment Contract
        paymentsList = pc.paymentsList(customer.getM_Id()); //set paymentList to payments of customer

        return paymentsList;
    }

    @Override
    public void onClick(View v) {
        addPaymentDialog();
    }

    //dialog for customer to enter payment method
    private void addPaymentDialog() {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_addpayment, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (addCardToDatabase()) {
                    paymentAdapter.submitList(getPaymentsList());
                    alertDialog.cancel();
                    Toast.makeText(getContext(), "Payment Added", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //add card to DB after user enters dara and press OK on dialog
    private boolean addCardToDatabase() {
        //References to all dialog variables

        cardNumber = dV.findViewById(R.id.creditCardNumber);
        nameOnCard = dV.findViewById(R.id.nameOnCard);
        ccv = dV.findViewById(R.id.ccv);
        month = dV.findViewById(R.id.expMonth);
        year = dV.findViewById(R.id.expYear);
        paymentType = dV.findViewById(R.id.paymentType);

        if (validateName(nameOnCard) & validateCardNumber(cardNumber) & validateCCV(ccv)) {
            String email = sharedPref.getString("Email", ""); //get email from shared pref

            CustomersContract cc = new CustomersContract(getContext());
            Customer customer = cc.getCustomerIdByEmail(email);

            PaymentsContract pc = new PaymentsContract(getContext());
            pc.createPayment(paymentType.getSelectedItem().toString(), nameOnCard.getText().toString(),
                    cardNumber.getText().toString(),
                    (month.getSelectedItem().toString() + "/" + year.getSelectedItem().toString()),
                    ccv.getText().toString(),
                    (Calendar.getInstance().getTime()).toString(),
                    customer.getM_Id());

            return true;
        } else

            return false;
    }

    //Validation For NameonCard
    private boolean validateName(EditText nameOnCard) {
        p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(nameOnCard.getText().toString());
        boolean cv = m.find();
        String name = nameOnCard.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameOnCard.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            nameOnCard.setError("Invalid Entry");
            return false;
        }

        return true;
    }

    //Validation For CardNumber
    private boolean validateCardNumber(EditText cardNumber) {
        p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(cardNumber.getText().toString());
        boolean zp = m.find();
        String num = cardNumber.getText().toString();

        if (num.length() != 16) {
            cardNumber.setError("Wrong Length");
            return false;
        } else if (!zp) {
            cardNumber.setError("Invalid Entry");
            return false;
        }

        return true;
    }

    //Validation For CCV
    private boolean validateCCV(EditText ccv) {
        p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(ccv.getText().toString());
        boolean zp = m.find();
        String num = ccv.getText().toString();

        if (num.length() < 3) {
            ccv.setError("Enter Full CCV");
            return false;
        } else if (!zp) {
            ccv.setError("Invalid Entry");
            return false;
        }

        return true;
    }
}