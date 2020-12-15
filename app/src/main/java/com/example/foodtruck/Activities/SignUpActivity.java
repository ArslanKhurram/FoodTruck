package com.example.foodtruck.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.InvoiceContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.OrderedItemOptionsContract;
import com.example.foodtruck.DataBase.OrderedItemsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.Customer.CartFragment;
import com.example.foodtruck.Fragments.Customer.CustomerAccountFragment;
import com.example.foodtruck.Fragments.Customer.CustomerMainFragment;
import com.example.foodtruck.Fragments.Customer.FavoritesFragment;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Fragments.SearchFragment;
import com.example.foodtruck.Fragments.Vendor.MenuFragment;
import com.example.foodtruck.Fragments.Vendor.VendorMainFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    public Customer customer = new Customer();
    public Vendor vendor = new Vendor();
    public Admin admin = new Admin();
    public static String currentFragment = null;
    public static View currentFragmentView = null;

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
            vendorsContract.addVendor("J", "C", "4", "4", "0", "0", "0", "0", "0", "0");
            Vendor vendor = vendorsContract.getVendorIdByEmail("3");
            Vendor vendor2 = vendorsContract.getVendorIdByEmail("4");

            FoodTrucksContract foodTrucksContract = new FoodTrucksContract(this);
            foodTrucksContract.createFoodTruck("Hot Indian Tacos", "Mexican", picture(R.drawable.foodtruck), 40.8, -73.2, vendor.getM_Id());
            foodTrucksContract.createFoodTruck("Kono Pizza", "Italian", picture(R.drawable.foodtruck1), 40.7, -73.2, vendor.getM_Id());
            foodTrucksContract.createFoodTruck("Nacho Town", "Italian", picture(R.drawable.foodtruck1), 40.7, -73.3, vendor2.getM_Id());
            foodTrucksContract.createFoodTruck("Pizza Town", "Italian", picture(R.drawable.foodtruck1), 40.7, -73.4, vendor2.getM_Id());
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
                oc.createOption("Cheese", 1); //burger
                oc.createOption("Cream", 2); //pie
                oc.createOption("Ketchup", 3); //hotdog
                oc.createOption("Berries", 2); //pie
                oc.createOption("Mustard", 3); //hotdog
                oc.createOption("Beans", 3); //hotdog

                OrdersContract ordersContract = new OrdersContract(this);
                ordersContract.createOrder("A01", "10/22/2020", "Preparing", customer1.getM_Id(), foodTruck1.getM_ID());
                ordersContract.createOrder("A03", "10/22/2020", "Preparing", customer2.getM_Id(), foodTruck1.getM_ID());
                ordersContract.createOrder("B03", "10/22/2020", "Completed", customer3.getM_Id(), foodTruck1.getM_ID());
                ArrayList<Order> orderArrayList = ordersContract.getOrdersList(foodTruck1.getM_ID());

                OrderedItemsContract orderedItemsContract = new OrderedItemsContract(this);
                OrderedItemOptionsContract orderedItemOptionsContract = new OrderedItemOptionsContract(this);

                if (itemArrayList != null && orderArrayList != null) {

                    for (int i = 0; i < orderArrayList.size(); i++) {
                        orderedItemsContract.addOrderedItem("1", itemArrayList.get(1).getM_Id(), orderArrayList.get(i).getM_Id());
                        orderedItemsContract.addOrderedItem("2", itemArrayList.get(2).getM_Id(), orderArrayList.get(i).getM_Id());
                    }
                }

                //adding options to orders
                orderedItemOptionsContract.addOrderedItemOptions(2, 2, 1); //pie
                orderedItemOptionsContract.addOrderedItemOptions(4, 2, 1); //pie
                orderedItemOptionsContract.addOrderedItemOptions(3, 3, 2); //hotdog
                orderedItemOptionsContract.addOrderedItemOptions(4, 2, 3); //pie
                orderedItemOptionsContract.addOrderedItemOptions(5, 3, 4); //hotdog
                orderedItemOptionsContract.addOrderedItemOptions(6, 3, 4); //hotdog
                orderedItemOptionsContract.addOrderedItemOptions(2, 2, 5); //pie
                orderedItemOptionsContract.addOrderedItemOptions(4, 2, 5); //pie
                orderedItemOptionsContract.addOrderedItemOptions(6, 3, 6); //hotdog

                //payment and invoice Test code
                PaymentsContract paymentsContract = new PaymentsContract(this);
                paymentsContract.createPayment("Debit", "Bob", "1283902093848639","12/2023", "453", "12/7/2020", 1);
                InvoiceContract invoiceContract = new InvoiceContract(this);
                invoiceContract.createInvoice("12/7/2020", "19.99", "2.99", "2.99", "25.97", 1, 1, 1);
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
        displayPreviousFragment(currentFragment, currentFragmentView);
    }

    private void displayPreviousFragment(String currentFragment, View view) {
        //create fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        if (currentFragment != null) {
            switch (currentFragment) {
                case "Fragment_VendorMain":
                    fragment = new VendorMainFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new MenuFragment()).commit();


                    break;
                case "Fragment_CustomerMain":
                    fragment = new CustomerMainFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new SearchFragment()).commit();
                    break;
                case "SignOut":
                    fragment = new LoginFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        }

        //replacing the fragement
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_container, fragment);
//            ft.commit();
//        }
    }
}