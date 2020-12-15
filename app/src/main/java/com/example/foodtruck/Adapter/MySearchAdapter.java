package com.example.foodtruck.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.Fragments.SearchFragment;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;

import static java.security.AccessController.getContext;

public class MySearchAdapter extends ListAdapter<FoodTruck, MySearchAdapter.FoodTruckViewHolder> {

    Context foodTruckContext;
    onCardClickListener mSearchCardListener;
    public SearchFragment searchFrag;

    //comparison method to add animations to recycler view
    private static DiffUtil.ItemCallback<FoodTruck> DIFF_ITEMCALLBACK = new DiffUtil.ItemCallback<FoodTruck>() {
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

    public MySearchAdapter(Context context, onCardClickListener onCardClickListener, SearchFragment fragment) {
        super(DIFF_ITEMCALLBACK);
        foodTruckContext = context;
        mSearchCardListener = onCardClickListener;
        searchFrag = fragment;
    }

    @Override
    public FoodTruckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new FoodTruckViewHolder(v, mSearchCardListener);
    }

    // Display items in their positions
    @Override
    public void onBindViewHolder(@NonNull FoodTruckViewHolder holder, int position) {
        //bind data for the item at position
        holder.BindData(getItem(position), foodTruckContext, searchFrag);
    }

    public interface onCardClickListener {
        void onCardClick(int pos);
    }

    public static class FoodTruckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView foodTruckPicture;
        public TextView foodTruckName, foodTruckCategory, foodTruckDistance, foodTruckRating, foodTruckRatingCount;
        public onCardClickListener onCardClickListener;

        public FoodTruckViewHolder(@NonNull View itemView, onCardClickListener vOnCardClickListener) {
            super(itemView);
            foodTruckPicture = itemView.findViewById(R.id.ivSearchItem);
            foodTruckName = itemView.findViewById(R.id.txtSearchName);
            foodTruckCategory = itemView.findViewById(R.id.txtSearchCategory);
            foodTruckDistance = itemView.findViewById(R.id.txtDistance);
            foodTruckRating = itemView.findViewById(R.id.txtSearchRating);
            foodTruckRatingCount= itemView.findViewById(R.id.txtRatingCount);
            onCardClickListener = vOnCardClickListener;
            itemView.setOnClickListener(this);
        }

        public void BindData(FoodTruck foodTruck, Context context, SearchFragment fragment) {
            RatingsContract rc = new RatingsContract(fragment.getContext());
            Bitmap bmp = BitmapFactory.decodeByteArray(foodTruck.getM_Image(), 0, foodTruck.getM_Image().length);
            foodTruckPicture.setImageBitmap(bmp);
            foodTruckName.setText(foodTruck.getM_Name());
            foodTruckCategory.setText(foodTruck.getM_Category());
            foodTruckDistance.setText(String.format("%.1f Miles away", fragment.distanceToFoodTruck(foodTruck)));
            foodTruckRating.setText(String.format("%.1f", rc.averageRatingsForID(foodTruck.getM_ID())));
            foodTruckRatingCount.setText("(" + String.valueOf(rc.countRatingsForID(foodTruck.getM_ID())) + ")");
            rc.close();


        //    if (ActivityCompat.checkSelfPermission(, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //        foodTruckDistance.setText("Enable Permissions");
        //    } else
        //        foodTruckDistance.setText("");
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition());
        }
    }
}