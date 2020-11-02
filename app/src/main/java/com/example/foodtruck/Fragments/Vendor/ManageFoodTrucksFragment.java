package com.example.foodtruck.Fragments.Vendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ManageFoodTrucksFragment extends Fragment implements MyFoodTruckAdapter.onFoodTruckCardListener, View.OnClickListener {

    private static final int GALLERY_REQUEST = '1';
    Bitmap bitmap;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private MyFoodTruckAdapter foodTruckAdapter;
    private RecyclerView.LayoutManager foodTruckLayoutManager;
    private TextView tv;
    EditText name, category, location;
    LatLng latLng;
    ImageView imageView;
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
        foodTruckAdapter.setHasStableIds(true);
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
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            //When Success Intialize Places
            Place place = Autocomplete.getPlaceFromIntent(data);
            //Set address on Edit Text
            location.setText(place.getAddress());
            latLng = place.getLatLng();
            Log.i("Address", place.getAddress());
            Log.i("Address", String.valueOf(latLng));
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //When error Initialize Status
            Status status = Autocomplete.getStatusFromIntent(data);
            //Display Message
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            if (requestCode == 10) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                    Log.i("Image", String.valueOf(bitmap));
                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
            }
        }
    }

    @Override
    public void onCardClick(int pos) {
        FoodTruck foodTruck = foodTruckList.get(pos);
        updateFoodTruckDialog(foodTruck);
    }

    @Override
    public void onClick(View v) {
        addFoodTruckDialog();
    }


    public ArrayList<FoodTruck> getFoodTruckList() {
        String email = sharedPref.getString("Email", "");

        VendorsContract vc = new VendorsContract(getContext());
        Vendor vendor = vc.getVendorIdByEmail(email);

        FoodTrucksContract fc = new FoodTrucksContract(getContext());
        foodTruckList = fc.FoodTruckList(vendor.getM_Id());

        return foodTruckList;
    }

    //dialog to allow vendor to add a foodtruck
    public void addFoodTruckDialog() {
        LayoutInflater dialogInflater = getLayoutInflater();
        View dv = dialogInflater.inflate(R.layout.dialog_add_foodtruck, null);


        name = dv.findViewById(R.id.foodTruckName);
        category = dv.findViewById(R.id.foodTruckCategory);
        location = dv.findViewById(R.id.foodTruckLocation);
        Button image = dv.findViewById(R.id.foodTruckImage);
        imageView = dv.findViewById(R.id.foodTruckLoadImage);

        //Initialize Places
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyCHwgmjQP5u0LAtT4D7ZJ_W3YdhwqEDJHY");

        //Set EditText Location no focusable
        location.setFocusable(false);
        location.setOnClickListener(v -> {
            //Initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                    , Place.Field.LAT_LNG, Place.Field.NAME);
            //Create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                    , fieldList).build(getActivity());

            //Start activity result
            startActivityForResult(intent, 100);
        });

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        add.setOnClickListener(v -> {
            if (imageView.getDrawable() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                VendorsContract vc = new VendorsContract(getActivity());
                Vendor vendor = vc.getVendorIdByEmail(sharedPref.getString("Email", ""));

                if (vendor != null) {
                    FoodTrucksContract fc = new FoodTrucksContract(getActivity());
                    fc.createFoodTruck(name.getText().toString(), category.getText().toString(), bitMapData, latLng.latitude, latLng.longitude, vendor.getM_Id());
                    foodTruckAdapter.submitList(getFoodTruckList());
                    alertDialog.cancel();
                }
            } else
                Snackbar.make(v, "                         Please Upload an Image", Snackbar.LENGTH_LONG).show();

        });
    }

    //dialog to allow vendor to add a foodtruck
    public void updateFoodTruckDialog(FoodTruck foodTruck) {
        LayoutInflater dialogInflater = getLayoutInflater();
        View dv = dialogInflater.inflate(R.layout.dialog_add_foodtruck, null);


        name = dv.findViewById(R.id.foodTruckName);
        category = dv.findViewById(R.id.foodTruckCategory);
        location = dv.findViewById(R.id.foodTruckLocation);
        Button image = dv.findViewById(R.id.foodTruckImage);
        imageView = dv.findViewById(R.id.foodTruckLoadImage);

        //set food truck details into edit texts and image view
        name.setText(foodTruck.getM_Name());
        category.setText(foodTruck.getM_Category());
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodTruck.getM_Image(), 0, foodTruck.getM_Image().length);
        imageView.setImageBitmap(bitmap);
        name.setSelection(name.getText().length());
        category.setSelection(category.getText().length());

        //get address from Lat and Long
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(foodTruck.getM_Latitude(), foodTruck.getM_Longitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0);
            location.setText(address);
        }

        //Initialize Places
        Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(), "AIzaSyCHwgmjQP5u0LAtT4D7ZJ_W3YdhwqEDJHY");

        //Set EditText Location no focusable
        location.setFocusable(false);
        location.setOnClickListener(v -> {
            //Initialize place field list
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                    , Place.Field.LAT_LNG, Place.Field.NAME);
            //Create intent
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                    , fieldList).build(getActivity());

            //Start activity result
            startActivityForResult(intent, 100);
        });

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        add.setOnClickListener(v -> {
            if (imageView.getDrawable() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                VendorsContract vc = new VendorsContract(getActivity());
                Vendor vendor = vc.getVendorIdByEmail(sharedPref.getString("Email", ""));

                if (vendor != null) {
                    FoodTrucksContract fc = new FoodTrucksContract(getActivity());
                    fc.updateFoodTruck(foodTruck.getM_ID(), name.getText().toString(), category.getText().toString(), bitMapData, latLng.latitude, latLng.longitude);
                    foodTruckAdapter.submitList(getFoodTruckList());
                    alertDialog.cancel();
                }
            } else
                Snackbar.make(v, "                         Please Upload an Image", Snackbar.LENGTH_LONG).show();

        });
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