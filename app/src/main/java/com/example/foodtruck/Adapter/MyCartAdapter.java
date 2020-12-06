package com.example.foodtruck.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.R;

public class MyCartAdapter extends ListAdapter<Cart, MyCartAdapter.ItemViewHolder> {

    private Context itemContext;
    private OnItemListener mOnItemListener;

    public MyCartAdapter( Context context, OnItemListener onItemListener) {
        super(DIFF_CALLBACK);
        itemContext = context;
        mOnItemListener = onItemListener;


    }

   public static DiffUtil.ItemCallback<Cart>DIFF_CALLBACK=new DiffUtil.ItemCallback<Cart>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getM_ID() == newItem.getM_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getM_Item().getM_Name().equals(newItem.getM_Item().getM_Name()) &&
                    oldItem.getM_Item().getM_Price().equals(newItem.getM_Item().getM_Price()) &&
                    oldItem.getM_Quantity().equals(newItem.getM_OrderNumber());
        }
    };



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate list item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);

        return new ItemViewHolder(itemView, mOnItemListener);
    }

    //Bind data to position
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.BindData(getItem(position), itemContext);
    }

    public Cart getItemAt(int position) {
        return getItem(position);
    }

    //viewHolder class whose objects represent each list items
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView menuIcon;
        private TextView itemName, itemPrice, itemQnty;
        private OnItemListener onItemListener;

        public ItemViewHolder(View view, OnItemListener onItemListener) {
            super(view);
            itemName = view.findViewById(R.id.itemNameCard);
            itemPrice = view.findViewById(R.id.itemPriceCard);
            itemQnty = view.findViewById(R.id.itemQntyCard);
            menuIcon = view.findViewById(R.id.ivMenuItemPicture);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        public void BindData(Cart cart, Context context) {


            itemName.setText(cart.getM_Item().getM_Name());
            itemPrice.setText("Price: " + cart.getM_Item().getM_Price());
            itemQnty.setText("Qty: " + cart.getM_Quantity());

            Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getM_Item().getM_Image(), 0, cart.getM_Item().getM_Image().length);
            menuIcon.setImageBitmap(bitmap);
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
