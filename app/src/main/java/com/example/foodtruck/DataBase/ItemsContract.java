package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.models.Item;
import com.example.foodtruck.models.Menu;
import com.example.foodtruck.models.Vendor;

import java.util.Objects;

public class ItemsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open items table
    public ItemsContract(Context context) {
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
            ItemsEntry._ID,
            ItemsEntry.COL_NAME,
            ItemsEntry.COL_PRICE,
            ItemsEntry.COL_AVAILABILITY,
            ItemsEntry.COL_IMAGE,
            ItemsEntry.COL_MENU_ID
    };

    //add item into database
    public Item createItem(String name, String price, String available, byte[] image, long menuID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(ItemsEntry.COL_NAME, name);
        cv.put(ItemsEntry.COL_PRICE, price);
        cv.put(ItemsEntry.COL_AVAILABILITY, available);
        cv.put(ItemsEntry.COL_IMAGE, image);
        cv.put(ItemsEntry.COL_MENU_ID, menuID);

        long insertId = mDb.insert(ItemsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(ItemsEntry.TABLE_NAME, mAllColumns, ItemsEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Item newItem = cursorToItem(cursor, menuID);
        cursor.close();
        mDb.close();
        close();
        return newItem;
    }


    //set data to specific item object
    protected Item cursorToItem(Cursor cursor, long id) {
        Item item = new Item();
        item.setM_Id(cursor.getLong(0));
        item.setM_Name(cursor.getString(1));
        item.setM_Price(cursor.getString(2));
        item.setM_Available(cursor.getString(3));
        item.setM_Image(cursor.getBlob(4));

        //get The Customer by id
        MenusContract contract = new MenusContract(mContext);
        Menu menu = contract.getMenuByVendorId(id);
        if (contract != null) {
            item.setM_Menu(menu);
        }
        cursor.close();
        return item;
    }

    //column and table names
    public static final class ItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COL_NAME = "name";
        public static final String COL_PRICE = "price";
        public static final String COL_AVAILABILITY = "availability";
        public static final String COL_IMAGE = "image";
        public static final String COL_MENU_ID = "menu_ID";
    }


}
