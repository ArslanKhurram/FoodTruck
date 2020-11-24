package com.example.foodtruck.DataBase;
//customer id
//delete function
//

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;

public class CheckOutContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;


    //reference to table column name
    private String[] mAllColumns = {
            CartEntry._ID,
            CartEntry.COL_CUST_ID,
            CartEntry.COL_ITEM,
            CartEntry.COL_PRICE,
            CartEntry.COL_QUANTITY,
    };

    //Add checkout cart to database
    public Cart addCartByObject (Cart cart){
        open();
        ContentValues av = new ContentValues();
        av.put(CartEntry.COL_ITEM, cart.getM_Item());
        av.put(CartEntry.COL_PRICE, cart.getM_Price());
        av.put(CartEntry.COL_QUANTITY,cart.getM_Quantity());

        long insertID = mDb.insert(CartEntry.TABLE_NAME,null,av);
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME,mAllColumns,CartEntry._ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Cart newCart = cursorToCart(cursor);
        cursor.close();
        mDb.close();
        close();
        return newCart;
    }

    //used to add cart into database
    public Cart addCart (String item , String price, String qnty, long customerId){
        open();
        ContentValues av = new ContentValues();
        av.put(CartEntry.COL_ITEM, item);
        av.put(CartEntry.COL_PRICE, price);
        av.put(CartEntry.COL_QUANTITY,qnty);
        av.put(CartEntry.COL_CUST_ID, customerId);
        long insertID = mDb.insert(CartEntry.TABLE_NAME,null,av);
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME,mAllColumns,CartEntry._ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Cart newCart = cursorToCart(cursor);
        cursor.close();
        mDb.close();
        close();
        return newCart;
    }

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //return cart by id
    public Cart getCart(long id){
        open();
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME,mAllColumns,CartEntry._ID + " = ?",
                                    new String[]{String.valueOf(id)},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Cart cart = cursorToCart(cursor);
        cursor.close();
        mDb.close();
        close();
        return cart;

    }

    //open cart table
    public CheckOutContract(Context context){
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract", "SQLException on opening database " + e.getMessage());
        }
    }//end checkOutContract

    //setters
    private Cart cursorToCart(Cursor cursor){
        Cart cart = new Cart();
        cart.setM_ID(cursor.getLong(0));
        cart.setM_Item(cursor.getString(1));
        cart.setM_Price(cursor.getString(2));
        cart.setM_Quantity(cursor.getString(3));

        //get The Customer by id
        CustomersContract contract = new CustomersContract(mContext);
        Customer customer = contract.getCustomerById(cursor.getLong(cursor.getColumnIndex(CartEntry.COL_CUST_ID)));
        if (contract != null) {
            cart.setCustId(customer);
        }


        cursor.close();
        return cart;
            }


    //remove item from database
    public void clearTable(long id) {
        open();
        mDb = mDbHelper.getWritableDatabase();
        String dlQuery = "DELETE FROM " + CartEntry.TABLE_NAME + " WHERE " + CartEntry.COL_CUST_ID + " = " + id;
        Cursor cursor = mDb.rawQuery(dlQuery, null);
        cursor.moveToFirst();
        cursor.close();
        mDb.close();
        close();
    }


    //column and tables names
    public static final class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart";
        public static final String COL_CUST_ID = "custID";
        public static final String COL_ITEM = "item";
        public static final String COL_PRICE = "price";
        public static final String COL_QUANTITY = "Quantity";

    }// ends CartEntry
}



