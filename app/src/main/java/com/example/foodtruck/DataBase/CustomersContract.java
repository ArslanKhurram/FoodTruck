package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.models.Customer;
import com.example.foodtruck.models.Payment;

//class to add customer data to database, foreign key to orders/payments
public final class CustomersContract {

    // Database fields
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //reference to table column names for queries
    private String[] mAllColumns = {
            CustomersEntry._ID,
            CustomersEntry.COL_FIRST_NAME,
            CustomersEntry.COL_LAST_NAME,
            CustomersEntry.COL_EMAIL,
            CustomersEntry.COL_PASSWORD,
            CustomersEntry.COL_PHONE_NUMBER,
            CustomersEntry.COL_STREET_NAME,
            CustomersEntry.COL_HOUSE_NUMBER,
            CustomersEntry.COL_ZIP_CODE,
            CustomersEntry.COL_CITY,
            CustomersEntry.COL_STATE,
    };

    //used to add customer into database
    public Customer addCustomer(String first, String last, String email, String password, String phone, String streetName, String houseNum
            , String zipCode, String city, String state) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(CustomersEntry.COL_FIRST_NAME, first);
        cv.put(CustomersEntry.COL_LAST_NAME, last);
        cv.put(CustomersEntry.COL_EMAIL, email);
        cv.put(CustomersEntry.COL_PASSWORD, password);
        cv.put(CustomersEntry.COL_PHONE_NUMBER, phone);
        cv.put(CustomersEntry.COL_STREET_NAME, streetName);
        cv.put(CustomersEntry.COL_HOUSE_NUMBER, houseNum);
        cv.put(CustomersEntry.COL_ZIP_CODE, zipCode);
        cv.put(CustomersEntry.COL_CITY, city);
        cv.put(CustomersEntry.COL_STATE, state);

        long insertId = mDb.insert(CustomersEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(CustomersEntry.TABLE_NAME, mAllColumns, CustomersEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Customer newCustomer = cursorToCustomer(cursor);
        cursor.close();
        mDb.close();
        close();
        return newCustomer;
    }


    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //return Customer by searching by id
    public Customer getCustomerById(long id) {
        open();
        Cursor cursor = mDb.query(CustomersEntry.TABLE_NAME, mAllColumns, CustomersEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Customer customer = cursorToCustomer(cursor);
        cursor.close();
        mDb.close();
        close();
        return customer;
    }

    //return Customer by searching by email
    public Customer getCustomerIdByEmail(String email) {
        open();
        Cursor cursor = mDb.query(CustomersEntry.TABLE_NAME, mAllColumns, CustomersEntry.COL_EMAIL + " = ?",
                new String[]{String.valueOf(email)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Customer customer = cursorToCustomer(cursor);
        cursor.close();
        mDb.close();
        close();
        return customer;
    }


    //constructor to open customer table
    public CustomersContract(Context context) {
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract ", "SQLException on opening database " + e.getMessage());
        }
    }


    //used to set data to specific customer object
    private Customer cursorToCustomer(Cursor cursor) {
        Customer customer = new Customer();
        customer.setM_Id(cursor.getLong(0));
        customer.setM_FirstName(cursor.getString(1));
        customer.setM_LastName(cursor.getString(2));
        customer.setM_Email(cursor.getString(3));
        customer.setM_Password(cursor.getString(4));
        customer.setM_PhoneNumber(cursor.getString(5));
        customer.setM_StreetName(cursor.getString(6));
        customer.setM_HouseNumber(cursor.getString(7));
        customer.setM_ZipCode(cursor.getString(8));
        customer.setM_City(cursor.getString(9));
        customer.setM_State(cursor.getString(10));

        cursor.close();
        return customer;
    }

    //column and table names
    public static final class CustomersEntry implements BaseColumns {
        public static final String TABLE_NAME = "customers";
        public static final String COL_FIRST_NAME = "first_Name";
        public static final String COL_LAST_NAME = "last_Name";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASSWORD = "password";
        public static final String COL_PHONE_NUMBER = "phone_Number";
        public static final String COL_STREET_NAME = "street_Name";
        public static final String COL_HOUSE_NUMBER = "house_Number";
        public static final String COL_ZIP_CODE = "zip_Code";
        public static final String COL_CITY = "city";
        public static final String COL_STATE = "state";
    }

}
