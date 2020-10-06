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

import com.example.foodtruck.R;

import static android.app.Activity.RESULT_OK;

public class AddFoodTruckFragment extends Fragment {

    ImageView picView;
    Button picBttn;
    EditText etName;
    EditText etCategory;
    Spinner spFoodType;
    Button back;
    Button submit;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_food_truck, container, false);

        picView = v.findViewById(R.id.picView);
        picBttn = v.findViewById(R.id.spnImageUpload);
        etName = v.findViewById(R.id.etName);
        etCategory = v.findViewById(R.id.etCategory);
        spFoodType = v.findViewById(R.id.spnPaymentType);
        back = v.findViewById(R.id.btnBack);
        submit = v.findViewById(R.id.btnSubmit);

        //choose an image from gallery after clicking button
        picBttn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        return v;
    }

    //result sets image to an image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            picView.setImageBitmap(selectedImage);
        }

    }
}
