package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Rating;
import com.example.foodtruck.Models.Vendor;

import java.util.Objects;

//class to add rating data to database
public final class RatingsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;
    //reference to table column names for queries
    private String[] mAllColumns = {
            RatingsEntry._ID,
            RatingsEntry.COL_RATING,
            RatingsEntry.COL_TITLE,
            RatingsEntry.COL_DETAILS,
            RatingsEntry.COL_FOODTRUCK_ID,
            RatingsEntry.COL_CUSTOMER_ID
    };

    //constructor to open ratings table
    public RatingsContract(Context context) {
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
    protected Rating cursorToRating(Cursor cursor) {
        Rating rating = new Rating();

        rating.setM_Id(cursor.getLong(0));
        rating.setM_Rating(cursor.getString(1));
        rating.setM_Detail(cursor.getString(2));

        //get the FoodTruck by ID
        FoodTrucksContract ft = new FoodTrucksContract(mContext);
        FoodTruck foodTruck = ft.getFoodTruckById(cursor.getLong(cursor.getColumnIndex(RatingsEntry.COL_FOODTRUCK_ID)));
        if (foodTruck != null) {
            rating.setM_FoodTruck(foodTruck);
        }

        //get the Customer by ID
        CustomersContract cc = new CustomersContract(mContext);
        Customer customer = cc.getCustomerById(cursor.getLong(cursor.getColumnIndex(RatingsEntry.COL_CUSTOMER_ID)));
        if (customer != null) {
            rating.setM_Customer(customer);
        }

        return rating;
    }

    public Rating createRating(String rating, String title, String details, long foodTruckID, long customerID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(RatingsEntry.COL_RATING, rating);
        cv.put(RatingsEntry.COL_TITLE, title);
        cv.put(RatingsEntry.COL_DETAILS, details);
        cv.put(RatingsEntry.COL_FOODTRUCK_ID, foodTruckID);
        cv.put(RatingsEntry.COL_CUSTOMER_ID, customerID);

        long insertId = mDb.insert(RatingsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(RatingsEntry.TABLE_NAME, mAllColumns, RatingsEntry._ID +
                " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        Rating newRating = cursorToRating(cursor);
        cursor.close();
        mDb.close();
        close();
        return newRating;
    }

    //column and table names
    public static final class RatingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "ratings";
        public static final String COL_RATING = "rating";
        public static final String COL_TITLE = "title";
        public static final String COL_DETAILS = "details";
        public static final String COL_FOODTRUCK_ID = "foodtruck_ID";
        public static final String COL_CUSTOMER_ID = "customer_ID";
    }
}
