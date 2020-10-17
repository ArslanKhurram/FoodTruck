package com.example.foodtruck.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.Models.Customer;

import static com.example.foodtruck.DataBase.CustomersContract.CustomersEntry.TABLE_NAME;

///class for admin login and report
public final class AdminContract {

    // Database fields;
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //reference to table column name
    private String[] mAllColumns = {
            AdminEntry._ID,
            AdminEntry.COL_EMAIL,
            AdminEntry.COL_PASSWORD,
    };

    //used to add admin into database
    public Admin addAdminByObject(Admin admin) {
        open();
        ContentValues av = new ContentValues();
        av.put(AdminEntry.COL_EMAIL, admin.getM_Email());
        av.put(AdminEntry.COL_PASSWORD, admin.getM_Password());

        long insertId = mDb.insert(AdminEntry.TABLE_NAME, null, av);
        Cursor cursor = mDb.query(AdminEntry.TABLE_NAME, mAllColumns, AdminEntry._ID + " = " +
                insertId, null, null, null, null);
        cursor.moveToFirst();
        Admin newAdmin = cursorToAdmin(cursor);
        cursor.close();
        mDb.close();
        close();
        return newAdmin;
    }

    //used to add admin into database
    public Admin addAdmin(String email, String password){
        open();
        ContentValues av = new ContentValues();
        av.put(AdminEntry.COL_EMAIL,email);
        av.put(AdminEntry.COL_PASSWORD,password);

        long insertId = mDb.insert(AdminEntry.TABLE_NAME,null, av);
        Cursor cursor = mDb.query(AdminEntry.TABLE_NAME, mAllColumns, AdminEntry._ID + " = " + insertId,
                null,null,null,null);
        cursor.moveToFirst();
        Admin newAdmin = cursorToAdmin(cursor);
        cursor.close();
        mDb.close();
        close();
        return newAdmin;

    }

    //Check for empty table
    public boolean checkForEmptyTable() {
        open();
        mDb = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) From " + AdminEntry.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        mDb.close();
        cursor.close();
        close();
        return icount <= 0;
    }

    //return boolean after checking email and identification key for admin login
    public boolean validateAdmin(String email, String password) {
        open();
        String selection = AdminEntry.COL_EMAIL + "=?" + " and " + AdminEntry.COL_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = mDb.query(AdminEntry.TABLE_NAME, mAllColumns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        mDb.close();
        cursor.close();
        close();

        if (count > 0)
            return true;
        else
            return false;
    }

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //return Admin by id
    public Admin getAdminByID(long id) {
        open();
        Cursor cursor = mDb.query(AdminEntry.TABLE_NAME, mAllColumns, AdminEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Admin admin = cursorToAdmin(cursor);
        cursor.close();
        mDb.close();
        close();
        return admin;
    }

    //return Admin by Searching by email
    public Admin getAdminIdByEmail(String email){
        open();
        Cursor cursor = mDb.query(AdminEntry.TABLE_NAME,mAllColumns,AdminEntry.COL_EMAIL + " = ?",
                new String[]{String.valueOf(email)}, null, null, null);
        if (cursor == null){
            return new Admin();
        }
        cursor.moveToFirst();
        Admin admin = cursorToAdmin(cursor);
        cursor.close();
        mDb.close();
        close();
        return admin;
    }

    //open admin table
    public AdminContract(Context context) {
        mDbHelper = new DbHelper(context);
        this.mContext = context;
        //open database
        try {
            open();
        } catch (SQLException e) {
            Log.e("ChildContract", "SQLException on opening database " + e.getMessage());
        }
    }

    //setters
    private Admin cursorToAdmin(Cursor cursor) {
        Admin admin = new Admin();
        admin.setM_Id(cursor.getLong(0));
        admin.setM_Email(cursor.getString(1));
        admin.setM_Password(cursor.getString(2));

        cursor.close();
        return admin;
    }

    //column and table names
    public static final class AdminEntry implements BaseColumns {
        public static final String TABLE_NAME = "admin";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASSWORD = "password";

    }



}//end class


