package com.example.myeditablelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

/**
 * Created by Mitch on 2016-05-13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "costCollector.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "HOST";
    public static final String COL3 = "ADDRESS";
    public static final String COL4 = "NAME";
    public static final String COL5 = "AUTHOR";
    public static final String COL6 = "CURRENT_PRICE";
    public static final String COL7 = "ORIGINAL_PRICE";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " HOST TEXT, ADDRESS TEXT, " +
                " NAME TEXT, " + " AUTHOR TEXT, " + " CURRENT_PRICE TEXT, " + " ORIGINAL_PRICE TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String host, String address, String name, String author, String cprice, String oprice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, host);
        contentValues.put(COL3, address);
        contentValues.put(COL4, name);
        contentValues.put(COL5, author);
        contentValues.put(COL6, cprice);
        contentValues.put(COL7,oprice);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getListContents(String host){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL2 + " = '" + host +  "'", null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String address, String price1, String price2){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL6 +
                " = '" + price1 + "' WHERE " + COL3 + " = '" + address + "'";
        db.execSQL(query);
        query = "UPDATE " + TABLE_NAME + " SET " + COL7 +
                " = '" + price2 + "' WHERE " + COL3 + " = '" + address + "'";
        db.execSQL(query);
    }

    public void deleteName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL4 + " = '" + name +  "'";
        db.execSQL(query);
    }
}