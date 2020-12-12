package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Customer;
import com.example.foodtruck.Models.Invoice;
import com.example.foodtruck.Models.Order;
import com.example.foodtruck.Models.Payment;

import java.util.ArrayList;
import java.util.Objects;

public class InvoiceContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    public InvoiceContract(Context context) {
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
            InvoiceEntry._ID,
            InvoiceEntry.COL_INVOICE_DATE,
            InvoiceEntry.COL_TOTAL,
            InvoiceEntry.COL_SERVICE_CHARGE,
            InvoiceEntry.COL_TAX_AMOUNT,
            InvoiceEntry.COL_TOTAL_INVOICE_AMOUNT,
            InvoiceEntry.COL_ORDER_ID,
            InvoiceEntry.COL_PAYMENT_ID,
            InvoiceEntry.COL_CUSTOMER_ID
    };

    //add order into database
    public Invoice createInvoice(String invoiceDate, String total, String serviceCharge, String taxAmount, String totalInvoiceAmount, long orderId, long paymentId, long customerId) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(InvoiceEntry.COL_INVOICE_DATE, invoiceDate);
        cv.put(InvoiceEntry.COL_TOTAL, total);
        cv.put(InvoiceEntry.COL_SERVICE_CHARGE, serviceCharge);
        cv.put(InvoiceEntry.COL_TAX_AMOUNT, taxAmount);
        cv.put(InvoiceEntry.COL_TOTAL_INVOICE_AMOUNT, totalInvoiceAmount);
        cv.put(InvoiceEntry.COL_ORDER_ID, orderId);
        cv.put(InvoiceEntry.COL_PAYMENT_ID, paymentId);
        cv.put(InvoiceEntry.COL_CUSTOMER_ID, customerId);

        long insertId = mDb.insert(InvoiceEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(InvoiceEntry.TABLE_NAME, mAllColumns, InvoiceEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Invoice newInvoice = cursorToInvoice(cursor, orderId, paymentId, customerId);
        cursor.close();
        mDb.close();
        close();
        return newInvoice;
    }

    //return Invoice by searching by id
    public Invoice getInvoiceById(long id) {
        open();
        Cursor cursor = mDb.query(InvoiceEntry.TABLE_NAME, mAllColumns, InvoiceEntry._ID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Invoice invoice = cursorToInvoice(cursor, cursor.getLong(cursor.getColumnIndex(InvoiceEntry.COL_ORDER_ID)), cursor.getLong(cursor.getColumnIndex(InvoiceEntry.COL_PAYMENT_ID)), cursor.getLong(cursor.getColumnIndex(InvoiceEntry.COL_CUSTOMER_ID)));
        cursor.close();
        mDb.close();
        close();
        return invoice;
    }

//    public ArrayList<Invoice> getInvoiceList(long orderID) {
//        open();
//        ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
//
//        Cursor cursor = mDb.query(InvoiceEntry.TABLE_NAME, mAllColumns, InvoiceEntry.COL_ORDER_ID + " =?",
//                new String[]{String.valueOf(orderID)}, null, null, null);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                Invoice invoice = cursorToInvoice(cursor, orderID, cursor.getColumnIndex(InvoiceEntry.COL_PAYMENT_ID), cursor.getColumnIndex(InvoiceEntry.COL_CUSTOMER_ID));
//                invoiceList.add(invoice);
//                if (cursor.isLast() || cursor.isClosed())
//                    break;
//                else
//                    cursor.moveToNext();
//            }
//
//        }
//        cursor.close();
//        mDb.close();
//        close();
//        return invoiceList;
//    }

    //return array List of invoice for customer
    public ArrayList<Invoice> getInvoiceListByCustomerId(long customerId) {
        open();
        ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();

        Cursor cursor = mDb.query(OrdersContract.OrdersEntry.TABLE_NAME, mAllColumns,   InvoiceEntry.COL_CUSTOMER_ID + " =? ", new String[]{String.valueOf(customerId)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Invoice invoice = cursorToInvoice(cursor, cursor.getLong(cursor.getColumnIndex(InvoiceEntry.COL_ORDER_ID)), cursor.getLong(cursor.getColumnIndex(InvoiceEntry.COL_ORDER_ID)), customerId );
                invoiceList.add(invoice);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
        }
        cursor.close();
        mDb.close();
        close();
        return invoiceList;
    }

    //set data to specific invoice object
    protected Invoice cursorToInvoice(Cursor cursor, long orderId, long paymentId, long customerId) {
        Invoice invoice = new Invoice();
        invoice.setM_Id(cursor.getLong(0));
        invoice.setM_InvoiceDate(cursor.getString(1));
        invoice.setM_Total(cursor.getString(2));
        invoice.setM_ServiceCharge(cursor.getString(3));
        invoice.setM_TaxAmount(cursor.getString(4));
        invoice.setM_TotalInvoiceAmount(cursor.getString(5));

        //get The Order by id
        OrdersContract oc = new OrdersContract(mContext);
        Order order = oc.getOrderById(orderId);
        if (oc != null) {
            invoice.setM_Order(order);
        }

        //get The Payment by id
        PaymentsContract pc = new PaymentsContract(mContext);
        Payment payment = pc.getPaymentById(paymentId);
        if (pc != null) {
            invoice.setM_Payment(payment);
        }

        //get the customer by id
        CustomersContract cc = new CustomersContract(mContext);
        Customer customer = cc.getCustomerById(customerId);
        if (cc != null){
            invoice.setM_Customer(customer);
        }

        return invoice;
    }

    //column and table names
    public static final class InvoiceEntry implements BaseColumns {
        public static final String TABLE_NAME = "invoice";
        public static final String COL_INVOICE_DATE = "invoiceDate";
        public static final String COL_TOTAL = "total";
        public static final String COL_SERVICE_CHARGE = "serviceCharge";
        public static final String COL_TAX_AMOUNT = "taxAmount";
        public static final String COL_TOTAL_INVOICE_AMOUNT = "totalInvoiceAmount";
        public static final String COL_ORDER_ID = "orderId";
        public static final String COL_PAYMENT_ID = "paymentId";
        public static final String COL_CUSTOMER_ID = "customerId";

    }

}
