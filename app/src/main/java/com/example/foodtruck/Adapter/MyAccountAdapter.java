package com.example.foodtruck.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Card;
import com.example.foodtruck.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyViewHolder> {

    private List<Card> mCardList;
    private Context mContect;
    private OnCardListener onCardListener;

    public MyAccountAdapter(List<Card> cardList, Context context, OnCardListener onCardListener) {
        mCardList = cardList;
        mContect = context;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate out cars list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recycler_view, parent, false);

        //return new view holder
        return new MyViewHolder(view, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //bind data for the item at position

        holder.bindData(mCardList.get(position), mContect);
    }

    @Override
    public int getItemCount() {

        //return the total number of items
        return mCardList.size();
    }

    // View Holder class whose objects represent each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView cardImageView;
        public MaterialTextView cardTextView;
        OnCardListener onCardListener;

        //holds reference to objects inside card
        public MyViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);
            //add reference to view so adapter can use them
            cardImageView = itemView.findViewById(R.id.accountCardImageView);
            cardTextView = itemView.findViewById(R.id.accountCardTextView);
            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        public void bindData(Card card, Context context) {
            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow));
            cardTextView.setText(card.getOption());
        }

        //when card is clicked, get that position in the adapter
        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }


    public interface OnCardListener {
        void onCardClick(int postion);
    }
}
