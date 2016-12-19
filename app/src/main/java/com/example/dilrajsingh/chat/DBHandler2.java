package com.example.dilrajsingh.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class DBHandler2 extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "chatsDB.db";
    public static final String TABLE_PRODUCTS = "chats";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CHAT = "chat";
    public static final String COLUMN_TO = "sendto";
    public static final String COLUMN_DATE = "sentdate";

    public DBHandler2(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " + COLUMN_TO + " TEXT, " + COLUMN_CHAT + " TEXT " +
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

    /*public void delEntry(int cxz){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        db.execSQL("DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PHONE + "=\"" + cxz + "\";");
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(cxz)) {
                dbString.add(c.getString(c.getColumnIndex("phone")));
            }
            c.moveToNext();
        }
        db.close();
    }*/

    public void addItem(String date, String sendto, String message){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TO, sendto);
        values.put(COLUMN_CHAT, message);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public ArrayList<String> dataToDate(String whose){
        ArrayList<String> ar = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(whose)) {
                ar.add(c.getString(c.getColumnIndex("sentdate")));
            }
            c.moveToNext();
        }
        db.close();
        return ar;
    }

    public int getDateCount(String whose, String date){
        int x = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(whose)) {
                if(c.getString(c.getColumnIndex("sentdate")).equals(date)){
                    x++;
                }
            }
            c.moveToNext();
        }
        db.close();
        return x;
    }

    public ArrayList<String> dataToChat(String whose, String date){
        ArrayList<String> ar = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(whose)) {
                if(c.getString(c.getColumnIndex("sentdate")).equals(date)){
                    ar.add(c.getString(c.getColumnIndex("chat")));
                }
            }
            c.moveToNext();
        }
        db.close();
        return ar;
    }

    public ArrayList<String> dataToChatf(String whose, String date){
        ArrayList<String> ar = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(whose)) {
                if(c.getString(c.getColumnIndex("sentdate")).equals(date)){
                    ar.add(c.getString(c.getColumnIndex("chat")));
                }
            }
            c.moveToNext();
        }
        db.close();
        return ar;
    }

    public ArrayList<String> databaseToString(String key){
        ArrayList<String> dbString = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")).equals(key)) {
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

    public ArrayList<String> databaseToChat(){
        ArrayList<String> dbString = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("sendto")) != null) {
                dbString.add(c.getString(c.getColumnIndex("sendto")) + " : " + c.getString(c.getColumnIndex("sentdate")) + " : " + c.getString(c.getColumnIndex("chat")));
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

}
