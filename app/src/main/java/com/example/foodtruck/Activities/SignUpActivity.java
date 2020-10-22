package com.example.foodtruck.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    public Customer customer = new Customer();
    public Vendor vendor = new Vendor();
    public Admin admin = new Admin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //open login fragment on start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }

        CustomersContract customersContract = new CustomersContract(this);
        customersContract.addCustomer("A", "K", "2", "2", "0", "0", "0", "0", "0", "0");
        Customer customer = customersContract.getCustomerIdByEmail("2");

        VendorsContract vendorsContract = new VendorsContract(this);
        vendorsContract.addVendor("J", "C", "3", "3", "0", "0", "0", "0", "0", "0");
        Vendor vendor = vendorsContract.getVendorIdByEmail("3");

        byte[] image = new byte[]{};
        FoodTrucksContract foodTrucksContract = new FoodTrucksContract(this);
        foodTrucksContract.createFoodTruck("Test", "Italian", image, 10.5, 10.5, vendor.getM_Id());
        FoodTruck foodTruck = foodTrucksContract.getFoodTruckByVendorId(vendor.getM_Id());

        MenusContract menusContract = new MenusContract(this);
        menusContract.createMenu(foodTruck.getM_ID());
        Menu menu = menusContract.getMenuByFoodTruckId(foodTruck.getM_ID());

        /*FIXME: Fix adding ordered items

            Not sure how to fix the error yet.

            DESCRIPTION:
            App compiles when executing first addOrderedItem
            App crashed when executing both first and second addOrderedItem on a clean database with no previous entries
                There is a cursor error in multiple files when adding an ordered item with the 2nd item

         */

        ItemsContract itemsContract = new ItemsContract(this);
        itemsContract.createItem("test", "9.99", "Yes", image, menu.getM_Id());
        itemsContract.createItem("test2", "1.99", "No", image, menu.getM_Id());
        ArrayList<Item> itemArrayList = itemsContract.ItemsList(menu.getM_Id());

        for (Item item : itemArrayList) {
            Log.i("Test", "Item Name: " + item.getM_Name());
            Log.i("Test", "Item Price: " + item.getM_Price());
            Log.i("Test", "Item MenuID: " + item.getM_Menu().getM_Id());
        }

        OrdersContract ordersContract = new OrdersContract(this);
        ordersContract.createOrder("A01", "10/22/2020", "Preparing", customer.getM_Id(), vendor.getM_Id());
        Order order = ordersContract.getOrderById(1);

        OrderedItemsContract orderedItemsContract = new OrderedItemsContract(this);
        orderedItemsContract.addOrderedItem("1", itemArrayList.get(0).getM_Id(), order.getM_Id());
        orderedItemsContract.addOrderedItem("2", itemArrayList.get(1).getM_Id(), order.getM_Id()); //error happens when running this line
        ArrayList<OrderedItem> orderedItemArrayList = orderedItemsContract.getOrderedItems(order.getM_Id());

        for (OrderedItem orderedItem : orderedItemArrayList) {
            Log.i("Test", "Ordered Item Name: " + orderedItem.getM_Item().getM_Name());
            Log.i("Test", "Ordered Item Quantity: " + orderedItem.getM_Quantity());
            Log.i("Test", "Ordered Item Order ID: " + orderedItem.getM_Order().getM_Id());
        }

    }


    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}