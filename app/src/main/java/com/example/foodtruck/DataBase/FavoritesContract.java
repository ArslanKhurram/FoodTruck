package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Favorite;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Payment;
import com.example.foodtruck.Models.Rating;

import java.util.ArrayList;
import java.util.Objects;

public final class FavoritesContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;
    //reference to table column names for queries
    private String[] mAllColumns = {
            FavoritesEntry._ID,
            FavoritesEntry.COL_FOODTRUCK_ID,
            FavoritesEntry.COL_CUSTOMER_ID

    };

    //constructor to open table
    public FavoritesContract(Context context) {
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

    //set data to specific ratings object
    protected Favorite cursorToFavorite(Cursor cursor) {
        Favorite favorite = new Favorite();

        favorite.setM_Id(cursor.getLong(0));

        //get the FoodTruck by ID
        FoodTrucksContract ft = new FoodTrucksContract(mContext);
        FoodTruck foodTruck = ft.getFoodTruckById(cursor.getLong(cursor.getColumnIndex(FavoritesEntry.COL_FOODTRUCK_ID)));
        if (foodTruck != null) {
            favorite.setM_FoodTruckID(foodTruck);
        }

        //get the Customer by ID
        CustomersContract cc = new CustomersContract(mContext);
        Customer customer = cc.getCustomerById(cursor.getLong(cursor.getColumnIndex(FavoritesEntry.COL_CUSTOMER_ID)));
        if (customer != null) {
            favorite.setM_CustomerID(customer);
        }

        return favorite;
    }

    public Favorite createEntry(long foodTruckID, long customerID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(FavoritesEntry.COL_FOODTRUCK_ID, foodTruckID);
        cv.put(FavoritesEntry.COL_CUSTOMER_ID, customerID);

        long insertId = mDb.insert(FavoritesEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(FavoritesEntry.TABLE_NAME, mAllColumns, FavoritesEntry._ID +
                " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Favorite newFavorite = cursorToFavorite(cursor);
        cursor.close();
        mDb.close();
        close();
        return newFavorite;
    }

    public ArrayList<FoodTruck> getSavedFoodTrucks(long customerID) {
        open();
        ArrayList<FoodTruck> savedFoodTrucks = new ArrayList<>();
        FoodTrucksContract fc = new FoodTrucksContract(mContext);


        Cursor cursor = mDb.rawQuery("SELECT " + FavoritesEntry.COL_FOODTRUCK_ID + " FROM " + FavoritesEntry.TABLE_NAME + " WHERE " + FavoritesEntry.COL_CUSTOMER_ID + " = " + String.valueOf(customerID), null);

        Log.i("Cursor", DatabaseUtils.dumpCursorToString(cursor));
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                FoodTruck foodTruck = fc.getFoodTruckById(cursor.getLong(0));
                savedFoodTrucks.add(foodTruck);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else {
                    cursor.moveToNext();
                }
            }

        }
        cursor.close();
        mDb.close();
        close();
        return savedFoodTrucks;
    }

    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COL_FOODTRUCK_ID = "foodtruck_ID";
        public static final String COL_CUSTOMER_ID = "customer_ID";
    }

}
