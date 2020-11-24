package com.example.foodtruck.Fragments.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.CompoundButton;
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

import com.example.foodtruck.Adapter.CustomerMenuAdapter;
import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
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
    private HorizontalScrollView hsv;
    private LayoutInflater dialogInflater;
    private Menu menu;
    View dV;
    private Spinner spnQnty;
    private CheckOutContract cart;
    private ArrayList<Option> arrayCb = new ArrayList<>();
    private String selectedOptions ="";

    //hardcoded
    private Customer currentCustomer;
    private CustomersContract cC;

    public MenuCustomerViewFragment() {

    }

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_customerview, container, false);
        Bundle bundle = getArguments();
        Long truckID = null;
        if (bundle != null)
            truckID = bundle.getLong("mKey");
        tv = v.findViewById(R.id.noMenuPrompt);
        hsv = v.findViewById(R.id.scrlMenu);
        tv.setVisibility(View.INVISIBLE);
        cMenuAdapter = new CustomerMenuAdapter(getContext(), this::onItemClick);
        if (getMenuList(truckID) != null)
            cMenuAdapter.submitList(getMenuList(truckID));
        else {
            tv.setVisibility(View.VISIBLE);
            hsv.setVisibility(View.INVISIBLE);
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
            itemList = ic.getItemListByMenuID(menu.getM_Id());
            return itemList;
        } else
            return null;
    }

    @Override
    public void onItemClick(int position) {
        Item itemPos = cMenuAdapter.getItemAt(position);
        openOptions(itemPos);

    }

    @Override
    public void onClick(View view) {

    }

    // TODO: Add dialog code for item's options from Bryan's branch
    private void openOptions(Item i) {
        itemOptionsDialog(i);

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
    private void  arrayOptionsUpdated(Item item) {
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
                    if(cb[finalI].isChecked()){
                        selectedOptions += (selectedOption.get(finalI).getM_Option() + " ");
                    }
                }
            });
            ll.addView(cb[i]);
        }

    }

}