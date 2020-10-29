package com.example.foodtruck.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtruck.DataBase.AdminContract;
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
        customersContract.addCustomer("Bob", "Marley", "2", "2", "0", "0", "0", "0", "0", "0");
        customersContract.addCustomer("John", "Hopkins", "22", "", "0", "0", "0", "0", "0", "0");
        customersContract.addCustomer("Adam", "Jack", "33", "", "0", "0", "0", "0", "0", "0");
        Customer customer1 = customersContract.getCustomerIdByEmail("2");
        Customer customer2 = customersContract.getCustomerIdByEmail("22");
        Customer customer3 = customersContract.getCustomerIdByEmail("33");

        AdminContract adminContract = new AdminContract(this);
        adminContract.addAdmin("1", "1");

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

        ItemsContract itemsContract = new ItemsContract(this);
        itemsContract.createItem("Cheese Burger", "9.99", "Yes", image, menu.getM_Id());
        itemsContract.createItem("Apple Pie", "1.99", "Yes", image, menu.getM_Id());
        itemsContract.createItem("Hot Dog", "6.99", "Yes", image, menu.getM_Id());
        ArrayList<Item> itemArrayList = itemsContract.ItemsList(menu.getM_Id());

//        for (Item item : itemArrayList) {
//            Log.i("Test", "Item Name: " + item.getM_Name());
//            Log.i("Test", "Item Price: " + item.getM_Price());
//            Log.i("Test", "Item MenuID: " + item.getM_Menu().getM_Id());
//        }

        OrdersContract ordersContract = new OrdersContract(this);
        ordersContract.createOrder("A01", "10/22/2020", "Preparing", customer1.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("A02", "10/22/2020", "Completed", customer1.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("A03", "10/22/2020", "Preparing", customer2.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("B01", "10/22/2020", "Completed", customer2.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("B02", "10/22/2020", "Preparing", customer3.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("B03", "10/22/2020", "Completed", customer3.getM_Id(), vendor.getM_Id());
        Order order = ordersContract.getOrderById(1);
        Order order2 = ordersContract.getOrderById(2);

       OrderedItemsContract orderedItemsContract = new OrderedItemsContract(this);
        orderedItemsContract.addOrderedItem("1", itemArrayList.get(0).getM_Id(), order.getM_Id());
        orderedItemsContract.addOrderedItem("2", itemArrayList.get(1).getM_Id(), order.getM_Id());
        orderedItemsContract.addOrderedItem("3", itemArrayList.get(2).getM_Id(), order2.getM_Id());
        orderedItemsContract.addOrderedItem("3", itemArrayList.get(2).getM_Id(), order2.getM_Id());
        ArrayList<OrderedItem> orderedItemArrayList = orderedItemsContract.getOrderedItems(order.getM_Id());
/*
        for (OrderedItem orderedItem : orderedItemArrayList) {
            Log.i("Test", "Ordered Item Name: " + orderedItem.getM_Item().getM_Name());
            Log.i("Test", "Ordered Item Quantity: " + orderedItem.getM_Quantity());
            Log.i("Test", "Ordered Item Order ID: " + orderedItem.getM_Order().getM_Id());
        }
*/
    }


    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}