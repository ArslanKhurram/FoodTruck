package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.OrderedItem;

import java.util.ArrayList;
import java.util.Objects;

//class to add Ordered Items to database
public final class OrderedItemsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;
    //reference to table column names for queries
    private String[] mAllColumns = {
            OrderedItemsEntry._ID,
            OrderedItemsEntry.COL_QUANTITY,
            OrderedItemsEntry.COL_ITEM_ID,
            OrderedItemsEntry.COL_ORDER_ID
    };

    //constructor to open ordereditems table
    public OrderedItemsContract(Context context) {
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
    public OrderedItem addOrderedItem(String quantity, long itemID, long orderID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(OrderedItemsEntry.COL_QUANTITY, quantity);
        cv.put(OrderedItemsEntry.COL_ITEM_ID, itemID);
        cv.put(OrderedItemsEntry.COL_ORDER_ID, orderID);

        long insertId = mDb.insert(OrderedItemsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(OrderedItemsEntry.TABLE_NAME, mAllColumns, OrderedItemsEntry._ID +
                " = " + insertId, null, null, null, null);

        OrderedItem newOrderedItem = new OrderedItem();
        if (cursor != null) {
            cursor.moveToFirst();
            newOrderedItem = cursorToOrderedItem(cursor, itemID, orderID);
            cursor.close();
            mDb.close();
            close();
        }
        return newOrderedItem;
    }

    //set data to specific item object
    protected OrderedItem cursorToOrderedItem(Cursor cursor, long itemID, long orderID) {
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setM_id(cursor.getLong(0));
        orderedItem.setM_Quantity(cursor.getString(1));

        //get The Item by id
        ItemsContract ic = new ItemsContract(mContext);
        Item item = ic.getItemById(itemID);
        orderedItem.setM_Item(item);

        //Get the order item by id
        OrdersContract oc = new OrdersContract(mContext);
        Order order = oc.getOrderById(orderID);
        orderedItem.setM_Order(order);


        return orderedItem;
    }

    public OrderedItem getOrderedItemById(long id) {
        open();
        Cursor cursor = mDb.query(OrderedItemsEntry.TABLE_NAME, mAllColumns, OrderedItemsEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        OrderedItem orderedItem = cursorToOrderedItem(cursor, cursor.getLong(cursor.getColumnIndex(OrderedItemsEntry.COL_ITEM_ID)), cursor.getLong(cursor.getColumnIndex(OrderedItemsEntry.COL_ORDER_ID)));
        cursor.close();
        mDb.close();
        close();
        return orderedItem;
    }

    //return array list for an order
    public ArrayList<OrderedItem> getOrderedItems(long orderID) {
        open();
        ArrayList<OrderedItem> orderedItems = new ArrayList<>();

        Cursor cursor = mDb.query(OrderedItemsEntry.TABLE_NAME, mAllColumns, OrderedItemsEntry.COL_ORDER_ID + " =? ",
                new String[]{String.valueOf(orderID)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OrderedItem orderedItem = cursorToOrderedItem(cursor, cursor.getLong(cursor.getColumnIndex(OrderedItemsEntry.COL_ITEM_ID)), orderID);
                orderedItems.add(orderedItem);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }

        cursor.close();
        ;
        mDb.close();
        close();
        return orderedItems;
    }


    //return ordereditem ID by order id and item id
    public OrderedItem getOrderedItemByOrderAndItemId(long orderId, long itemId) {
        open();
        Cursor cursor = mDb.query(OrderedItemsEntry.TABLE_NAME, mAllColumns, OrderedItemsEntry.COL_ORDER_ID + " = ? AND " + OrderedItemsEntry.COL_ITEM_ID + "= ?",
                new String[]{String.valueOf(orderId), String.valueOf(itemId)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        OrderedItem orderedItem = cursorToOrderedItem(cursor, cursor.getLong(cursor.getColumnIndex(OrderedItemsEntry.COL_ITEM_ID)), cursor.getLong(cursor.getColumnIndex(OrderedItemsEntry.COL_ORDER_ID)));
        cursor.close();
        mDb.close();
        close();
        return orderedItem;
    }


    //column and table names
    public static final class OrderedItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "ordered_items";
        public static final String COL_QUANTITY = "quantity";
        public static final String COL_ITEM_ID = "itemID";
        public static final String COL_ORDER_ID = "orderID";

    }
}
