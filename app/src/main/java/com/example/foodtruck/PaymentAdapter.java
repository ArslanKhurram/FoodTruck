package com.example.foodtruck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodtruck.Models.PaymentCard;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private ArrayList<PaymentCard> paymentCardList;
    private Context paymentContext;
    private onPaymentCardListener mPaymentCardListener;

    public static class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivCard;
        public TextView txtName, txtNumber;
        public onPaymentCardListener paymentCardListener;

        public PaymentViewHolder(@NonNull View itemView, onPaymentCardListener pcl) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivPaymentCard);
            txtName = itemView.findViewById(R.id.txtPaymentName);
            txtNumber = itemView.findViewById(R.id.txtPaymentNumber);
            paymentCardListener = pcl;
            itemView.setOnClickListener(this);
        }

        public void BindData(PaymentCard card, Context context) {
            ivCard.setImageResource(card.getPaymentImage());
            txtName.setText(card.getPaymentName());
            txtNumber.setText(card.getPaymentNumber());
        }

        @Override
        public void onClick(View v) {
            paymentCardListener.onCardClick(getAdapterPosition());
        }
    }

    public PaymentAdapter(ArrayList<PaymentCard> paymentList, Context context, onPaymentCardListener pcl) {
        paymentCardList = paymentList;
        paymentContext = context;
        mPaymentCardListener = pcl;
   }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card, parent, false);
        PaymentViewHolder vh = new PaymentViewHolder(v, mPaymentCardListener);
        return vh;
    }


    // In charge of displaying the items correctly and in their correct positions
    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.BindData(paymentCardList.get(position), paymentContext);

        //PaymentCard currentItem = paymentCardList.get(position);
        //holder.ivCard.setImageResource(currentItem.getPaymentImage());
        //holder.txtName.setText(currentItem.getPaymentName());
        //holder.txtNumber.setText(currentItem.getPaymentNumber());

    }

    @Override
    public int getItemCount() {
        return paymentCardList.size();
    }

    public interface onPaymentCardListener {
        void onCardClick (int pos);

    }

}
