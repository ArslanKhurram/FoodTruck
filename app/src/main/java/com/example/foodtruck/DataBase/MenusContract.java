package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.github.mikephil.charting.utils.Utils;

import java.util.Objects;

//class to add menu data to database, foreign key to Items
public final class MenusContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open menu table
    public MenusContract(Context context) {
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
            MenusEntry._ID,
            MenusEntry.COL_FOOD_TRUCK_ID
    };

    //add menu into database
    public Menu createMenu(long foodTruckID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(MenusEntry.COL_FOOD_TRUCK_ID, foodTruckID);

        long insertId = mDb.insert(MenusEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(MenusEntry.TABLE_NAME, mAllColumns, MenusEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Menu newMenu = cursorToMenu(cursor);
        cursor.close();
        mDb.close();
        close();
        return newMenu;
    }

    //return Menu by searching by id
    public Menu getMenuById(long id) {
        open();
        Cursor cursor = mDb.query(MenusEntry.TABLE_NAME, mAllColumns, MenusEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Menu menu = cursorToMenu(cursor);
        cursor.close();
        mDb.close();
        close();
        return menu;
    }

    //check if menu exists before deleting
    public boolean checkIfMenuExists(long foodTruckID) {
        open();
        Cursor cursor = mDb.query(MenusEntry.TABLE_NAME, mAllColumns, MenusEntry.COL_FOOD_TRUCK_ID + " =? ",
                new String[]{String.valueOf(foodTruckID)}, null, null, null);

        return cursor != null;
    }

    public void removeMenusByFoodTruckID(long id) {
        open();
        mDb = mDbHelper.getWritableDatabase();
        boolean check = checkIfMenuExists(id);
        if (check) {
            String dlQuery = "DELETE FROM " + MenusEntry.TABLE_NAME + " WHERE " + MenusEntry.COL_FOOD_TRUCK_ID + " = " + id;
            Cursor cursor = mDb.rawQuery(dlQuery, null);
            cursor.moveToNext();
        }
        mDb.close();
        close();
    }

    //return Menu by searching by id
    public Menu getMenuByFoodTruckId(long id) {
        open();
        boolean check = checkIfMenuExists(id);
        if (check) {
            Cursor cursor = mDb.query(MenusEntry.TABLE_NAME, mAllColumns, MenusEntry._ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Menu menu = cursorToMenu(cursor);
                cursor.close();
                mDb.close();
                close();
                return menu;
            }
        }

        return null;
    }

    //column and table names
    public static final class MenusEntry implements BaseColumns {
        public static final String TABLE_NAME = "menus";
        public static final String COL_FOOD_TRUCK_ID = "foodTruck_ID";
    }

    //set data to specific menu object
    protected Menu cursorToMenu(Cursor cursor) {
        Menu menu = new Menu();
        Log.i("Test", DatabaseUtils.dumpCursorToString(cursor));
        Log.i("Test", String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 1) { //check if cursor is empty
            menu.setM_Id(cursor.getLong(0));
            //get The FoodTruck by id
            FoodTrucksContract contract = new FoodTrucksContract(mContext);
            FoodTruck foodTruck = contract.getFoodTruckById(cursor.getLong(cursor.getColumnIndex(MenusEntry.COL_FOOD_TRUCK_ID)));
            menu.setM_FoodTruck(foodTruck);
        }
        cursor.close();
        return menu;
    }
}

