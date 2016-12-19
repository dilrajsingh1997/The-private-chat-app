package com.example.dilrajsingh.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "contactsDB.db";
    public static final String TABLE_PRODUCTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_NAME = "name";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER AUTO INCREMENT, " +
                COLUMN_NAME + " TEXT PRIMARY KEY, " + COLUMN_PHONE + " TEXT " +
                ");";
        //String query = "CREATE TABLE products( _id INTEGER PRIMARY KEY, productname TEXT, productquantity TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void delTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void delEntry(String cxz){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PHONE + "=\"" + cxz + "\";");
    }

    public void onUpdate(String cx, String name){
        String temp = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_NAME + " = '" + name + "' WHERE " + COLUMN_PHONE  + " = '" + cx + "'";

        db.execSQL(query);
        db.close();
    }

    public void addItem(ItemAdded item){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, item.getphone());
        values.put(COLUMN_NAME, item.getName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public ArrayList<String> databaseToString(){
        ArrayList<String> dbString = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("phone")) != null) {
                dbString.add(c.getString(c.getColumnIndex("name")));
                dbString.add(c.getString(c.getColumnIndex("phone")));
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String dataToPhone(Integer x){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        x--;

        while (x>=0) {
            c.moveToNext();
            x--;
        }
        String k =  c.getString(c.getColumnIndex("phone"));
        db.close();
        return k;
    }

    public ArrayList<String> databaseToPhone(){
        ArrayList<String> dbString = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("phone")) != null) {
                dbString.add(c.getString(c.getColumnIndex("phone")));
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String getPhone(String x){
        String temp = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("phone")) != null && c.getString(c.getColumnIndex("name")).equals(x)) {
                temp = c.getString(c.getColumnIndex("phone"));
            }
            c.moveToNext();
        }
        db.close();
        return temp;
    }

    public ArrayList<String> databaseToName(){
        ArrayList<String> dbString = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("phone")) != null) {
                dbString.add(c.getString(c.getColumnIndex("name")));
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

}
