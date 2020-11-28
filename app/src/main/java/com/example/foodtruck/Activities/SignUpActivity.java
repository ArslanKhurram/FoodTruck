package com.example.foodtruck.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    public Customer customer = new Customer();
    public Vendor vendor = new Vendor();
    public Admin admin = new Admin();

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //open login fragment on start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
        }
        CustomersContract customersContract = new CustomersContract(this);


        if (customersContract.checkForEmptyTable()) {
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

            FoodTrucksContract foodTrucksContract = new FoodTrucksContract(this);
            foodTrucksContract.createFoodTruck("Hot Indian Tacos", "Mexican", picture(R.drawable.foodtruck), 10.5, 10.5, vendor.getM_Id());
            foodTrucksContract.createFoodTruck("Kono Pizza", "Italian", picture(R.drawable.foodtruck1), 10.5, 10.5, vendor.getM_Id());
            FoodTruck foodTruck1 = foodTrucksContract.getFoodTruckByVendorId(1);


            if (foodTruck1 != null) {
                MenusContract menusContract = new MenusContract(this);
                menusContract.createMenu(foodTruck1.getM_ID());
                Menu menu = menusContract.getMenuByFoodTruckId(foodTruck1.getM_ID());

                ItemsContract itemsContract = new ItemsContract(this);
                itemsContract.createItem("Cheese Burger", "9.99", "Yes", picture(R.drawable.cheeseburger), menu.getM_Id());
                itemsContract.createItem("Apple Pie", "1.99", "Yes", picture(R.drawable.applepie), menu.getM_Id());
                itemsContract.createItem("Hot Dog", "6.99", "Yes", picture(R.drawable.hotdog), menu.getM_Id());
                ArrayList<Item> itemArrayList = itemsContract.getItemListByMenuID(menu.getM_Id());

                OptionsContract oc = new OptionsContract(this);
                if (itemArrayList != null) {
                    for (Item i : itemArrayList) {
                        oc.createOption("Something", i.getM_Id());
                        oc.createOption("Something", i.getM_Id());
                        oc.createOption("Something", i.getM_Id());
                    }
                }

                OrdersContract ordersContract = new OrdersContract(this);
                ordersContract.createOrder("A01", "10/22/2020", "Preparing", customer1.getM_Id(), foodTruck1.getM_ID());
                ordersContract.createOrder("A03", "10/22/2020", "Preparing", customer2.getM_Id(), foodTruck1.getM_ID());
                ordersContract.createOrder("B03", "10/22/2020", "Completed", customer3.getM_Id(), foodTruck1.getM_ID());
                ArrayList<Order> orderArrayList = ordersContract.getOrdersList(foodTruck1.getM_ID());

                OrderedItemsContract orderedItemsContract = new OrderedItemsContract(this);

                if (itemArrayList != null && orderArrayList != null) {

                    for (int i = 0; i < orderArrayList.size(); i++) {
                        orderedItemsContract.addOrderedItem("1", itemArrayList.get(1).getM_Id(), orderArrayList.get(i).getM_Id());
                        orderedItemsContract.addOrderedItem("2", itemArrayList.get(2).getM_Id(), orderArrayList.get(i).getM_Id());
                    }
                }
            }
        }
    }

    private byte[] picture(int id) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}