package com.example.foodtruck.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.foodtruck.Models.Item;
import com.example.foodtruck.Models.Option;
import com.example.foodtruck.Models.OrderedItemOptions;

import java.util.ArrayList;
import java.util.Objects;

//class to add options data to database
public final class OptionsContract {

    //initialize sql database
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;
    private Context mContext;

    //constructor to open options table
    public OptionsContract(Context context) {
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
            OptionsEntry._ID,
            OptionsEntry.COL_OPTION,
            OptionsEntry.COL_ITEM_ID
    };

    //open database
    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    //close database
    public void close() {
        mDbHelper.close();
    }

    //add option into database
    public Option createOption(String option, long itemID) {
        open();
        ContentValues cv = new ContentValues();
        cv.put(OptionsEntry.COL_OPTION, option);
        cv.put(OptionsEntry.COL_ITEM_ID, itemID);

        long insertId = mDb.insert(OptionsEntry.TABLE_NAME, null, cv);
        Cursor cursor = mDb.query(OptionsEntry.TABLE_NAME, mAllColumns, OptionsEntry._ID +
                " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Option newOption = cursorToOption(cursor, itemID);
        cursor.close();
        mDb.close();
        close();
        return newOption;
    }

    //return Item by searching by id
    public Option getOptionById(long optionID) {
        open();
        Cursor cursor = mDb.query(OptionsEntry.TABLE_NAME, mAllColumns, OptionsEntry._ID + " = ?",
                new String[]{String.valueOf(optionID)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Option option = cursorToOption(cursor, cursor.getLong(cursor.getColumnIndex(OptionsEntry.COL_ITEM_ID)));
        cursor.close();
        mDb.close();
        close();
        return option;
    }

    //return array list of options for a particular item
    public ArrayList<Option> getOptionsListByItemID(long itemID) {
        open();
        boolean check = checkIfOptionsExist(itemID);

        if (check) {
            ArrayList<Option> optionsList = new ArrayList<Option>();

            Cursor cursor = mDb.query(OptionsEntry.TABLE_NAME, mAllColumns, OptionsEntry.COL_ITEM_ID + " =?",
                    new String[]{String.valueOf(itemID)}, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Option option = cursorToOption(cursor, itemID);
                optionsList.add(option);
                if (cursor.isLast() || cursor.isClosed())
                    break;
                else
                    cursor.moveToNext();
            }
            cursor.close();
            return optionsList;
        }
        mDb.close();
        close();
        return null;
    }

    //check is options exist for an item
    public boolean checkIfOptionsExist(long ItemID) {
        open();
        Cursor cursor = mDb.query(OptionsEntry.TABLE_NAME, mAllColumns, OptionsEntry.COL_ITEM_ID + "=? ",
                new String[]{String.valueOf(ItemID)}, null, null, null);

        boolean check = cursor.getCount() > 0;
        cursor.close();
        return check;
    }

    //remove options of an item
    public void removeOptionsByItemID(long ItemID) {
        open();
        boolean check = checkIfOptionsExist(ItemID);

        if (check) {
            mDb = mDbHelper.getWritableDatabase();
            String dlQuery = "DELETE FROM " + OptionsEntry.TABLE_NAME + " WHERE " + OptionsEntry.COL_ITEM_ID + " = " + ItemID;
            Cursor cursor = mDb.rawQuery(dlQuery, null);
            cursor.moveToNext();
            cursor.close();
        }
        mDb.close();
        close();
    }


    //set data to specific option object
    protected Option cursorToOption(Cursor cursor, long itemID) {
        Option option = new Option();
        option.setM_Id(cursor.getLong(0));
        option.setM_Option(cursor.getString(1));

        //get The Customer by id
        ItemsContract contract = new ItemsContract(mContext);
        Item item = contract.getItemById(itemID);
        if (contract != null) {
            option.setM_Item(item);
        }
        return option;
    }

    //column and table names
    public static final class OptionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "options";
        public static final String COL_OPTION = "option";
        public static final String COL_ITEM_ID = "item_ID";
    }
}
