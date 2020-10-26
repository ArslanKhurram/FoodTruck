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

import com.example.foodtruck.Models.Order;
import com.example.foodtruck.R;

import java.util.List;

public class OrderAdapter extends ListAdapter<Order, OrderAdapter.OrderViewHolder> {
    private Context orderContext;
    private OnOrderListener mOnOrderListener;

    public OrderAdapter(Context context, OnOrderListener onOrderListener) {
        super(DIFF_CALLBACK);
        orderContext = context;
        mOnOrderListener = onOrderListener;
    }

    //comparison method to add animations to recycler view
    private static DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<Order>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldOrder, @NonNull Order newOrder) {
            return oldOrder.getM_Id() == newOrder.getM_Id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldOrder, @NonNull Order newOrder) {
            return oldOrder.getM_OrderNumber().equals(newOrder.getM_OrderNumber());


        }
    };

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate list item
        View OrderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_card, parent, false);

        return new OrderViewHolder(OrderView, mOnOrderListener);
    }

    //Bind data to position
    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.BindData(getOrder(position), orderContext);
    }

    public Order getOrder(int position) {
        return getOrder(position);
    }


    //viewHolder class whose objects represent each list items
    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView orderNum, orderName, orderQuantity;
        private OnOrderListener onOrderListener;

        public OrderViewHolder(View view, OnOrderListener onOrderListener) {
            super(view);
            orderNum.findViewById(R.id.tvOrderNum);
            orderName.findViewById(R.id.tvName);
            orderQuantity.findViewById(R.id.tvQuantity);
            this.onOrderListener = onOrderListener;

            view.setOnClickListener(this);
        }

        public void BindData(Order order, Context context) {
            orderNum.setText(order.getM_OrderNumber());
        }

        @Override
        public void onClick(View v) {
            onOrderListener.onOrderClick(getAdapterPosition());
        }
    }

        public interface OnOrderListener {
            void onOrderClick(int position);
        }
}
