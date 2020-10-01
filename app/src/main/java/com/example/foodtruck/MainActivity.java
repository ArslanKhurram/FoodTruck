package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.models.Customer;
import com.example.foodtruck.models.FoodTruck;
import com.example.foodtruck.models.Item;
import com.example.foodtruck.models.Menu;
import com.example.foodtruck.models.Vendor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        final TextView tv1 = findViewById(R.id.textView1);
        final ImageView image = findViewById(R.id.myImg);

        b.setOnClickListener(new View.OnClickListener() { //on click listener for button
            @Override
            public void onClick(View view) {

                //code to convert test image to byte[] for items constructor
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.test, null);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                //Test Customer Table
                CustomersContract cc = new CustomersContract(MainActivity.this);
                cc.addCustomer("Arslan", "Adam", "khura6@farmingdale.edu", "ILovePie","631-123-4567", "Apple St","1234","11777","Adams Town","PA");

                //Test Vendor Table
                VendorsContract vc = new VendorsContract(MainActivity.this);
                vc.addVendor("Mike","Pear","mpwar@gmail.com", "MikeIstheBest1234","516-920-0202","Maple Ave","987","10393","Apple Village","VA")  ;
                vc.addVendor("John","Deer","jdeer@gmail.com", "JohnIstheBest1234","516-920-0202","Chap Ave","314","10398","Down Town","NY")  ;

                //Get Vendor Objects by email for menu table
                Vendor ven,ven1;
                ven = vc.getVendorIdByEmail("mpwar@gmail.com");
                ven1 = vc.getVendorIdByEmail("jdeer@gmail.com");

                //Get Customer object for payment table
                Customer cus;
                cus = cc.getCustomerIdByEmail("khura6@farmingdale.edu");

                //Test Food Truck table
                FoodTrucksContract fc = new FoodTrucksContract(MainActivity.this);
                fc.createFoodTruck("Mike's Pizza", "Italian", bitMapData, ven.getM_Id());
                fc.createFoodTruck("Mike's Tacos", "Mexican", bitMapData, ven.getM_Id());

                //Get Food Truck object from table
                FoodTruck foodTruck = fc.getFoodTruckByVendorId(ven.getM_Id());

                //Test Payment table
                PaymentsContract pc = new PaymentsContract(MainActivity.this);
                pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20", cus.getM_Id());

                //Test Menu table
                MenusContract mc = new MenusContract(MainActivity.this);
                mc.createMenu(ven.getM_Id());
                //mc.createMenu(ven1.getM_Id());

                //Get Menu id by vendor ID
                Menu menu = mc.getMenuByFoodTruckId(foodTruck.getM_ID());

                //Test Items Table
                ItemsContract ic = new ItemsContract(MainActivity.this);
                ic.createItem("Cheese Pizza", "9.99", "Y", bitMapData ,menu.getM_Id());
                ic.createItem("Pancake", "3.99", "Y", bitMapData ,menu.getM_Id());

                //set ArrayList to items list
                ArrayList<Item> list = ic.ItemsList(menu.getM_Id());

                //output list to screen
                for (int i=0; i<list.size(); i++)
                    tv1.append(list.get(i).toString() + "\n");

                //output image
                byte[] blob = list.get(0).getM_Image();
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                image.setImageBitmap(bmp);

                //set ArrayList to food trucks list
                ArrayList<FoodTruck> truckArrayList = fc.FoodTruckList(ven.getM_Id());

                //output list to screen
                for (int i=0; i<truckArrayList.size(); i++)
                    tv1.append(truckArrayList.get(i).toString() + "\n");

            }
        });
    }
}