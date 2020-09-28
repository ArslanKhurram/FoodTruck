package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.models.Customer;
import com.example.foodtruck.models.Menu;
import com.example.foodtruck.models.Payment;
import com.example.foodtruck.models.Vendor;

import java.util.Objects;

public class MenusContract {

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
            MenusEntry.COL_VENODR_ID
    };

    //add menu into database
    public Menu createMenu(long vendorID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(MenusEntry.COL_VENODR_ID, vendorID);

        long insertId = mDb.insert(MenusEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(MenusEntry.TABLE_NAME, mAllColumns, MenusEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Menu newMenu = cursorToMenu(cursor, vendorID);
        cursor.close();
        mDb.close();
        close();
        return newMenu;
    }


    //column and table names
    public static final class MenusEntry implements BaseColumns {
        public static final String TABLE_NAME = "menus";
        public static final String COL_VENODR_ID = "vendor_id";
    }



    //set data to specific menu object
    protected Menu cursorToMenu(Cursor cursor, long id) {
        Menu menu = new Menu();
        menu.setM_Id(cursor.getLong(0));

        //get The Customer by id
        VendorsContract contract = new VendorsContract(mContext);
        Vendor vendor = contract.getVendorById(id);
        if (contract != null) {
            menu.setM_Vendor(vendor);
        }
        cursor.close();
        return menu;
    }
}

