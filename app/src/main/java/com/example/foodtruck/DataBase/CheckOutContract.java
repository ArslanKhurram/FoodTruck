package com.example.foodtruck.DataBase;
//customer id
//delete function
//

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.Customer;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Option;

import java.util.ArrayList;

public class CheckOutContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;


    //reference to table column name
    private String[] mAllColumns = {
            CartEntry._ID,
            CartEntry.COL_CUST_ID,
            CartEntry.COL_ITEM_ID,
            CartEntry.COL_QUANTITY,
            CartEntry.COL_ORDER_NUMBER
    };


    //used to add cart into database
    public Cart addCart (long item_ID , String qnty, long customerId, long orderNum){
        open();
        ContentValues av = new ContentValues();
        av.put(CartEntry.COL_ITEM_ID, item_ID);
        av.put(CartEntry.COL_QUANTITY,qnty);
        av.put(CartEntry.COL_CUST_ID, customerId);
        av.put(CartEntry.COL_ORDER_NUMBER, orderNum);
        long insertID = mDb.insert(CartEntry.TABLE_NAME, null, av);
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME, mAllColumns, CartEntry._ID + " = " + insertID, null, null, null, null);
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
    public Cart getCart(long custId){

        open();
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME,mAllColumns,CartEntry.COL_CUST_ID + " = ?",
        new String[]{String.valueOf(custId)},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Cart cart = cursorToCart(cursor);
        cursor.close();
        mDb.close();
        close();
        return cart;

    }

    //return cart b
    public Cart getCartByNumberId(long numberId){
        open();
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME,mAllColumns,CartEntry.COL_ORDER_NUMBER + " = ? " ,
                new String[]{String.valueOf(numberId)},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Cart cart = cursorToCart(cursor);
        cursor.close();
        mDb.close();
        close();
        return cart;

    }


    public Cart getCartById(long id){
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



    //return array list of options
    public ArrayList<Cart> getEntireCart(long itemID){
        open();
        boolean check = checkIfOptionsExist(itemID);
        if (check) {
            ArrayList<Cart> cartList = new ArrayList<Cart>();

            Cursor cursor = mDb.query(CartEntry.TABLE_NAME, mAllColumns, CartEntry.COL_CUST_ID + " =?",
                    new String[]{String.valueOf(itemID)}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Cart cart = cursorToCart(cursor);
                cartList.add(cart);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
            cursor.close();
            return cartList;
        }
        mDb.close();
        close();
        return null;
    }


    //check is options exist for an item
    public boolean checkIfOptionsExist(long ItemID) {
        open();
        Cursor cursor = mDb.query(CartEntry.TABLE_NAME, mAllColumns, CartEntry._ID + "=? ",
                new String[]{String.valueOf(ItemID)}, null, null, null);

        boolean check = cursor.getCount() > 0;
        cursor.close();
        return check;
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
        cart.setM_Quantity(cursor.getString(3));
        cart.setM_OrderNumber(cursor.getLong(4));
        //get cart by item id
        ItemsContract ic = new ItemsContract(mContext);
        Item item = ic.getItemById(cursor.getLong(cursor.getColumnIndex(CartEntry.COL_ITEM_ID)));
        if(item != null){
                cart.setM_Item(item);
        }
        //get The Customer by id
        CustomersContract contract = new CustomersContract(mContext);
        Customer customer = contract.getCustomerById(cursor.getLong(cursor.getColumnIndex(CartEntry.COL_CUST_ID)));
        if (contract != null) {
            cart.setCustId(customer);
        }
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
        public static final String COL_ITEM_ID = "itemID";
        public static final String COL_QUANTITY = "Quantity";
        public static final String COL_ORDER_NUMBER = "OrderNumber";


    }// ends CartEntry
}



