package com.example.foodtruck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.R;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> ItemsList;
    private Context itemContext;
    private OnItemListener mOnItemListener;

    //viewHolder class whose objects represent each list items
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemPrice;
        public TextView itemAvailable;
        public OnItemListener onItemListener;

        public ItemViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            itemName = view.findViewById(R.id.nameTxt);
            itemPrice = view.findViewById(R.id.priceTxt);
            itemAvailable = view.findViewById(R.id.availableTxt);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }
        public void BindData(Item itemCard, Context context)
        {
            itemName.setText(itemCard.getM_Name());
            itemPrice.setText(itemCard.getM_Price());
            itemAvailable.setText(itemCard.getM_Available());
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

    }
    //Default constructor
        public ItemsAdapter(List<Item> ItemsList, Context context, OnItemListener onItemListener) {
            this.ItemsList = ItemsList;
            itemContext = context;
            this.mOnItemListener = onItemListener;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate list item
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_card, parent, false);

            return new ItemViewHolder(itemView, mOnItemListener);
        }

        //Bind data to position
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.BindData(ItemsList.get(position), itemContext);
        }

        //return total number of items
        @Override
        public int getItemCount()
        {
        return ItemsList.size();
        }

        public interface OnItemListener {
        void onItemClick(int position);
    }
}
