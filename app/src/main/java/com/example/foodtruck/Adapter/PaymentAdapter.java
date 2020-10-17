package com.example.foodtruck.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.Models.PaymentCard;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private ArrayList<Payment> paymentCardList;
    private Context paymentContext;
    private onPaymentCardListener mPaymentCardListener;

    public PaymentAdapter(ArrayList<Payment> paymentList, Context context, onPaymentCardListener pcl) {
        paymentCardList = paymentList;
        paymentContext = context;
        mPaymentCardListener = pcl;
    }

    public PaymentAdapter() {
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate out payment list item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card, parent, false);

        //return new view holder
        return new PaymentViewHolder(v, mPaymentCardListener);
    }


    // In charge of displaying the items correctly and in their correct positions
    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        //bind data for the item at position
        holder.BindData(paymentCardList.get(position), paymentContext);
    }

    @Override
    public int getItemCount() {
        return paymentCardList.size();
    }

    //refresh view if payments are deleted or added
    public void refresh(ArrayList<Payment> payments) {
        if (paymentCardList != null) {
            paymentCardList.clear();
            paymentCardList.addAll(payments);
        } else {
            paymentCardList = payments;
        }
        notifyDataSetChanged();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivCard;
        public TextView txtName, txtNumber, txtExpireDate, txtPaymentType;
        public onPaymentCardListener paymentCardListener;

        public PaymentViewHolder(@NonNull View itemView, onPaymentCardListener pcl) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivPaymentCard);
            txtName = itemView.findViewById(R.id.nameOnCard);
            txtNumber = itemView.findViewById(R.id.creditCardNumber);
            txtExpireDate = itemView.findViewById(R.id.expireDate);
            txtPaymentType = itemView.findViewById(R.id.paymentType);
            paymentCardListener = pcl;
            itemView.setOnClickListener(this);
        }

        public void BindData(Payment payment, Context context) {
            ivCard.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_creditcard));
            txtName.setText(payment.getM_NameOnCard());
            txtNumber.setText("************" + payment.getM_CreditCardNumber().substring((payment.getM_CreditCardNumber().length() - 4)));
            txtExpireDate.setText(payment.getM_CCEXPDATE());
            txtPaymentType.setText(payment.getM_PaymentType());
        }

        @Override
        public void onClick(View v) {
            paymentCardListener.onCardClick(getAdapterPosition());
        }
    }


    public interface onPaymentCardListener {
        void onCardClick(int pos);

    }

}
