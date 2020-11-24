package com.example.foodtruck.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.foodtruck.DataBase.CheckOutContract.CartEntry;

import static com.example.foodtruck.DataBase.CustomersContract.CustomersEntry;
import static com.example.foodtruck.DataBase.FoodTrucksContract.FoodTrucksEntry;
import static com.example.foodtruck.DataBase.ItemsContract.ItemsEntry;
import static com.example.foodtruck.DataBase.MenusContract.MenusEntry;
import static com.example.foodtruck.DataBase.PaymentsContract.PaymentsEntry;
import static com.example.foodtruck.DataBase.VendorsContract.VendorsEntry;

import static com.example.foodtruck.DataBase.AdminContract.AdminEntry;

import static com.example.foodtruck.DataBase.OptionsContract.OptionsEntry;
import static com.example.foodtruck.DataBase.OrdersContract.OrdersEntry;
import static com.example.foodtruck.DataBase.OrderedItemsContract.OrderedItemsEntry;



public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "food_truck.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //query to create payments table
    public static final String SQL_CREATE_PAYMENTS_TABLE = "CREATE TABLE " + PaymentsEntry.TABLE_NAME + " (" +
            PaymentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PaymentsEntry.COL_PAYMENT_TYPE + " TEXT NOT NULL, " +
            PaymentsEntry.COL_NAME_ON_CARD + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CARD_NUMBER + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CC_EXP_DATE + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CCV + " TEXT NOT NULL, " +
            PaymentsEntry.COL_DATE_ADDED + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CUSTOMER_ID + " INTEGER NOT NULL" +
            "); ";

    //query to create customer table
    public static final String SQL_CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + CustomersEntry.TABLE_NAME + " (" +
            CustomersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CustomersEntry.COL_FIRST_NAME + " TEXT NOT NULL, " +
            CustomersEntry.COL_LAST_NAME + " TEXT NOT NULL, " +
            CustomersEntry.COL_EMAIL + " TEXT NOT NULL, " +
            CustomersEntry.COL_PASSWORD + " TEXT NOT NULL, " +
            CustomersEntry.COL_PHONE_NUMBER + " TEXT NOT NULL, " +
            CustomersEntry.COL_STREET_NAME + " TEXT NOT NULL, " +
            CustomersEntry.COL_HOUSE_NUMBER + " TEXT NOT NULL, " +
            CustomersEntry.COL_ZIP_CODE + " TEXT NOT NULL, " +
            CustomersEntry.COL_CITY + " TEXT NOT NULL, " +
            CustomersEntry.COL_STATE + " TEXT NOT NULL" +
            "); ";

    //query to create vendor table
    public static final String SQL_CREATE_VENDORS_TABLE = "CREATE TABLE " + VendorsEntry.TABLE_NAME + " (" +
            VendorsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            VendorsEntry.COL_FIRST_NAME + " TEXT NOT NULL, " +
            VendorsEntry.COL_LAST_NAME + " TEXT NOT NULL, " +
            VendorsEntry.COL_EMAIL + " TEXT NOT NULL, " +
            VendorsEntry.COL_PASSWORD + " TEXT NOT NULL, " +
            VendorsEntry.COL_PHONE_NUMBER + " TEXT NOT NULL, " +
            VendorsEntry.COL_STREET_NAME + " TEXT NOT NULL, " +
            VendorsEntry.COL_HOUSE_NUMBER + " TEXT NOT NULL, " +
            VendorsEntry.COL_ZIP_CODE + " TEXT NOT NULL, " +
            VendorsEntry.COL_CITY + " TEXT NOT NULL, " +
            VendorsEntry.COL_STATE + " TEXT NOT NULL" +
            "); ";

    //query to create menu table
    public static final String SQL_CREATE_MENUS_TABLE = "CREATE TABLE " + MenusEntry.TABLE_NAME + " (" +
            MenusEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MenusEntry.COL_FOOD_TRUCK_ID + " INTEGER NOT NULL" +
            "); ";

    //query to create items table
    public static final String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemsEntry.TABLE_NAME + " (" +
            ItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ItemsEntry.COL_NAME + " TEXT NOT NULL, " +
            ItemsEntry.COL_PRICE + " TEXT NOT NULL, " +
            ItemsEntry.COL_AVAILABILITY + " TEXT NOT NULL, " +
            ItemsEntry.COL_IMAGE + " BLOB NOT NULL, " +
            ItemsEntry.COL_MENU_ID + " INTEGER NOT NULL" +
            "); ";

    //query to create food truck table
    public static final String SQL_CREATE_FOOD_TRUCKS_TABLE = "CREATE TABLE " + FoodTrucksEntry.TABLE_NAME + " (" +
            FoodTrucksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FoodTrucksEntry.COL_NAME + " TEXT NOT NULL, " +
            FoodTrucksEntry.COL_CATEGORY + " TEXT NOT NULL, " +
            FoodTrucksEntry.COL_IMAGE + " BLOB NOT NULL, " +
            FoodTrucksEntry.COL_LATITUDE + " INTEGER NOT NULL, " +
            FoodTrucksEntry.COL_LONGITUDE + " INTEGER NOT NULL, " +
            FoodTrucksEntry.COL_VENDOR_ID + " INTEGER NOT NULL" +
            "); ";


    //query to create admin table
    public static final String SQL_CREATE_ADMIN_TABLE = "CREATE TABLE " + AdminEntry.TABLE_NAME + " (" +
            AdminEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AdminEntry.COL_EMAIL + " TEXT NOT NULL, " +
            AdminEntry.COL_PASSWORD + " INTEGER NOT NULL " + 
            "); ";



    //query to create options table
    public static final String SQL_CREATE_OPTIONS_TABLE = "CREATE TABLE " + OptionsEntry.TABLE_NAME + " (" +
            OptionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OptionsEntry.COL_OPTION + " TEXT NOT NULL, " +
            OptionsEntry.COL_ITEM_ID + " INTEGER NOT NULL " +
            "); ";

    //query to create orders table
    public static final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE " + OrdersEntry.TABLE_NAME + " (" +
            OrdersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OrdersEntry.COL_ORDER_NUMBER + " TEXT NOT NULL, " +
            OrdersEntry.COL_DATE_ADDED + " TEXT NOT NULL, " +
            OrdersEntry.COL_STATUS + " TEXT NOT NULL, " +
            OrdersEntry.COL_CUSTOMER_ID + " INTEGER NOT NULL, " +
            OrdersEntry.COL_FOOD_TRUCK_ID + " INTEGER NOT NULL " +
            "); ";

    //query to create ordered items table
    public static final String SQL_CREATE_ORDERED_ITEMS_TABLE = "CREATE TABLE " + OrderedItemsEntry.TABLE_NAME + " (" +
            OrderedItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            OrderedItemsEntry.COL_QUANTITY + " TEXT NOT NULL, " +
            OrderedItemsEntry.COL_ITEM_ID + " INTEGER NOT NULL, " +
            OrderedItemsEntry.COL_ORDER_ID + " INTEGER NOT NULL " +
            "); ";

    //query to create checkout table
    public static final String SQL_CREATE_CHECKOUT_CART_TABLE = "CREATE TABLE " + CartEntry.TABLE_NAME + " (" +
            CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CartEntry.COL_CUST_ID + " INTEGER NOT NULL, " +
            CartEntry.COL_ITEM + " TEXT NOT NULL, " +
            CartEntry.COL_PRICE + " TEXT NOT NULL, " +
            CartEntry.COL_QUANTITY + " TEXT NOT NULL, " +
            CartEntry.COL_OPTION + " TEXT NOT NULL " +
            ");";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PAYMENTS_TABLE);
        db.execSQL(SQL_CREATE_CUSTOMERS_TABLE);
        db.execSQL(SQL_CREATE_VENDORS_TABLE);
        db.execSQL(SQL_CREATE_MENUS_TABLE);
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
        db.execSQL(SQL_CREATE_FOOD_TRUCKS_TABLE);
        db.execSQL(SQL_CREATE_ADMIN_TABLE);
        db.execSQL(SQL_CREATE_OPTIONS_TABLE);
        db.execSQL(SQL_CREATE_ORDERS_TABLE);
        db.execSQL(SQL_CREATE_ORDERED_ITEMS_TABLE);
        db.execSQL(SQL_CREATE_CHECKOUT_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PaymentsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CustomersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VendorsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MenusEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FoodTrucksEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AdminEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OptionsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrdersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderedItemsEntry.TABLE_NAME);

        onCreate(db);
    }
}
