package com.example.jchen415.mywaytormobileapplication;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.*;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "HardwareSwap.db";

    //Database Table Names
    private static final String TABLE_CARDHOLDER = "Cardholder";
    private static final String TABLE_MENU1 = "Menu1";
    private static final String TABLE_HISTORY = "History";

    //CardHolder Table Column Names
    private static final String KEY_CARD_pID = "CARD_pID";
    private static final String KEY_CARD_NAME = "CARD_NAME";
    private static final String KEY_CARD_NUMBER = "CARD_NUMBER";
    private static final String KEY_CARD_EXPIRATIONDATE = "CARD_EXPIRATION";
    private static final String KEY_CARD_CVV = "CARD_CVV";
    private static final String KEY_CARD_USERADDRESS = "CARD_ADDRESS";
    private static final String KEY_CARD_USERZIPCODE = "CARD_ZIPCODE";
    private static final String KEY_CARD_USERCITY = "CARD_CITY";
    private static final String KEY_CARD_USERSTATE = "CARD_STATE";
    private static final String KEY_CARD_USERCOUNTRY = "CARD_COUNTRY";

    //Menu Table Column Names
    private static final String KEY_MENU_NAME = "MENU_NAME";
    private static final String KEY_MENU_VALUE = "MENU_VALUE";
    private static final String KEY_MENU_NUM = "MENU_NUMBER";

    //Default constructor which generates the database
    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints upon database create to ensure enforcement
            db.execSQL("PRAGMA foreign_keys=1;");
            Log.d("TAG", "Foreign Keys Constraint Enabled!!!");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Tables
        try {
            db.execSQL("create table " + TABLE_CARDHOLDER + " (CARD_pID INTEGER PRIMARY KEY NOT NULL, CARD_NAME TEXT NOT NULL, CARD_NUMBER INTEGER UNIQUE, CARD_EXPIRATION TEXT NOT NULL, CARD_CVV INTEGER NOT NULL, CARD_ADDRESS TEXT NOT NULL, CARD_ZIPCODE INTEGER NOT NULL, CARD_CITY TEXT NOT NULL, CARD_STATE TEXT NOT NULL, CARD_COUNTRY TEXT NOT NULL)");
            db.execSQL("create table " + TABLE_MENU1 + "(MENU_NAME TEXT NOT NULL, MENU_VALUE TEXT NOT NULL, MENU_NUMBER NOT NULL)");
            db.execSQL("create table " + TABLE_HISTORY + "(MENU_NAME TEXT NOT NULL, MENU_VALUE TEXT NOT NULL, MENU_NUMBER NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "Tables Created Successfully!!!");
    }

    //Database upgrade routine
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop previous table if exists on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDHOLDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        //Regenerate Tables
        onCreate(db);
        Log.d("TAG", "Tables Dropped Successfully!!!");
    }


    public boolean checkCardExists(String cards) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT CARD_NUMBER FROM " + TABLE_CARDHOLDER, null);
        String card;
        //Loop through the CardHolder table database for CARD_NUMBER column and if card matches the input parameter
        //return true if found, else return false
        if (cursor.moveToFirst()) {
            do {
                card = cursor.getString(0);
                if (card.equals(cards)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }


    //Database get card number
    public String getCardNumber() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT CARD_NUMBER FROM " + TABLE_CARDHOLDER, null);
        String cardNumber = "";
        while (c.moveToNext()) {
           cardNumber = c.getString(0);
        }
        c.close();
        return cardNumber;
    }

    //Database acquire cardholder primary ID for delete card
    public String getCardpID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT CARD_pID FROM " + TABLE_CARDHOLDER, null);
        String cardNumber = "";
        while (c.moveToNext()) {
            cardNumber = c.getString(0);
        }
        c.close();
        return cardNumber;
    }

    //Database validate card routine
    public boolean checkCard() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CARDHOLDER, null);

        if (c != null && (c.getCount() > 0)) {
            c.close();
            return true;
        }
        return false;
    }

    public boolean checkSummary1() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_MENU1, null);

        if (c != null && (c.getCount() > 0)) {
            c.close();
            return true;
        }
        return false;
    }

    //Database insert user card data routine
    public void insertCardData(CardHolder cardholder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_NAME, cardholder.getCardHolderName());
        values.put(KEY_CARD_NUMBER, cardholder.getCardNumber());
        values.put(KEY_CARD_EXPIRATIONDATE, cardholder.getExpirationDate());
        values.put(KEY_CARD_CVV, cardholder.getCvvNumber());
        values.put(KEY_CARD_USERADDRESS, cardholder.getUserAddress());
        values.put(KEY_CARD_USERZIPCODE, cardholder.getUserZipCode());
        values.put(KEY_CARD_USERCITY, cardholder.getUserCity());
        values.put(KEY_CARD_USERSTATE, cardholder.getUserState());
        values.put(KEY_CARD_USERCOUNTRY, cardholder.getUserState());

        db.insert(TABLE_CARDHOLDER, null, values);
        Log.d("TAG", "Cardholder Data Inserted Successfully!!!");
        db.close();
    }

    //Database delete user card data routine
    public void deleteCard(String pID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDHOLDER, KEY_CARD_pID + "=?", new String[]{pID});
        db.close();
    }

    //insert data for menu 1
    public void insert(order tes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MENU_NAME, tes.getFoodName());
        values.put(KEY_MENU_VALUE, tes.getFoodPrice());
        values.put(KEY_MENU_NUM, tes.getAmount());

        db.insert(TABLE_MENU1, null, values);
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }


    //get data from menu 1
    public Cursor getAllData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_MENU1, null);

        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU1, null, null);

    }

}