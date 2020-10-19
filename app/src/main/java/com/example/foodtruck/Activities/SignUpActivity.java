package com.example.foodtruck.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.OrdersContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.Order;
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

        //add customer
        CustomersContract cc = new CustomersContract(this);
        cc.addCustomer("Arslan", "Khurram", "2", "2", "584-445-4434", "Eastwood Ave", "44", "11703", "Deer Lawn", "NY");
        //cc.addCustomer("Tyler", "Homes", "tyler@gmail.com", "kjhgfverg4534", "584-445-4434", "Noble Ave", "323", "11704", "Bear Lawn", "NY");

        //add payment
        PaymentsContract pc = new PaymentsContract(this);
        pc.createPayment("Debit", "Arslan Khurram", "5453253253253515", "10/2023", "391", "10/16/2020", cc.getCustomerIdByEmail("2").getM_Id());
        //pc.createPayment("Credit","Arslan Khurram","7592018573928","10/21","625","10/16/2020",cc.getCustomerIdByEmail("").getM_Id());
        //pc.createPayment("Credit","Arslan Khurram","0563921740674","10/22","026","10/16/2020",cc.getCustomerIdByEmail("").getM_Id());

        //add vendor
        VendorsContract vc = new VendorsContract(this);
        vc.addVendor("James", "Dillion", "3", "3", "585-545-4444", "Mohawk Dr", "45", "Dix Hills", "11756", "NY");

        byte[] image = new byte[]{};
        //add Food Truck
        FoodTrucksContract fc = new FoodTrucksContract(this);
        fc.createFoodTruck("J1 Pizza", "Pizza", image, 34.55, 43.44, vc.getVendorIdByEmail("3").getM_Id());

        //add order
        OrdersContract oc = new OrdersContract(this);
        oc.createOrder("001", "10/15/2020", "Preparing", cc.getCustomerIdByEmail("2").getM_Id(), vc.getVendorIdByEmail("3").getM_Id());
        // oc.createOrder("002", "10/15/2020", "Preparing", cc.getCustomerIdByEmail("tyler@gmail.com").getM_Id(), vc.getVendorIdByEmail("jDillion@yahoo.com").getM_Id());

        //add menu
        MenusContract mc = new MenusContract(this);
        mc.createMenu(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("3").getM_Id()).getM_ID());

        //add items
        ItemsContract ic = new ItemsContract(this);
        ic.createItem("Pizza Roll", "3.99", "Yes", image, mc.getMenuByFoodTruckId(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("3").getM_Id()).getM_ID()).getM_Id());
        ic.createItem("BBQ Pizza", "5.99", "No", image, mc.getMenuByFoodTruckId(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("3").getM_Id()).getM_ID()).getM_Id());

        //add options
        OptionsContract opc = new OptionsContract(this);
//        opc.createOption("Hot Sauce", ic.getItemById(1).getM_Id());
        //       opc.createOption("Buffalo Sauce", ic.getItemById(1).getM_Id());
    }


    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}