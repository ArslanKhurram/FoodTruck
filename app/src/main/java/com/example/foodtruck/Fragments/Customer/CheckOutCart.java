package com.example.foodtruck.Fragments.Customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.DataBase.CheckOutContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class CheckOutCart extends Fragment implements View.OnClickListener {

    CheckOutContract cart;
    CustomersContract cC;
    MenusContract m;
    ItemsContract ic;
    OptionsContract op;
    Item item;
    ArrayList<Option> options;
    Customer currentCustomer;
    TextView itemNameDb, priceDb ;
    CheckBox boxOne,boxTwo,boxThree;
    Spinner qnty;

    private LayoutInflater dialogInflater;
    View dv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_check_out_cart, container, false);


        cart = new CheckOutContract(getContext());
        cC = new CustomersContract(getContext());
        m = new MenusContract(getContext());
        ic = new ItemsContract(getContext());
        op = new OptionsContract(getContext());

        Button btn = v.findViewById(R.id.button12);
        btn.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        addToCartDialog();
    }

    private void addToCartDialog() {
        dialogInflater = getLayoutInflater();
        dv = dialogInflater.inflate(R.layout.dialog_checkout_cart, null);

        itemNameDb = dv.findViewById(R.id.itemNameDb);
        priceDb = dv.findViewById(R.id.priceDb);
        qnty = dv.findViewById(R.id.spnQnty);
        boxOne = dv.findViewById(R.id.boxOne);
        boxTwo = dv.findViewById(R.id.boxTwo);
        boxThree = dv.findViewById(R.id.boxThree);
        options = op.OptionsList(1);
        item = ic.getItemById(1);

        currentCustomer = cC.getCustomerById(1);



        itemNameDb.setText(item.getM_Name());
        priceDb.setText("$" + item.getM_Price());
        qnty.getSelectedItem().toString();

        boxOne.setText(options.get(0).getM_Option());
        boxTwo.setText(options.get(1).getM_Option());
        boxThree.setText(options.get(2).getM_Option());

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button btnB = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnB.setOnClickListener(view -> {

            addCartToDb();
            clearCheckoutDatabase();
            alertDialog.cancel();
         });

    }//end of addToCartDialog



    //Add Cart To CheckOut Cart Db
    private void addCartToDb(){
        cart.addCart(item.getM_Name(),item.getM_Price(),qnty.getSelectedItem().toString(),currentCustomer.getM_Id());
        }

    //Empty Database
    private void clearCheckoutDatabase(){
        cart.clearTable(1);
    }
}
