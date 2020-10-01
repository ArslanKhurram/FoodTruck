package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.models.Customer;
import com.example.foodtruck.models.FoodTruck;
import com.example.foodtruck.models.Item;
import com.example.foodtruck.models.Payment;
import com.example.foodtruck.models.Vendor;

import java.util.ArrayList;
import java.util.Objects;

public final class FoodTrucksContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open payments table
    public FoodTrucksContract(Context context) {
        this.mContext = context;
        mDbHelper = new DbHelper(context);
        //open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ParentContract ", Objects.requireNonNull(e.getMessage()));
        }
    }

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //reference to table column names for queries
    private String[] mAllColumns = {
            FoodTrucksEntry._ID,
            FoodTrucksEntry.COL_NAME,
            FoodTrucksEntry.COL_CATEGORY,
            FoodTrucksEntry.COL_IMAGE,
            FoodTrucksEntry.COL_VENDOR_ID
    };

    //add food truck into database
    public FoodTruck createFoodTruck(String name, String category, byte[] image , long vendorID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(FoodTrucksEntry.COL_NAME, name);
        cv.put(FoodTrucksEntry.COL_CATEGORY, category);
        cv.put(FoodTrucksEntry.COL_IMAGE, image);
        cv.put(FoodTrucksEntry.COL_VENDOR_ID, vendorID);

        long insertId = mDb.insert(FoodTrucksEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(FoodTrucksEntry.TABLE_NAME, mAllColumns, FoodTrucksEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        FoodTruck newFoodTruck = cursorToFoodTruck(cursor, vendorID);
        cursor.close();
        mDb.close();
        close();
        return newFoodTruck;
    }

    //return FoodTruck by searching by id
    public FoodTruck getFoodTruckByVendorId(long id) {
        open();
        Cursor cursor = mDb.query(FoodTrucksEntry.TABLE_NAME, mAllColumns, FoodTrucksEntry.COL_VENDOR_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        FoodTruck foodTruck = cursorToFoodTruck(cursor,id);
        cursor.close();
        mDb.close();
        close();
        return foodTruck;
    }

    //return FoodTruck by searching by id
    public FoodTruck getFoodTruckById(long id) {
        open();
        Cursor cursor = mDb.query(FoodTrucksEntry.TABLE_NAME, mAllColumns, FoodTrucksEntry.COL_VENDOR_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        FoodTruck foodTruck = cursorToFoodTruck(cursor, id);
        cursor.close();
        mDb.close();
        close();
        return foodTruck;
    }


    //return array list of food trucks from a particular menu
    public ArrayList<FoodTruck> FoodTruckList(long vendorID) {
        open();
        ArrayList<FoodTruck> foodTruckArrayListList = new ArrayList<FoodTruck>();

        Cursor cursor = mDb.query(FoodTrucksEntry.TABLE_NAME, mAllColumns, FoodTrucksEntry.COL_VENDOR_ID + " =?",
                new String[]{String.valueOf(vendorID)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                FoodTruck foodTruck = cursorToFoodTruck(cursor,vendorID);
                foodTruckArrayListList.add(foodTruck);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }

        }
        cursor.close();
        mDb.close();
        close();
        return foodTruckArrayListList;
    }

    //set data to specific FoodTruck object
    protected FoodTruck cursorToFoodTruck(Cursor cursor, long id) {
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setM_ID(cursor.getLong(0));
        foodTruck.setM_Name(cursor.getString(1));
        foodTruck.setM_Category(cursor.getString(2));
        foodTruck.setM_Image(cursor.getBlob(3));

        //get The Vendor by id
        VendorsContract contract = new VendorsContract(mContext);
        Vendor vendor = contract.getVendorById(id);
        if (contract != null) {
            foodTruck.setM_Vendor(vendor);
        }
        return foodTruck;
    }

    //column and table names
    public static final class FoodTrucksEntry implements BaseColumns {
        public static final String TABLE_NAME = "food_trucks";
        public static final String COL_NAME = "name";
        public static final String COL_CATEGORY = "category";
        public static final String COL_IMAGE = "image";
        public static final String COL_VENDOR_ID = "vendor_id";
    }

}
