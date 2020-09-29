package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodtruck.DataBase.ItemsContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.PaymentsContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.models.Customer;
import com.example.foodtruck.models.Menu;
import com.example.foodtruck.models.Vendor;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() { //on click listener for button
            @Override
            public void onClick(View view) {

                //Test Customer Table
                CustomersContract cc = new CustomersContract(MainActivity.this);
                cc.addCustomer("Arslan", "Adam", "khura6@farmingdale.edu", "ILovePie","631-123-4567", "Apple St","1234","11777","Adams Town","PA");

                //Test Vendor Table
                VendorsContract vc = new VendorsContract(MainActivity.this);
                vc.addVendor("Mike","Pear","mpwar@gmail.com", "MikeIstheBest1234","516-920-0202","Maple Ave","987","10393","Apple Village","VA", (Calendar.getInstance().getTime()).toString(), "Pizza")  ;
                vc.addVendor("John","Deer","jdeer@gmail.com", "JohnIstheBest1234","516-920-0202","Chap Ave","314","10398","Down Town","NY", (Calendar.getInstance().getTime()).toString(), "Tacos")  ;

                //Get Vendor Objects by email for menu table
                Vendor ven,ven1;
                ven = vc.getVendorIdByEmail("mpwar@gmail.com");
                ven1 = vc.getVendorIdByEmail("jdeer@gmail.com");

                //Get Customer object for payment table
                Customer cus;
                cus = cc.getCustomerIdByEmail("khura6@farmingdale.edu");

                //Test Payment table
                PaymentsContract pc = new PaymentsContract(MainActivity.this);
                pc.createPayment("Credit Card", "John Doe", "05/22", "123", "09/25/20", cus.getM_Id());

                //Test Menu table
                MenusContract mc = new MenusContract(MainActivity.this);
                mc.createMenu(ven.getM_Id());
                mc.createMenu(ven1.getM_Id());

                Menu menu = mc.getMenuByVendorId(ven.getM_Id());

                //code to convert test image to byte[] for items constructor
                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.test);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                //Test Items Table
                ItemsContract ic = new ItemsContract(MainActivity.this);
                ic.createItem("Cheese Pizza", "9.99", "Y", bitMapData ,menu.getM_Id());


            }
        });
    }
}