package com.example.foodtruck.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.foodtruck.DataBase.PaymentsContract.PaymentsEntry;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "food_truck.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //query to create users table
    public static final String SQL_CREATE_PAYMENTS_TABLE = "CREATE TABLE " + PaymentsEntry.TABLE_NAME + " (" +
            PaymentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PaymentsEntry.COL_PAYMENT_TYPE + " TEXT NOT NULL, " +
            PaymentsEntry.COL_NAME_ON_CARD + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CC_EXP_DATE + " TEXT NOT NULL, " +
            PaymentsEntry.COL_CCV + " TEXT NOT NULL, " +
            PaymentsEntry.COL_DATE_ADDED + " TEXT NOT NULL" +
            "); ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PAYMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PaymentsEntry.TABLE_NAME);

    }
}
