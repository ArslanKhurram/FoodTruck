package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.OrderedItem;
import com.example.foodtruck.Models.OrderedItemOptions;

import java.util.ArrayList;
import java.util.Objects;

public final class OrderedItemOptionsContract {
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;
    //reference to table column names for queries
    private String[] mAllColumns = {
            OrderedItemOptionsEntry.COL_OPTION_ID,
            OrderedItemOptionsEntry.COL_ITEM_ID,
            OrderedItemOptionsEntry.COL_ORDERED_ITEM_ID
    };

    //constructor to open OrderedItemOptions table
    public OrderedItemOptionsContract(Context context) {
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

    //add item into database
    public OrderedItemOptions addOrderedItemOptions(long optionID,long itemID, long ordereditemID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(OrderedItemOptionsEntry.COL_OPTION_ID, optionID);
        cv.put(OrderedItemOptionsEntry.COL_ITEM_ID, itemID);
        cv.put(OrderedItemOptionsEntry.COL_ORDERED_ITEM_ID, ordereditemID);

        long insertId = mDb.insert(OrderedItemOptionsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(OrderedItemOptionsEntry.TABLE_NAME, mAllColumns, OrderedItemOptionsEntry._ID +
                " = " + insertId, null, null, null, null);

        OrderedItemOptions newOrderedItemOptions = new OrderedItemOptions();
        if (cursor != null) {
            cursor.moveToFirst();
            newOrderedItemOptions = cursorToItemOptions(cursor, itemID, ordereditemID);
            cursor.close();
            mDb.close();
            close();
        }
        return newOrderedItemOptions;
    }

    //set data to specific item object
    protected OrderedItemOptions cursorToItemOptions(Cursor cursor, long itemID, long ordereditemID) {
        OrderedItemOptions orderedItemOptions = new OrderedItemOptions();
        orderedItemOptions.setM_id(cursor.getLong(0));

        //get The Item by id
        ItemsContract ic = new ItemsContract(mContext);
        Item item = ic.getItemById(itemID);
        orderedItemOptions.setM_Item(item);

        //Get the ordereditem by id
        OrderedItemsContract oic = new OrderedItemsContract(mContext);
        OrderedItem orderedItem = oic.getOrderedItemById(ordereditemID);
        orderedItemOptions.setM_OrderedItem(orderedItem);


        return orderedItemOptions;
    }

    //return array list for an order
    public ArrayList<OrderedItemOptions> getOrderedItemOptions(long ordereditemID) {
        open();
        ArrayList<OrderedItemOptions> orderedItemOptions = new ArrayList<>();

        Cursor cursor = mDb.query(OrderedItemOptionsEntry.TABLE_NAME, mAllColumns,  OrderedItemOptionsEntry.COL_ORDERED_ITEM_ID + " =? ",
                new String[]{String.valueOf(ordereditemID)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OrderedItemOptions mOrderedItemOptions = cursorToItemOptions(cursor, cursor.getLong(cursor.getColumnIndex(OrderedItemOptionsEntry.COL_ITEM_ID)), ordereditemID);
                orderedItemOptions.add(mOrderedItemOptions);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }

        cursor.close();
        mDb.close();
        close();
        return orderedItemOptions;
    }

    //column and table names
    public static final class OrderedItemOptionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "ordered_items_options";
        public static final String COL_OPTION_ID = "optionID";
        public static final String COL_ITEM_ID = "itemID";
        public static final String COL_ORDERED_ITEM_ID = "ordereditemID";

    }
}
