package com.example.foodtruck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.R;

public class InvoiceAdapter extends ListAdapter<Invoice, InvoiceAdapter.InvoiceViewHolder> {
    private Context invoiceContext;
    private OnInvoiceListener mOnInvoiceListener;

    public InvoiceAdapter(Context context, OnInvoiceListener onInvoiceListener) {
        super(DIFF_CALLBACK);
        invoiceContext = context;
        mOnInvoiceListener = onInvoiceListener;
    }

    private static DiffUtil.ItemCallback<Invoice> DIFF_CALLBACK = new DiffUtil.ItemCallback<Invoice>() {
        @Override
        public boolean areItemsTheSame(@NonNull Invoice oldInvoice, @NonNull Invoice newInvoice) {
            return oldInvoice.getM_Id() == newInvoice.getM_Id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Invoice oldInvoice, @NonNull Invoice newInvoice) {
            return oldInvoice.getM_InvoiceDate().equals(newInvoice.getM_InvoiceDate()) &&
                    oldInvoice.getM_Total().equals(newInvoice.getM_Total()) &&
                    oldInvoice.getM_ServiceCharge().equals(newInvoice.getM_ServiceCharge()) &&
                    oldInvoice.getM_TaxAmount().equals(newInvoice.getM_TaxAmount()) &&
                    oldInvoice.getM_TotalInvoiceAmount().equals(newInvoice.getM_TotalInvoiceAmount());
        }
    };

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View InvoiceView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_history, parent, false);
        return new InvoiceViewHolder(InvoiceView, mOnInvoiceListener);
    }

    //Bind data to position
    @Override
    public void onBindViewHolder(InvoiceViewHolder holder, int position) {
        holder.BindData(getItem(position), invoiceContext);
    }

    public Invoice getInvoiceAt(int position) {
        return getItem(position);
    }

    //viewHolder class whose objects represent each list items
    public static class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView invoiceDate, foodTruckName, totalPrice;
        public OnInvoiceListener onInvoiceListener;

        public InvoiceViewHolder(View view, OnInvoiceListener onInvoiceListener) {
            super(view);
            invoiceDate = view.findViewById(R.id.tvDateOfOrder);
            foodTruckName = view.findViewById(R.id.tvFoodtruckName);
            totalPrice = view.findViewById(R.id.tvPriceOfOrder);
            this.onInvoiceListener = onInvoiceListener;

            view.setOnClickListener(this);
        }

        public void BindData(Invoice invoice, Context context) {
            invoiceDate.setText(invoice.getM_InvoiceDate());
            foodTruckName.setText(invoice.getM_Order().getM_FoodTruck().getM_Name());
            totalPrice.setText((invoice.getM_TotalInvoiceAmount()));
        }

        @Override
        public void onClick(View v) {
            onInvoiceListener.onInvoiceClick(v, getAdapterPosition());
        }
    }

    public interface OnInvoiceListener {
        void onInvoiceClick(View view, int position);
    }
}
