package com.example.foodtruck.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.R;

public class CustomerMenuAdapter extends ListAdapter<Item, CustomerMenuAdapter.ItemViewHolder> {

    private Context itemContext;
    private OnItemListener mOnItemListener;

    public CustomerMenuAdapter(Context context, OnItemListener onItemListener) {
        super(DIFF_CALLBACK);
        itemContext = context;
        mOnItemListener = onItemListener;
    }

    //comparison method to add animations to recycler view
    private static DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getM_Id() == newItem.getM_Id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getM_Name().equals(newItem.getM_Name()) &&
                    oldItem.getM_Price().equals(newItem.getM_Price()) &&
                    oldItem.getM_Available().equals(newItem.getM_Available());
        }
    };

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate list item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_customerview_card, parent, false);

        return new ItemViewHolder(itemView, mOnItemListener);
    }

    //Bind data to position
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.BindData(getItem(position), itemContext);
    }

    public Item getItemAt(int position) {
        return getItem(position);
    }

    //viewHolder class whose objects represent each list items
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemName, itemPrice, itemSoldOut;
        private ImageView itemPicture;
        private OnItemListener onItemListener;

        public ItemViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            itemSoldOut = view.findViewById(R.id.availableTxt);
            itemPicture = view.findViewById(R.id.ivMenuItemPicture);
            this.onItemListener = onItemListener;
            view.setOnClickListener(this);
        }

        public void BindData(Item item, Context context) {
            itemName.setText(item.getM_Name());
            itemPrice.setText(item.getM_Price());
            itemSoldOut.setText("[Sold Out]");
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getM_Image(), 0, item.getM_Image().length);
            itemPicture.setImageBitmap(bitmap);
            if (item.getM_Available().equals("No")) {
                itemName.setTextColor(Color.GRAY);
                itemPrice.setTextColor(Color.GRAY);
                itemSoldOut.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

    }

        public interface OnItemListener {
        void onItemClick(int position);
    }
}
