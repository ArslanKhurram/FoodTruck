package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Payment;

import java.util.ArrayList;
import java.util.Objects;

//class to add payment data to customer in database
public final class PaymentsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open payments table
    public PaymentsContract(Context context) {
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
            PaymentsEntry._ID,
            PaymentsEntry.COL_PAYMENT_TYPE,
            PaymentsEntry.COL_NAME_ON_CARD,
            PaymentsEntry.COL_CARD_NUMBER,
            PaymentsEntry.COL_CC_EXP_DATE,
            PaymentsEntry.COL_CCV,
            PaymentsEntry.COL_DATE_ADDED,
            PaymentsEntry.COL_CUSTOMER_ID

    };

    //add payment into database
    public Payment createPayment(String paymentType, String name, String cardNumber, String exp, String ccv, String dateAdded, long customerID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(PaymentsEntry.COL_PAYMENT_TYPE, paymentType);
        cv.put(PaymentsEntry.COL_NAME_ON_CARD, name);
        cv.put(PaymentsEntry.COL_CARD_NUMBER, cardNumber);
        cv.put(PaymentsEntry.COL_CC_EXP_DATE, exp);
        cv.put(PaymentsEntry.COL_CCV, ccv);
        cv.put(PaymentsEntry.COL_DATE_ADDED, dateAdded);
        cv.put(PaymentsEntry.COL_CUSTOMER_ID, customerID);

        long insertId = mDb.insert(PaymentsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(PaymentsEntry.TABLE_NAME, mAllColumns, PaymentsEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Payment newPayment = cursorToPayment(cursor, customerID);
        cursor.close();
        mDb.close();
        close();
        return newPayment;
    }

    public void updatePayment(long id, String paymentType, String name, String cardNumber, String exp, String ccv, String dateAdded) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(PaymentsEntry.COL_PAYMENT_TYPE, paymentType);
        cv.put(PaymentsEntry.COL_NAME_ON_CARD, name);
        cv.put(PaymentsEntry.COL_CARD_NUMBER, cardNumber);
        cv.put(PaymentsEntry.COL_CC_EXP_DATE, exp);
        cv.put(PaymentsEntry.COL_CCV, ccv);
        cv.put(PaymentsEntry.COL_DATE_ADDED, dateAdded);

        mDb.update(PaymentsEntry.TABLE_NAME, cv, PaymentsEntry._ID + " = " + id, null);
        close();

    }

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //return array list of items from a particular menu
    public ArrayList<Payment> paymentsList(long customerID) {
        open();
        ArrayList<Payment> paymentsList = new ArrayList<Payment>();

        Cursor cursor = mDb.query(PaymentsEntry.TABLE_NAME, mAllColumns, PaymentsEntry.COL_CUSTOMER_ID + " =?",
                new String[]{String.valueOf(customerID)}, null, null, null);

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Payment payment = cursorToPayment(cursor, customerID);
                paymentsList.add(payment);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
            cursor.close();
            return paymentsList;
        }
        mDb.close();
        close();
        return null;
    }

    public Payment getPaymentById(long id) {
        open();
        Cursor cursor = mDb.query(PaymentsEntry.TABLE_NAME, mAllColumns, PaymentsEntry._ID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Payment payment = cursorToPayment(cursor, cursor.getLong(cursor.getColumnIndex(PaymentsEntry.COL_CUSTOMER_ID)));
        cursor.close();
        mDb.close();
        close();
        return payment;
    }

    //check for empty table
    public boolean checkForEmptyTable() {
        open();
        mDb = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + PaymentsEntry.TABLE_NAME;
        Cursor cursor = mDb.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        mDb.close();
        cursor.close();
        close();
        return icount <= 0;
    }

    //remove item from database
    public void removePayment(long id) {
        open();
        mDb = mDbHelper.getWritableDatabase();
        String dlQuery = "DELETE FROM " + PaymentsEntry.TABLE_NAME + " WHERE " + PaymentsEntry._ID + " = " + id;
        Cursor cursor = mDb.rawQuery(dlQuery, null);
        cursor.moveToFirst();
        cursor.close();
        mDb.close();
        close();
    }

    //column and table names
    public static final class PaymentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "payments";
        public static final String COL_PAYMENT_TYPE = "paymentType";
        public static final String COL_NAME_ON_CARD = "nameOnCard";
        public static final String COL_CARD_NUMBER = "cardNumber";
        public static final String COL_CC_EXP_DATE = "CCEXPDATE";
        public static final String COL_CCV = "ccv";
        public static final String COL_DATE_ADDED = "dateAdded";
        public static final String COL_CUSTOMER_ID = "customer_id";
    }

    //set data to specific payment object
    protected Payment cursorToPayment(Cursor cursor, long id) {
        Payment payment = new Payment();
        payment.setM_ID(cursor.getLong(0));
        payment.setM_PaymentType(cursor.getString(1));
        payment.setM_NameOnCard(cursor.getString(2));
        payment.setM_CreditCardNumber(cursor.getString(3));
        payment.setM_CCEXPDATE(cursor.getString(4));
        payment.setM_CCV(cursor.getString(5));
        payment.setM_DateAdded(cursor.getString(6));

        //get The Customer by id
        CustomersContract contract = new CustomersContract(mContext);
        Customer customer = contract.getCustomerById(id);
        if (contract != null) {
            payment.setmCustomer(customer);
        }
        return payment;
    }
}
