package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;


import com.example.foodtruck.Models.CartOptions;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Item;


public class CartOptionsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;


    private String[] mAllColumns = {
            CartOptionsEntry._ID,
            CartOptionsEntry.COL_CUST_ID,
            CartOptionsEntry.COL_ITEM_ID,
            CartOptionsEntry.COL_OPTION_ID
    };


    //Add to Database
    public CartOptions savedSelectedItemsOptions(long customerId, long itemId, long cartId) {
        open();
        ContentValues av = new ContentValues();
        av.put(CartOptionsEntry.COL_CUST_ID, customerId);
        av.put(CartOptionsEntry.COL_ITEM_ID, itemId);
        av.put(CartOptionsEntry.COL_OPTION_ID, cartId);
        long insertID = mDb.insert(CartOptionsEntry.TABLE_NAME, null, av);
        Cursor cursor = mDb.query(CartOptionsEntry.TABLE_NAME, mAllColumns, CartOptionsEntry._ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        CartOptions cartOptions = cursorToTable(cursor);
        cursor.close();
        mDb.close();
        return cartOptions;
    }


    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }


    //remove option selected from database
    public void clearTable(long id, long id2) {
        open();
        mDb = mDbHelper.getWritableDatabase();
        String dlQuery = "DELETE FROM " + CartOptionsEntry.TABLE_NAME + " WHERE " + CartOptionsEntry._ID + " = " + id + " AND " + CartOptionsEntry.COL_OPTION_ID + " = " + id2;
        Cursor cursor = mDb.rawQuery(dlQuery, null);
        cursor.moveToFirst();
        cursor.close();
        mDb.close();
        close();
    }


    //return cart item options by id
    public CartOptions getCartItemOptions(long id) {
        open();
        Cursor cursor = mDb.query(CartOptionsEntry.TABLE_NAME, mAllColumns, CartOptionsEntry.COL_CUST_ID + " + ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        CartOptions cartOptions = cursorToTable(cursor);
        cursor.close();
        mDb.close();
        close();

        return cartOptions;
    }


    //Open Table
    public CartOptionsContract(Context context) {
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract", "SQLException on opening database " + e.getMessage());
        }
    }


    //Setter
    private CartOptions cursorToTable(Cursor cursor) {
        CartOptions cartOptions = new CartOptions();
        cartOptions.setM_Id(cursor.getLong(0));

        CustomersContract cC = new CustomersContract(mContext);
        Customer cc = cC.getCustomerById(cursor.getLong(cursor.getColumnIndex(CartOptionsEntry.COL_CUST_ID)));
        if (cC != null) {
            cartOptions.setM_Customer(cc);
        }

        ItemsContract iC = new ItemsContract(mContext);
        Item item = iC.getItemById(cursor.getLong(cursor.getColumnIndex(CartOptionsEntry.COL_ITEM_ID)));
        if (iC != null) {
            cartOptions.setM_itemId(item);
        }

        cartOptions.setM_cartId(cursor.getLong(3));

        cursor.close();
        return cartOptions;
    }


    public static final class CartOptionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart_options";
        public static final String COL_CUST_ID = "custID";
        public static final String COL_ITEM_ID = "itemID";
        public static final String COL_OPTION_ID = "optionID" ;


    }
}


