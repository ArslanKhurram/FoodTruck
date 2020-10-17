package com.example.foodtruck.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.OptionsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.LoginFragment;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;

public class SignUpActivity extends AppCompatActivity {

    public Customer customer = new Customer();
    public Vendor vendor = new Vendor();

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
        cc.addCustomer("Arslan", "Khurram", "marslan@gmail.com", "hfjsdhfls", "584-445-4434", "Eastwood Ave","44","11703","Deer Lawn","NY");
        cc.addCustomer("Tyler", "Homes", "tyler@gmail.com", "kjhgfverg4534", "584-445-4434", "Noble Ave","323","11704","Bear Lawn","NY");

        //add vendor
        VendorsContract vc = new VendorsContract(this);
        vc.addVendor("James","Dillion","abc@gmail.com","abc@123", "585-545-4444","Mohawk Dr","45","Dix Hills","11756","NY");

        byte[] image = new byte[]{};
        //add Food Truck
        FoodTrucksContract fc = new FoodTrucksContract(this);
        fc.createFoodTruck("J1 Pizza","Pizza", image , 34.55, 43.44, vc.getVendorIdByEmail("abc@gmail.com").getM_Id());

        //add menu
        MenusContract mc = new MenusContract(this);
        mc.createMenu(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("abc@gmail.com").getM_Id()).getM_ID());

        //add items
        ItemsContract ic = new ItemsContract(this);
        ic.createItem("Pizza Roll","3.99","Y",image, mc.getMenuByFoodTruckId(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("abc@gmail.com").getM_Id()).getM_ID()).getM_Id());
        ic.createItem("BBQ Pizza","5.99","Y",image, mc.getMenuByFoodTruckId(fc.getFoodTruckByVendorId(vc.getVendorIdByEmail("abc@gmail.com").getM_Id()).getM_ID()).getM_Id());

        }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();

    }
}