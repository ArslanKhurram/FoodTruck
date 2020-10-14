package com.example.foodtruck.Fragments.Vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
        picBttn = v.findViewById(R.id.btnImageUpload);
        etName = v.findViewById(R.id.etName);
        etCategory = v.findViewById(R.id.etCategory);
        spFoodType = v.findViewById(R.id.spnPaymentType);
        v.findViewById(R.id.btnSubmit).setOnClickListener(this);
        return v;


    }

    /*
    //result sets image to an image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            selectedImage = (Bitmap) data.getExtras().get("data");
            picView.setImageBitmap(selectedImage);
        }
    }
*/
    @Override
    public void onClick(View v) {

        FoodTrucksContract ft = new FoodTrucksContract(getContext());
        VendorsContract vc = new VendorsContract(getContext());

        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);//get the vendor email from shared preference
        String email = sharedPref.getString("Email", ""); //set email from SP to a variable


        Vendor vendor = vc.getVendorIdByEmail(email);

        //convert image to byte to be able to pass int food truck database
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.test, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        //calling create function
        ft.createFoodTruck(etName.getText().toString(), etCategory.getText().toString(), bitMapData, 89.9393, 93.939, vendor.getM_Id());
        Toast.makeText(getContext(), "Truck Added", Toast.LENGTH_SHORT).show();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new VendorAccountFragment()).commit();
    }


}
