package com.example.foodtruck.Fragments.Customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MyAccountAdapter;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Fragments.PaymentsFragment;
import com.example.foodtruck.Models.Card;
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

        mRecyclerView = v.findViewById(R.id.accountRecycleView);
        cardList.add(new Card("Name"));
        cardList.add(new Card("Email"));
        cardList.add(new Card("Payments"));
        cardList.add(new Card("Sign Out"));

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
            case "Payments":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new PaymentsFragment()).commit();
                break;
            case "Sign Out":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
                break;
        }
    }
}
