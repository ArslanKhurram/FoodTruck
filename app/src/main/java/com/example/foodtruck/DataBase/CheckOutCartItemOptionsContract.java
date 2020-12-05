package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Cart;
import com.example.foodtruck.Models.CartItemOption;
import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Option;


public class CheckOutCartItemOptionsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;


    private String[] mAllColumns = {
            CheckOutOptionEntry._ID,
            CheckOutOptionEntry.COL_CUST_ID,
            CheckOutOptionEntry.COL_ITEM_ID,
            CheckOutOptionEntry.COL_ITEM_OPTION_ID
    };



    //Add to Database
    public CartItemOption savedSelectedItemsOptions(long customerId, long itemId, long optionId) {
        open();
        ContentValues av = new ContentValues();
        av.put(CheckOutOptionEntry.COL_CUST_ID, customerId);
        av.put(CheckOutOptionEntry.COL_ITEM_ID, itemId);
        av.put(CheckOutOptionEntry.COL_ITEM_OPTION_ID, optionId);
        long insertID = mDb.insert(CheckOutOptionEntry.TABLE_NAME, null, av);
        Cursor cursor = mDb.query(CheckOutOptionEntry.TABLE_NAME, mAllColumns, CheckOutOptionEntry._ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        CartItemOption newCartItemOption = cursorToTable(cursor);
        cursor.close();
        mDb.close();
        return newCartItemOption;


    }

    //Open Database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //Close Database
    public void close() {
        mDbHelper.close();
    }


    //Open Table
    public CheckOutCartItemOptionsContract(Context context) {
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract", "SQLException on opening database " + e.getMessage());
        }
    }


    //return cart item options by id
    public CartItemOption getCartItemOptions(long id) {
        open();
        Cursor cursor = mDb.query(CheckOutOptionEntry.TABLE_NAME, mAllColumns, CheckOutOptionEntry.COL_CUST_ID + " + ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        CartItemOption cartItemOption = cursorToTable(cursor);
        cursor.close();
        mDb.close();
        close();

        return cartItemOption;
    }



    //Setter
    private CartItemOption cursorToTable(Cursor cursor) {
        CartItemOption cartItemOption = new CartItemOption();
        cartItemOption.setM_Id(cursor.getLong(0));

        CustomersContract cC = new CustomersContract(mContext);
        Customer cc = cC.getCustomerById(cursor.getLong(cursor.getColumnIndex(CheckOutOptionEntry.COL_CUST_ID)));
        if (cC != null) {
            cartItemOption.setM_Customer(cc);
        }

        ItemsContract iC = new ItemsContract(mContext);
        Item item = iC.getItemById(cursor.getLong(cursor.getColumnIndex(CheckOutOptionEntry.COL_ITEM_ID)));
        if (iC != null) {
            cartItemOption.setM_itemId(item);
        }

        OptionsContract oC = new OptionsContract(mContext);
        Option option = oC.getOptionById(cursor.getLong(cursor.getColumnIndex(CheckOutOptionEntry.COL_ITEM_OPTION_ID)));
        if (oC != null) {
            cartItemOption.setM_OptionId(option);
        }


        cursor.close();
        return cartItemOption;
    }


    public static final class CheckOutOptionEntry implements BaseColumns {
        public static final String TABLE_NAME = "ckOutOptions";
        public static final String COL_CUST_ID = "custID";
        public static final String COL_ITEM_ID = "itemID";
        public static final String COL_ITEM_OPTION_ID = "optionId";


    }
}


