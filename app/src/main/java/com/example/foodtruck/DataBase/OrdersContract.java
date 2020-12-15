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
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Menu;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Vendor;

import java.util.ArrayList;
import java.util.Objects;

//class to add order data to database, foreign key to OrderTracking, Invoice
public final class OrdersContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open order table
    public OrdersContract(Context context) {
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
            OrdersEntry._ID,
            OrdersEntry.COL_ORDER_NUMBER,
            OrdersEntry.COL_DATE_ADDED,
            OrdersEntry.COL_STATUS,
            OrdersEntry.COL_CUSTOMER_ID,
            OrdersEntry.COL_FOOD_TRUCK_ID
    };

    //add order into database
    public Order createOrder(String orderNumber, String dateAdded, String status, long customerId, long vendorId) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(OrdersEntry.COL_ORDER_NUMBER, orderNumber);
        cv.put(OrdersEntry.COL_DATE_ADDED, dateAdded);
        cv.put(OrdersEntry.COL_STATUS, status);
        cv.put(OrdersEntry.COL_CUSTOMER_ID, customerId);
        cv.put(OrdersEntry.COL_FOOD_TRUCK_ID, vendorId);

        long insertId = mDb.insert(OrdersEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(OrdersEntry.TABLE_NAME, mAllColumns, OrdersEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Order newOrder = cursorToOrder(cursor, customerId, vendorId);
        cursor.close();
        mDb.close();
        close();
        return newOrder;
    }

    //return array List of orders for vendor
    public ArrayList<Order> getOrdersList(long foodTruckID) {
        open();
        ArrayList<Order> ordersList = new ArrayList<Order>();

        Cursor cursor = mDb.query(OrdersEntry.TABLE_NAME, mAllColumns, OrdersEntry.COL_FOOD_TRUCK_ID + " =?",
                new String[]{String.valueOf(foodTruckID)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Order order = cursorToOrder(cursor, cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_CUSTOMER_ID)), foodTruckID);
                ordersList.add(order);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }
        cursor.close();
        mDb.close();
        close();
        return ordersList;
    }

    //return array List of orders for vendor
    public ArrayList<Order> getOrderListByStatus(long foodTruckID, String status) {
        open();
        ArrayList<Order> ordersList = new ArrayList<Order>();

        Cursor cursor = mDb.query(OrdersEntry.TABLE_NAME, mAllColumns, OrdersEntry.COL_STATUS + " =? " + " AND " + OrdersEntry.COL_FOOD_TRUCK_ID + " =? ", new String[]{status, String.valueOf(foodTruckID)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Order order = cursorToOrder(cursor, cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_CUSTOMER_ID)), foodTruckID);
                ordersList.add(order);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }
        cursor.close();
        mDb.close();
        close();
        return ordersList;
    }

    //return Order by searching by id
    public Order getOrderById(long id) {
        open();
        Cursor cursor = mDb.query(OrdersEntry.TABLE_NAME, mAllColumns, OrdersEntry._ID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Order order = cursorToOrder(cursor, cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_CUSTOMER_ID)), cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_FOOD_TRUCK_ID)));
        cursor.close();
        mDb.close();
        close();
        return order;
    }

    //set data to specific order object
    protected Order cursorToOrder(Cursor cursor, long customerID, long foodTruckID) {
        Order order = new Order();
        order.setM_Id(cursor.getLong(0));
        order.setM_OrderNumber(cursor.getString(1));
        order.setM_DateAdded(cursor.getString(2));
        order.setM_Status(cursor.getString(3));

        //get The Customer by id
        CustomersContract cc = new CustomersContract(mContext);
        Customer customer = cc.getCustomerById(customerID);
        if (cc != null) {
            order.setM_Customer(customer);
        }

        //get The Vendor by id
        FoodTrucksContract fc = new FoodTrucksContract(mContext);
        FoodTruck foodTruck = fc.getFoodTruckById(foodTruckID);
        if (foodTruck != null) {
            order.setM_FoodTruck(foodTruck);
        }

        return order;
    }

    //return array List of all orders
    public ArrayList<Order> getAllOrders() {
        open();
        ArrayList<Order> ordersList = new ArrayList<Order>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + OrdersEntry.TABLE_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Order order = cursorToOrder(cursor, cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_CUSTOMER_ID)), cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_FOOD_TRUCK_ID)));
                ordersList.add(order);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }
        cursor.close();
        mDb.close();
        close();
        return ordersList;
    }


    //return Order by searching by order number
    public Order getOrderByOrderNumber(String orderNumber) {
        open();
        Cursor cursor = mDb.query(OrdersEntry.TABLE_NAME, mAllColumns, OrdersEntry.COL_ORDER_NUMBER + " =? ",
                new String[]{orderNumber}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Order order = cursorToOrder(cursor, cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_CUSTOMER_ID)), cursor.getLong(cursor.getColumnIndex(OrdersEntry.COL_FOOD_TRUCK_ID)));
        cursor.close();
        mDb.close();
        close();
        return order;
    }





    //column and table names
    public static final class OrdersEntry implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COL_ORDER_NUMBER = "orderNumber";
        public static final String COL_DATE_ADDED = "dateAdded";
        public static final String COL_STATUS = "status";
        public static final String COL_CUSTOMER_ID = "customer_ID";
        public static final String COL_FOOD_TRUCK_ID = "foodtruck_ID";
    }

    public void updateOrder(long id, String status) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(OrdersEntry.COL_STATUS, status);

        mDb.update(OrdersContract.OrdersEntry.TABLE_NAME, cv, OrdersContract.OrdersEntry._ID + " = " + id, null);
        close();
    }
}

