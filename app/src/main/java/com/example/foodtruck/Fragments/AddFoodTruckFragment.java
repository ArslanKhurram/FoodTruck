package com.example.foodtruck.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddFoodTruckFragment extends Fragment implements View.OnClickListener {

    ImageView picView;
    Button picBttn;
    EditText etName;
    EditText etCategory;
    Spinner spFoodType;
    Button submit;
    Bitmap selectedImage;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_food_truck, container, false);

        picView = v.findViewById(R.id.picView);
        picBttn = v.findViewById(R.id.spnImageUpload);
        etName = v.findViewById(R.id.etName);
        etCategory = v.findViewById(R.id.etCategory);
        spFoodType = v.findViewById(R.id.spnPaymentType);
        v.findViewById(R.id.btnSubmit).setOnClickListener(this);
        return v;
    }

    //result sets image to an image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = (Bitmap) data.getExtras().get("data");
            picView.setImageBitmap(selectedImage);
        }
    }

    @Override
    public void onClick(View v) {
        FoodTrucksContract ft = new FoodTrucksContract(getContext());

        //convert image to byte to be able to pass int food truck database
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        //calling create function
        ft.createFoodTruck(etName.toString(), etCategory.toString(), bitMapData, 234);


    }
}
