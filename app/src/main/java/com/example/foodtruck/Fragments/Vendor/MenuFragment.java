package com.example.foodtruck.Fragments.Vendor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private MenuAdapter menuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> itemList = new ArrayList<>();
    private EditText itemName, itemPrice;
    private TextView tv;
    private Spinner itemAvailability;
    private SharedPreferences sharedPref;
    private LayoutInflater dialogInflater;
    private Menu menu;
    View dV;
    private Pattern p;
    private Matcher m;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        FloatingActionButton btnAddMenuItem = v.findViewById(R.id.btnAddItem);
        btnAddMenuItem.setOnClickListener(this);
        tv = v.findViewById(R.id.noMenuPrompt);

        sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);

        menuAdapter = new MenuAdapter(getContext(), this);
        menuAdapter.submitList(getMenuList());
        tv.setVisibility(View.INVISIBLE);

        if (itemList.size() < 1)
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
                if (itemList.size() < 1)
                    tv.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        return v;
    }

    public ArrayList<Item> getMenuList() {
        String email = sharedPref.getString("Email", "");

        VendorsContract vc = new VendorsContract(getContext());
        MenusContract mc = new MenusContract(getContext());
        ItemsContract ic = new ItemsContract(getContext());
        FoodTrucksContract fc = new FoodTrucksContract(getContext());

        //get vendor id
        Vendor vendor = vc.getVendorIdByEmail(email);
        //get FoodTruck by vendor id
        FoodTruck foodTruck = fc.getFoodTruckByVendorId(vendor.getM_Id());
        //get menu id from foodTruck
        if (foodTruck != null) {
            menu = mc.getMenuByFoodTruckId(foodTruck.getM_ID());
            itemList = ic.getItemListByMenuID(menu.getM_Id());
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

    private void addItemDialog() {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_addmenu_item, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

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

    private void editItemDialog(Item item) {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_addmenu_item, null);
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);

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

        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(v -> {
            if (updateItemInDatabase(item)) {
                menuAdapter.submitList(getMenuList());
                alertDialog.cancel();
                Toast.makeText(getContext(), "Menu Item Updated", Toast.LENGTH_LONG).show();
                tv.setVisibility(View.INVISIBLE);
            }
        });
    }

    //add menu item to DB after user enters data and presses ADD
    private boolean addItemToDatabase() {
        //references to all Dialog views
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);

        if (validateName(itemName) & validatePrice(itemPrice)) {
            //convert image to byte to be able to pass int food truck database
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.test, null);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitMapData = stream.toByteArray();

            ItemsContract ic = new ItemsContract(getContext());
            ic.createItem(itemName.getText().toString(), itemPrice.getText().toString(), itemAvailability.getSelectedItem().toString(), bitMapData, menu.getM_Id());
            return true;
        }
        return false;
    }

    //add menu item to DB after user enters data and presses ADD
    private boolean updateItemInDatabase(Item item) {
        //references to all Dialog views
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemAvailability = dV.findViewById(R.id.dl_spinnerAvailability);

        if (validateName(itemName) & validatePrice(itemPrice)) {
            //convert image to byte to be able to pass int food truck database
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.test, null);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitMapData = stream.toByteArray();

            ItemsContract ic = new ItemsContract(getContext());
            ic.updateItem(item.getM_Id(), itemName.getText().toString(), itemPrice.getText().toString(), itemAvailability.getSelectedItem().toString(), bitMapData);
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
}