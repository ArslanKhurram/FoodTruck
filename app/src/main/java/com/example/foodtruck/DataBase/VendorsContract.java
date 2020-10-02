package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Vendor;

import java.util.Objects;

//class to add vendor data to database, foreign key to orders/menu/orderTracking/VendorTracking
public final class VendorsContract {

    // Database fields
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open payments table
    public VendorsContract(Context context) {
        this.mContext = context;
        mDbHelper = new DbHelper(context);
        //open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ParentContract ", Objects.requireNonNull(e.getMessage()));
        }
    }

    //reference to table column names for queries
    private String[] mAllColumns = {
            VendorsEntry._ID,
            VendorsEntry.COL_FIRST_NAME,
            VendorsEntry.COL_LAST_NAME,
            VendorsEntry.COL_EMAIL,
            VendorsEntry.COL_PASSWORD,
            VendorsEntry.COL_PHONE_NUMBER,
            VendorsEntry.COL_STREET_NAME,
            VendorsEntry.COL_HOUSE_NUMBER,
            VendorsEntry.COL_ZIP_CODE,
            VendorsEntry.COL_CITY,
            VendorsEntry.COL_STATE,
    };
    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //return Vendor by searching by id
    public Vendor getVendorById(long id) {
        open();
        Cursor cursor = mDb.query(VendorsEntry.TABLE_NAME, mAllColumns, VendorsEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Vendor vendor = cursorToVendor(cursor);
        cursor.close();
        mDb.close();
        close();
        return vendor;
    }

    //return Vendor by searching by email
    public Vendor getVendorIdByEmail(String email) {
        open();
        Cursor cursor = mDb.query(VendorsEntry.TABLE_NAME, mAllColumns, VendorsEntry.COL_EMAIL + " = ?",
                new String[]{String.valueOf(email)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Vendor vendor = cursorToVendor(cursor);
        cursor.close();
        mDb.close();
        close();
        return vendor;
    }

    //used to add vendor into database
    public Vendor addVendor(String first, String last, String email, String password, String phone, String streetName, String houseNum,
            String zipCode, String city, String state) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(VendorsEntry.COL_FIRST_NAME, first);
        cv.put(VendorsEntry.COL_LAST_NAME, last);
        cv.put(VendorsEntry.COL_EMAIL, email);
        cv.put(VendorsEntry.COL_PASSWORD, password);
        cv.put(VendorsEntry.COL_PHONE_NUMBER, phone);
        cv.put(VendorsEntry.COL_STREET_NAME, streetName);
        cv.put(VendorsEntry.COL_HOUSE_NUMBER, houseNum);
        cv.put(VendorsEntry.COL_ZIP_CODE, zipCode);
        cv.put(VendorsEntry.COL_CITY, city);
        cv.put(VendorsEntry.COL_STATE, state);

        long insertId = mDb.insert(VendorsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(VendorsEntry.TABLE_NAME, mAllColumns, VendorsEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Vendor newVendor = cursorToVendor(cursor);
        cursor.close();
        mDb.close();
        close();
        return newVendor;
    }

    private Vendor cursorToVendor(Cursor cursor) {
        Vendor vendor = new Vendor();
        vendor.setM_Id(cursor.getLong(0));
        vendor.setM_FirstName(cursor.getString(1));
        vendor.setM_LastName(cursor.getString(2));
        vendor.setM_Email(cursor.getString(3));
        vendor.setM_Password(cursor.getString(4));
        vendor.setM_PhoneNumber(cursor.getString(5));
        vendor.setM_StreetName(cursor.getString(6));
        vendor.setM_HouseNumber(cursor.getString(7));
        vendor.setM_ZipCode(cursor.getString(8));
        vendor.setM_City(cursor.getString(9));
        vendor.setM_State(cursor.getString(10));
        cursor.close();
        return vendor;
    }


    //column and table names
    public static final class VendorsEntry implements BaseColumns {
        public static final String TABLE_NAME = "vendors";
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
