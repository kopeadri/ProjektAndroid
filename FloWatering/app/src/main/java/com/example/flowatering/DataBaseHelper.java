package com.example.flowatering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // po zmianie schemy zmieniamy nr wersji bazy
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PlantsDataBase.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // wymazujemy dane z bazy i zastepujemy nowymi po upgradzie
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //creating a table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseContract.FeedEntry.TABLE_NAME + " (" +
                    DataBaseContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.FeedEntry.NAME + " TEXT," +
                    DataBaseContract.FeedEntry.SPECIES + " TEXT," +
                    DataBaseContract.FeedEntry.IS_WATERED + " INTEGER," +
                    DataBaseContract.FeedEntry.HOW_OFTEN + " INTEGER," +
                    DataBaseContract.FeedEntry.PHOTO + " TEXT)";

    //deleting a table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataBaseContract.FeedEntry.TABLE_NAME;
























}