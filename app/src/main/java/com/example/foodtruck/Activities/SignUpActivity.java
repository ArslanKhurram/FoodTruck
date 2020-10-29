package com.example.foodtruck.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
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
        customersContract.addCustomer("A", "K", "2", "2", "0", "0", "0", "0", "0", "0");
        Customer customer = customersContract.getCustomerIdByEmail("2");

        AdminContract adminContract = new AdminContract(this);
        adminContract.addAdmin("1", "1");

        VendorsContract vendorsContract = new VendorsContract(this);
        vendorsContract.addVendor("J", "C", "3", "3", "0", "0", "0", "0", "0", "0");
        Vendor vendor = vendorsContract.getVendorIdByEmail("3");

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.foodtruck, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.foodtruck1, null);
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData1 = stream.toByteArray();

        FoodTrucksContract foodTrucksContract = new FoodTrucksContract(this);
        foodTrucksContract.createFoodTruck("Hot Indian Tacos", "Mexican", bitMapData, 10.5, 10.5, vendor.getM_Id());
        foodTrucksContract.createFoodTruck("Kono Pizza", "Italian", bitMapData1, 10.5, 10.5, vendor.getM_Id());
        FoodTruck foodTruck1 = foodTrucksContract.getFoodTruckByVendorId(1);

        MenusContract menusContract = new MenusContract(this);
        menusContract.createMenu(foodTruck1.getM_ID());
        Menu menu = menusContract.getMenuByFoodTruckId(foodTruck1.getM_ID());

        ItemsContract itemsContract = new ItemsContract(this);
        itemsContract.createItem("test", "9.99", "Yes", bitMapData, menu.getM_Id());
        itemsContract.createItem("test2", "1.99", "No", bitMapData, menu.getM_Id());
        itemsContract.createItem("test3", "6.99", "Yes", bitMapData, menu.getM_Id());
        ArrayList<Item> itemArrayList = itemsContract.getItemListByMenuID(menu.getM_Id());

        OptionsContract oc = new OptionsContract(this);
        if (itemArrayList != null) {
            for (Item i : itemArrayList) {
                oc.createOption("Something", i.getM_Id());
                oc.createOption("Something", i.getM_Id());
                oc.createOption("Something", i.getM_Id());
            }
        }
       /* for (Item item : itemArrayList) {
            Log.i("Test", "Item Name: " + item.getM_Name());
            Log.i("Test", "Item Price: " + item.getM_Price());
            Log.i("Test", "Item MenuID: " + item.getM_Menu().getM_Id());
        }

        OrdersContract ordersContract = new OrdersContract(this);
        ordersContract.createOrder("A01", "10/22/2020", "Preparing", customer.getM_Id(), vendor.getM_Id());
        ordersContract.createOrder("B01", "10/22/2020", "Preparing", customer.getM_Id(), vendor.getM_Id());
        Order order = ordersContract.getOrderById(1);
        Order order2 = ordersContract.getOrderById(2);

        OrderedItemsContract orderedItemsContract = new OrderedItemsContract(this);
        orderedItemsContract.addOrderedItem("1", itemArrayList.get(0).getM_Id(), order.getM_Id());
        orderedItemsContract.addOrderedItem("2", itemArrayList.get(1).getM_Id(), order.getM_Id());
        orderedItemsContract.addOrderedItem("3", itemArrayList.get(2).getM_Id(), order2.getM_Id());
        orderedItemsContract.addOrderedItem("3", itemArrayList.get(2).getM_Id(), order2.getM_Id());
        ArrayList<OrderedItem> orderedItemArrayList = orderedItemsContract.getOrderedItems(order.getM_Id());

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