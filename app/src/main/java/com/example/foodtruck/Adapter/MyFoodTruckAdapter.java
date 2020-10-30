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

import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.R;

public class MyFoodTruckAdapter extends ListAdapter<FoodTruck, MyFoodTruckAdapter.FoodTruckViewHolder> {

    //comparison method to add animations to recycler view
    private static DiffUtil.ItemCallback<FoodTruck> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodTruck>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodTruck oldItem, @NonNull FoodTruck newItem) {
            return oldItem.getM_ID() == newItem.getM_ID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodTruck oldItem, @NonNull FoodTruck newItem) {
            return oldItem.getM_Category().equals(newItem.getM_Category()) &&
                    (oldItem.getM_Latitude() == newItem.getM_Latitude()) &&
                    (oldItem.getM_Longitude() == newItem.getM_Longitude()) &&
                    oldItem.getM_Name().equals(newItem.getM_Name());
        }
    };
    private Context foodTruckContext;
    private onFoodTruckCardListener mFoodTruckCardListener;

    public MyFoodTruckAdapter(Context context, onFoodTruckCardListener onFoodTruckCardListener) {
        super(DIFF_CALLBACK);
        foodTruckContext = context;
        mFoodTruckCardListener = onFoodTruckCardListener;
    }

    @NonNull
    @Override
    public FoodTruckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate out food truck list item
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodtruck_card, parent, false);

        //return new view holder
        return new FoodTruckViewHolder(v, mFoodTruckCardListener);
    }

    //In Charge of diplaying the items correctly and in their correct positions
    @Override
    public void onBindViewHolder(@NonNull FoodTruckViewHolder holder, int position) {
        //bind data for the item at position
        holder.BindData(getItem(position), foodTruckContext);
    }

    public FoodTruck getFoodTruckAt(int position) {
        return getItem(position);
    }

    public interface onFoodTruckCardListener {
        void onCardClick(int pos);
    }

    public static class FoodTruckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView foodTruckPicture;
        public TextView foodTruckName;
        public onFoodTruckCardListener onFoodTruckCardListener;

        public FoodTruckViewHolder(@NonNull View itemView, onFoodTruckCardListener vOnFoodTruckCardListener) {
            super(itemView);
            foodTruckPicture = itemView.findViewById(R.id.foodTruckPicture);
            foodTruckName = itemView.findViewById(R.id.foodTruckName);
            onFoodTruckCardListener = vOnFoodTruckCardListener;
            itemView.setOnClickListener(this);
        }

        public void BindData(FoodTruck foodTruck, Context context) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(foodTruck.getM_Image(), 0, foodTruck.getM_Image().length);
            foodTruckPicture.setImageBitmap(bitmap);
            foodTruckName.setText(foodTruck.getM_Name());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
