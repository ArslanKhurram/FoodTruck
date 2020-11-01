package com.example.foodtruck.Fragments.Vendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MyFoodTruckAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class ManageFoodTrucksFragment extends Fragment implements MyFoodTruckAdapter.onFoodTruckCardListener, View.OnClickListener {

    private static final int GALLERY_REQUEST = '1';
    ImageView picView;
    Bitmap bitmap;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private MyFoodTruckAdapter foodTruckAdapter;
    private RecyclerView.LayoutManager foodTruckLayoutManager;
    private TextView tv;
    private ArrayList<FoodTruck> foodTruckList = new ArrayList<>();
    private SharedPreferences sharedPref;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_manage_foodtrucks, container, false);

        FloatingActionButton btnAddFoodTruck = v.findViewById(R.id.btnAddFoodTruck);
        btnAddFoodTruck.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        foodTruckAdapter = new MyFoodTruckAdapter(getContext(), this);
        tv = v.findViewById(R.id.noFoodTruckPrompt);

        //check is list is null(empty)
        if (getFoodTruckList() != null) {
            foodTruckAdapter.submitList(getFoodTruckList());
            tv.setVisibility(View.INVISIBLE);
        } else
            tv.setVisibility(View.VISIBLE);

        recyclerView = v.findViewById(R.id.foodtruck_recycler);
        recyclerView.setHasFixedSize(true);
        foodTruckLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(foodTruckLayoutManager);
        recyclerView.setAdapter(foodTruckAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                FoodTruck foodTruck = foodTruckAdapter.getFoodTruckAt(viewHolder.getAdapterPosition()); //get foodtruck that is being swiped
                deleteDialog(foodTruck, viewHolder.getAdapterPosition()); //open dialog to prompt vendor
            }
        }).attachToRecyclerView(recyclerView);

        return v;
    }

    //result sets image to an image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        picView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onCardClick(int pos) {

    }

    public ArrayList<FoodTruck> getFoodTruckList() {
        String email = sharedPref.getString("Email", "");

        VendorsContract vc = new VendorsContract(getContext());
        Vendor vendor = vc.getVendorIdByEmail(email);

        FoodTrucksContract fc = new FoodTrucksContract(getContext());
        foodTruckList = fc.FoodTruckList(vendor.getM_Id());

        return foodTruckList;
    }

    public void addFoodTruckDialog() {

    }

    //dialog to handle if vendors wants to delete a food truck. Will be prompted with message and has to confirm delete
    public void deleteDialog(FoodTruck foodTruck, int position) {
        LayoutInflater dialogInflater = getLayoutInflater();
        View dv = dialogInflater.inflate(R.layout.dialog_delete, null);

        //create Alert Dialog
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton(R.string.delete, null)
                .setNegativeButton("Cancel", null)
                .show();

        //set DELETE buttons text to red
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.delete, null));

        //DELETE button is pressed
        Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(v -> {
            FoodTrucksContract fc = new FoodTrucksContract(getContext());
            fc.deleteFoodTruckByID(foodTruck.getM_ID());
            foodTruckAdapter.submitList(getFoodTruckList());
            if (foodTruckList == null)
                tv.setVisibility(View.VISIBLE);
            alertDialog.cancel();
        });

        //CANCEL button is pressed
        Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negative.setOnClickListener(v -> {
            foodTruckAdapter.notifyItemChanged(position + 1); //notify recycler view which item was being swiped
            foodTruckAdapter.notifyItemRangeChanged(position, foodTruckAdapter.getItemCount()); //restore the item being swiped and will not remove
            alertDialog.cancel();
        });
    }
}