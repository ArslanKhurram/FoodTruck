package com.example.foodtruck.Fragments.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.Adapter.MyAccountAdapter;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.Fragments.PaymentsFragment;
import com.example.foodtruck.Models.Card;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAccountFragment extends Fragment implements MyAccountAdapter.OnCardListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> cardList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_account, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("Email", "");

        CustomersContract customersContract = new CustomersContract(getContext());
        Customer customer = customersContract.getCustomerIdByEmail(email);

        mRecyclerView = v.findViewById(R.id.accountRecycleView);
        cardList.add(new Card("Name: ", customer.getM_FirstName() + " " + customer.getM_LastName()));
        cardList.add(new Card("Email: ", customer.getM_Email()));
        cardList.add(new Card("Payment Methods", ""));
        cardList.add(new Card("Order History", ""));
        cardList.add(new Card("Sign Out", ""));

        //improve performance of app by setting fixed size
        mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager on the cards
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify the adapter and pass in the card model list
        mAdapter = new MyAccountAdapter(cardList, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }


    //handel action after card is pressed
    @Override
    public void onCardClick(int position) {
        Card card = cardList.get(position);

        switch (card.getOption()) {
            case "Name":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
                break;
            case "Email":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
                break;
            case "Payment Methods":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new PaymentsFragment()).commit();
                break;
            case "Order History":
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new OrderHistory()).commit();
                break;
            case "Sign Out":
                SignUpActivity.currentFragment = "SignOut";
                getActivity().onBackPressed();
                break;
        }
    }
}
