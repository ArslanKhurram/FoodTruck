package com.example.foodtruck.Fragments.Vendor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private MenuAdapter menuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> itemList = new ArrayList<>();
    private EditText itemName, itemPrice;
    private TextView tv;
    private Spinner itemAvailability, foodTruckSpinner;
    private FoodTruck currentFoodTruck;
    private SharedPreferences sharedPref;
    private LayoutInflater dialogInflater;
    private Menu menu;
    View dV;
    private Pattern p;
    private Matcher m;
    private Bitmap bitmap;
    private ImageView imageView;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        FloatingActionButton btnAddMenuItem = v.findViewById(R.id.btnAddItem);
        btnAddMenuItem.setOnClickListener(this);
        tv = v.findViewById(R.id.noMenuPrompt);
        foodTruckSpinner = v.findViewById(R.id.foodTruckSpinner);

        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        generateFoodTruckSpinnerData();

        menuAdapter = new MenuAdapter(getContext(), this);
        menuAdapter.submitList(getMenuList());
        tv.setVisibility(View.INVISIBLE);
        foodTruckSpinner.setOnItemSelectedListener(this);
        if (itemList == null)
            tv.setVisibility(View.VISIBLE);

        recyclerView = v.findViewById(R.id.ItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); //use linear layout on cards
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = menuAdapter;//specify adapter and pass in item list
        recyclerView.setAdapter(recyclerAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = itemList.get(viewHolder.getAdapterPosition());
                ItemsContract ic = new ItemsContract(getContext());
                ic.removeItem(item.getM_Id());
                menuAdapter.submitList(getMenuList());
                if (itemList == null)
                    tv.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        return v;
    }

    public ArrayList<Item> getMenuList() {
        MenusContract mc = new MenusContract(getContext());
        ItemsContract ic = new ItemsContract(getContext());

        //get menu from foodTruck
        if (currentFoodTruck != null) {
            menu = mc.getMenuByFoodTruckId(currentFoodTruck.getM_ID());
            if (menu != null) {
                itemList = ic.getItemListByMenuID(menu.getM_Id());
                tv.setVisibility(View.INVISIBLE);
            } else {
                int size = itemList.size();
                itemList.clear();
                menuAdapter.notifyItemRangeRemoved(0, size);
                tv.setVisibility(View.VISIBLE);
            }
        }

        return itemList;
    }

    @Override
    public void onItemClick(int position) {
        Item item = menuAdapter.getItemAt(position);
        editItemDialog(item);
    }

    @Override
    public void onClick(View view) {
        addItemDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void generateFoodTruckSpinnerData() {
        VendorsContract vendorsContract = new VendorsContract(getContext());
        Vendor vendor = vendorsContract.getVendorIdByEmail(sharedPref.getString("Email", ""));
        FoodTrucksContract foodTrucksContract = new FoodTrucksContract(getContext());
        ArrayList<FoodTruck> foodTrucks = foodTrucksContract.FoodTruckList(vendor.getM_Id());

        ArrayAdapter<FoodTruck> spinnerAdapter = new ArrayAdapter<FoodTruck>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(foodTrucks);
        foodTruckSpinner.setAdapter(spinnerAdapter);
    }


    private void addItemDialog() {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_addmenu_item, null);
        imageView = dV.findViewById(R.id.menuLoadImage);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button image = dV.findViewById(R.id.dl_btnAddItemImage);

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });

        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(v -> {
            if (addItemToDatabase()) {
                menuAdapter.submitList(getMenuList());
                alertDialog.cancel();
                Toast.makeText(getContext(), "Menu Item Added", Toast.LENGTH_LONG).show();
                tv.setVisibility(View.INVISIBLE);
            }
        });
    }

    //update
    private void editItemDialog(Item item) {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_addmenu_item, null);
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);
        imageView = dV.findViewById(R.id.menuLoadImage);
        Button imageUpload = dV.findViewById(R.id.dl_btnAddItemImage);

        //set image
        bitmap = BitmapFactory.decodeByteArray(item.getM_Image(), 0, item.getM_Image().length);
        imageView.setImageBitmap(bitmap);

        //value for spinner
        int selection = ((item.getM_Available().equals("Yes")) ? 0 : 1);

        itemName.setText(item.getM_Name());
        itemPrice.setText(item.getM_Price());
        itemAvailability.setSelection(selection);
        itemName.setSelection(itemName.getText().length());
        itemPrice.setSelection(itemPrice.getText().length());

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .show();

        imageUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });

        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(v -> {
            if (updateItemInDatabase(item)) {
                alertDialog.cancel();
                Toast.makeText(getContext(), "Menu Item Updated", Toast.LENGTH_LONG).show();
                tv.setVisibility(View.INVISIBLE);
            }
        });

        alertDialog.setOnDismissListener(dialog -> {
            menuAdapter.notifyDataSetChanged();
        });
    }

    //add menu item to DB after user enters data and presses ADD
    private boolean addItemToDatabase() {
        //references to all Dialog views
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);
        imageView = dV.findViewById(R.id.menuLoadImage);
        MenusContract menusContract = new MenusContract(getContext());

        //create menu for foodtruck if it does not exits
        if (!menusContract.checkIfMenuExists(currentFoodTruck.getM_ID())) {
            menusContract.createMenu(currentFoodTruck.getM_ID());
            menu = menusContract.getMenuByFoodTruckId(currentFoodTruck.getM_ID());
        }


        if (validateName(itemName) & validatePrice(itemPrice)) {

            if (imageView.getDrawable() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();
                ItemsContract ic = new ItemsContract(getContext());
                ic.createItem(itemName.getText().toString(), itemPrice.getText().toString(), itemAvailability.getSelectedItem().toString(), bitMapData, menu.getM_Id());
                return true;
            } else
                Snackbar.make(dV, "                      Please Upload an Image", Snackbar.LENGTH_LONG).show();

        }
        return false;
    }

    //add menu item to DB after user enters data and presses ADD
    private boolean updateItemInDatabase(Item item) {
        //references to all Dialog views
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);
        imageView = dV.findViewById(R.id.menuLoadImage);

        if (validateName(itemName) & validatePrice(itemPrice)) {
            //convert image to byte to be able to pass int food truck database
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            drawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitMapData = stream.toByteArray();

            ItemsContract ic = new ItemsContract(getContext());
            ic.updateItem(item.getM_Id(), itemName.getText().toString(), itemPrice.getText().toString(), itemAvailability.getSelectedItem().toString(), bitMapData);
            menuAdapter.submitList(getMenuList());

            return true;
        }
        return false;
    }

    //Validation For ItemName
    private boolean validateName(EditText itemName) {
        p = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);
        m = p.matcher(itemName.getText().toString());
        boolean cv = m.find();
        String name = itemName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            itemName.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            itemName.setError("Invalid Entry (Only Letters)");
            return false;
        }

        return true;
    }

    //Validation For ItemName
    private boolean validatePrice(EditText itemPrice) {
        p = Pattern.compile("(\\d+\\.\\d{2})", Pattern.CASE_INSENSITIVE);
        m = p.matcher(itemPrice.getText().toString());
        boolean cv = m.find();
        String price = itemPrice.getText().toString();

        if (TextUtils.isEmpty(price)) {
            itemPrice.setError("Can Not Be Empty ");
            return false;
        } else if (!cv) {
            itemPrice.setError("Invalid Entry (0.00)");
            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentFoodTruck = (FoodTruck) parent.getSelectedItem();
        Log.i("123: ", currentFoodTruck.getM_Name());
        menuAdapter.submitList(getMenuList());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}