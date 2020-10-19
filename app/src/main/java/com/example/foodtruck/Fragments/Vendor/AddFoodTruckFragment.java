package com.example.foodtruck.Fragments.Vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddFoodTruckFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST = '1';
    ImageView picView;
    Button picBttn;
    EditText etName;
    EditText etCategory;
    Spinner spFoodType;
    Bitmap bitmap;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_food_truck, container, false);

        picView = v.findViewById(R.id.picView);
        picBttn = v.findViewById(R.id.btnImageUpload);
        etName = v.findViewById(R.id.etName);
        etCategory = v.findViewById(R.id.etCategory);
        spFoodType = v.findViewById(R.id.spnPaymentType);
        v.findViewById(R.id.btnSubmit).setOnClickListener(this);
        picBttn.setOnClickListener(new View.OnClickListener() {
           //add a picture using photos/gallery
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        return v;
    }

    //result sets image to an image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
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

        FoodTrucksContract ft = new FoodTrucksContract(getContext());
        VendorsContract vc = new VendorsContract(getContext());

        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);//get the vendor email from shared preference
        String email = sharedPref.getString("Email", ""); //set email from SP to a variable


        Vendor vendor = vc.getVendorIdByEmail(email);

        //convert image to byte to be able to pass int food truck database
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        //calling create function
        ft.createFoodTruck(etName.getText().toString(), etCategory.getText().toString(), bitMapData, 89.9393, 93.939, vendor.getM_Id());
        Toast.makeText(getContext(), "Truck Added", Toast.LENGTH_SHORT).show();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new VendorAccountFragment()).commit();
    }


}
