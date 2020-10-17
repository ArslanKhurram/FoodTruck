package com.example.foodtruck.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.Models.PaymentCard;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class PaymentAdapter extends ListAdapter<Payment, PaymentAdapter.PaymentViewHolder> {
    private Context paymentContext;
    private onPaymentCardListener mPaymentCardListener;

    public PaymentAdapter(Context context, onPaymentCardListener onPaymentCardListener) {
        super(DIFF_CALLBACK);
        paymentContext = context;
        mPaymentCardListener = onPaymentCardListener;
    }

    //comparison method to add animations to recycler view
    private static DiffUtil.ItemCallback<Payment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Payment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.getM_ID() == newItem.getM_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.getM_NameOnCard().equals(newItem.getM_CreditCardNumber()) &&
                    oldItem.getM_CreditCardNumber().equals(newItem.getM_CreditCardNumber()) &&
                    oldItem.getM_CCEXPDATE().equals(newItem.getM_CCEXPDATE()) &&
                    oldItem.getM_PaymentType().equals(newItem.getM_PaymentType());
        }
    };

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
        holder.BindData(getItem(position), paymentContext);
    }

    public Payment getPaymentAt(int position) {
        return getItem(position);
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
            ivCard.setImageDrawable(ContextCompat.getDrawable(context, getCreditCardBrand(payment)));
            txtName.setText(payment.getM_NameOnCard());
            txtNumber.setText("************" + payment.getM_CreditCardNumber().substring((payment.getM_CreditCardNumber().length() - 4)));
            txtExpireDate.setText(payment.getM_CCEXPDATE());
            txtPaymentType.setText(payment.getM_PaymentType());
        }

        //method to determine credit card brand for image placeholder
        private int getCreditCardBrand(Payment payment) {
            String creditCardNum = payment.getM_CreditCardNumber();

            ArrayList<String> listOfPattern = new ArrayList<>();
            String ptVisa = "^4[0-9]{6,}$";
            listOfPattern.add(ptVisa);
            String ptMasterCard = "^5[1-5][0-9]{5,}$";
            listOfPattern.add(ptMasterCard);
            String ptAmeExp = "^3[47][0-9]{5,}$";
            listOfPattern.add(ptAmeExp);
            String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
            listOfPattern.add(ptDinClb);
            String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
            listOfPattern.add(ptDiscover);
            String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
            listOfPattern.add(ptJcb);


            return R.drawable.ic_creditcard;
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
