package com.example.foodtruck.Fragments.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.Adapter.CustomerMenuAdapter;
import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FavoritesContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuCustomerViewFragment extends Fragment implements MenuAdapter.OnItemListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private CustomerMenuAdapter cMenuAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> itemList = new ArrayList<>();
    private EditText itemName, itemPrice;
    private TextView tv, itemNameDb, priceDb;
    private LayoutInflater dialogInflater;
    private Menu menu;
    View dV;
    private Spinner spnQnty;
    private CheckOutContract cart;
    private ArrayList<Option> arrayCb = new ArrayList<>();
    private String selectedOptions = "";

    //hardcoded
    private Customer currentCustomer;
    private CustomersContract cC;
    private Button btnSave;

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_customerview, container, false);

        Bundle bundle = getArguments();
        Long truckID = null;
        if (bundle != null)
            truckID = bundle.getLong("mKey");
        tv = v.findViewById(R.id.noMenuPrompt);
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        tv.setVisibility(View.INVISIBLE);
        TextView truckName = v.findViewById(R.id.foodTruckName);

        FoodTrucksContract fc = new FoodTrucksContract(getContext());
        FoodTruck foodTruck = fc.getFoodTruckById(truckID);
        truckName.setText(foodTruck.getM_Name() + " Menu");

        if (checkFavorited()) {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable favorite = getContext().getResources().getDrawable(R.drawable.ic_favorite, null);
            favorite.setBounds(0, 0, 70, 70);
            btnSave.setCompoundDrawables(favorite, null, null, null);
            btnSave.setTextColor(Color.RED);
        }

        cMenuAdapter = new CustomerMenuAdapter(getContext(), this::onItemClick);
        if (getMenuList(truckID) != null)
            cMenuAdapter.submitList(getMenuList(truckID));
        else {
            tv.setVisibility(View.VISIBLE);
        }

        recyclerView = v.findViewById(R.id.CustomerItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); // LinearLayout for cards
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = cMenuAdapter;
        recyclerView.setAdapter(recyclerAdapter);

        //hardcoded
        cart = new CheckOutContract(getContext());
        cC = new CustomersContract(getContext());

        return v;
    }

    public ArrayList<Item> getMenuList(Long ft) {
        MenusContract mc = new MenusContract(getContext());
        ItemsContract ic = new ItemsContract(getContext());

        // Menu id from clicked foodTruck, compile list of items associated in that menu
        menu = mc.getMenuByFoodTruckId(ft);
        if (menu != null) {
            for(Item item : ic.getItemListByMenuID(menu.getM_Id())) {
                if(item.getM_Available().equals("No")) {

                }
            }
            itemList = ic.getItemListByMenuID(menu.getM_Id());
            return itemList;
        } else
            return null;
    }

    @Override
    public void onItemClick(int position) {
        Item itemPos = cMenuAdapter.getItemAt(position);
        itemOptionsDialog(itemPos);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
            String email = sharedPref.getString("Email", "");
            String userType = sharedPref.getString("UserType", "");

            if (userType.equals("Customer") && !checkFavorited()) {
                CustomersContract customersContract = new CustomersContract(getContext());
                Customer customer = customersContract.getCustomerIdByEmail(email);

                Bundle bundle = getArguments();
                long foodtruckID = bundle.getLong("mKey");

                FavoritesContract favoritesContract = new FavoritesContract(getContext());
                favoritesContract.createEntry(foodtruckID, customer.getM_Id());

                Drawable favorite = getContext().getResources().getDrawable(R.drawable.ic_favorite, null);
                favorite.setBounds(0, 0, 50, 50);
                btnSave.setCompoundDrawables(favorite, null, null, null);
                btnSave.setTextColor(Color.RED);
            } else if (checkFavorited()) {
                unFavorite();
            }

        }
    }

    private boolean checkFavorited() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = sharedPref.getString("Email", "");
        String userType = sharedPref.getString("UserType", "");
        Bundle bundle = getArguments();
        long foodtruckID = bundle.getLong("mKey");


        if (userType.equals("Customer")) {
            CustomersContract customersContract = new CustomersContract(getContext());
            Customer customer = customersContract.getCustomerIdByEmail(email);
            FavoritesContract favoritesContract = new FavoritesContract(getContext());
            if (favoritesContract.checkIfFavoritesExist(customer.getM_Id())) {
                for (FoodTruck foodtruck : favoritesContract.getSavedFoodTrucks(customer.getM_Id())) {
                    if (foodtruckID == foodtruck.getM_ID()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void unFavorite() {
        Bundle bundle = getArguments();
        long foodtruckID = bundle.getLong("mKey");

        FavoritesContract favoritesContract = new FavoritesContract(getContext());
        favoritesContract.deleteFavorite(foodtruckID);

        Drawable favorite = getContext().getResources().getDrawable(R.drawable.ic_notfavorite, null);
        favorite.setBounds(0, 0, 50, 50);
        btnSave.setCompoundDrawables(favorite, null, null, null);
        btnSave.setTextColor(Color.BLACK);
    }

    private void itemOptionsDialog(Item item) {
        dialogInflater = getLayoutInflater();
        dV = dialogInflater.inflate(R.layout.dialog_checkout_cart, null);
        itemName = dV.findViewById(R.id.dl_itemName);
        itemPrice = dV.findViewById(R.id.dl_itemPrice);
        itemNameDb = dV.findViewById(R.id.itemNameDb);
        priceDb = dV.findViewById(R.id.priceDb);
        spnQnty = dV.findViewById(R.id.spnQnty);


        //hardcoded
        currentCustomer = cC.getCustomerById(1);
        itemNameDb.setText(item.getM_Name());
        priceDb.setText("$" + item.getM_Price());
        spnQnty.getSelectedItem().toString();

        //Dynamically Displays Checkboxes & Pulls options from database
        arrayOptionsUpdated(item);


        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton("Add to Cart", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button btnAdd = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnAdd.setOnClickListener(v -> {

            addCartToDb(item);
            //Clears checkout cart database but shouldnt be use yet until we forward the cart to foodtrucks
           // clearCheckoutDatabase();

            alertDialog.cancel();
        });
    }//end itemOptionDialog

    //Add Cart To CheckOut Cart Db
    private void addCartToDb(Item item) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("foodTruck",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Bundle bundle = getArguments();
        Long truckID = null;
        if (bundle != null) {
            truckID = bundle.getLong("mKey");
            editor.putLong("truck_Id",truckID);
            editor.commit();
        }
        cart.addCart(item.getM_Id(), spnQnty.getSelectedItem().toString(), currentCustomer.getM_Id(),selectedOptions);
        Toast.makeText(getContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
       //Temporary Fix, if this code is not emplace the previous selection will stack on to the newly added items
        selectedOptions ="";
    }

    //Empty Database
    private void clearCheckoutDatabase() {
        cart.clearTable(1);
    }

    //obtains option from database and display them in a dynamic checkbox
    private void arrayOptionsUpdated(Item item) {
        LinearLayout ll = dV.findViewById(R.id.checkBoxes);
        Long optionId = item.getM_Id();
        ArrayList<Option> selectedOption;
        OptionsContract oc = new OptionsContract(getContext());
        selectedOption = oc.getOptionsListByItemID(optionId);
        selectedOption.get(0).getM_Option();
        CheckBox[] cb = new CheckBox[selectedOption.size()];

        for (int i = 0; i < selectedOption.size(); i++) {
            cb[i] = new CheckBox(getContext());
            cb[i].setText(selectedOption.get(i).getM_Option());
            int finalI = i;

            cb[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb[finalI].isChecked()) {
                        selectedOptions += (selectedOption.get(finalI).getM_Option() + " ");
                    }
                }
            });
            ll.addView(cb[i]);
        }

    }

}