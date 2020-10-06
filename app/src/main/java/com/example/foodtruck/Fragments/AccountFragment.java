package com.example.foodtruck.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MyAccountAdapter;
import com.example.foodtruck.Models.Card;
import com.example.foodtruck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements MyAccountAdapter.OnCardListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Card> cardList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

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
            case "Email":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
            case "Payments":
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
            case "Sign Out":
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new LoginFragment()).commit();
        }
    }
}