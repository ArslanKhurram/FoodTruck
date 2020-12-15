package com.example.foodtruck.Fragments.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.CustomerMenuAdapter;
import com.example.foodtruck.Adapter.MenuAdapter;
import com.example.foodtruck.DataBase.CartOptionsContract;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FavoritesContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
        hsv = v.findViewById(R.id.scrlMenu);
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        tv.setVisibility(View.INVISIBLE);



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
            hsv.setVisibility(View.INVISIBLE);
        }

        recyclerView = v.findViewById(R.id.CustomerItemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext()); // LinearLayout for cards
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = cMenuAdapter;
        recyclerView.setAdapter(recyclerAdapter);


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
            for (Item item : ic.getItemListByMenuID(menu.getM_Id())) {
                if (item.getM_Available().equals("No")) {

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



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("Email", "");


        currentCustomer = cC.getCustomerIdByEmail(email);
        itemNameDb.setText(item.getM_Name());
        priceDb.setText("$" + item.getM_Price());
        spnQnty.getSelectedItem().toString();


        Boolean[] checkedOption = displayOptions(item);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dV)
                .setPositiveButton("Add to Cart", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button btnAdd = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnAdd.setOnClickListener(v -> {

            if (checkedOption != null && Arrays.asList(checkedOption).contains(true)) {
                addToCart(checkedOption, currentCustomer, item, spnQnty.getSelectedItem().toString());
            } else {
                addCartToDb(item);
            }
            //Clears checkout cart database but shouldnt be use yet until we forward the cart to foodtrucks
            // clearCheckoutDatabase();

            alertDialog.cancel();
        });
    }

    //Add Cart To CheckOut Cart Db
    private void addCartToDb(Item item) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("foodTruck", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Bundle bundle = getArguments();
        Long truckID = null;
        if (bundle != null) {
            truckID = bundle.getLong("mKey");
            editor.putLong("truck_Id", truckID);
            editor.commit();
        }

        cart.addCart(item.getM_Id(), spnQnty.getSelectedItem().toString(), currentCustomer.getM_Id(),23);
        Toast.makeText(getContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
    }

    //Empty Database
    private void clearCheckoutDatabase() {
        cart.clearTable(currentCustomer.getM_Id());
    }


    private Boolean[] displayOptions(Item item) {
        LinearLayout ll = dV.findViewById(R.id.checkBoxes);
        long optionId = item.getM_Id();

        OptionsContract oC = new OptionsContract(getContext());

        ArrayList<Option> optionRay;


        optionRay = oC.getOptionsListByItemID(optionId);
        if (optionRay != null) {

            Boolean[] checkedOptions = new Boolean[optionRay.size()];
            Arrays.fill(checkedOptions, false);

            CheckBox[] checkBoxes = new CheckBox[optionRay.size()];

            for (int i = 0; i < optionRay.size(); i++) {
                checkBoxes[i] = new CheckBox(getContext());
                checkBoxes[i].setText(optionRay.get(i).getM_Option());
                ll.addView(checkBoxes[i]);

                int finalI = i;
                checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBoxes[finalI].isChecked()) {
                            checkedOptions[finalI] = true;
                        } else if (checkBoxes[finalI].isChecked() == false) {
                            checkedOptions[finalI] = false;
                        }
                    }
                });
            }
            return checkedOptions;
        }

        return null;
    }

    private void addToCart(Boolean[] checkedOptions, Customer customer, Item item, String qty) {
        Random random = new Random();
        //long orderNumber = random.nextInt(1000);
        long orderNumber = getRandomNumberUsingNextInt(1000,9999);

        CheckOutContract checkOutContract = new CheckOutContract(getContext());
        checkOutContract.addCart(item.getM_Id(), qty, customer.getM_Id(), orderNumber);

        OptionsContract optionsContract = new OptionsContract(getContext());

        ArrayList<Option> optionRay = optionsContract.getOptionsListByItemID(item.getM_Id());

        Cart cart = checkOutContract.getCartByNumberId(orderNumber);

        CartOptionsContract cartOptionsContract = new CartOptionsContract(getContext());

        if (optionRay.size() != 0) {
            for (int i = 0; i < optionRay.size(); i++) {
                if (checkedOptions[i]) {//change entire db to cart instead of cust
                    cartOptionsContract.savedSelectedItemsOptions(cart.getM_ID(), item.getM_Id(), optionRay.get(i).getM_Id());
                }
            }
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("foodTruck", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Bundle bundle = getArguments();
        Long truckID = null;
        if (bundle != null) {
            truckID = bundle.getLong("mKey");
            editor.putLong("truck_Id", truckID);
            editor.commit();
        }

    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
