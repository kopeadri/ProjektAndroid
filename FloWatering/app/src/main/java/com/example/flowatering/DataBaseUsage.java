package com.example.flowatering;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUsage {
    //creating a database

    long createWritable() {
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());

        //putting info to database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //mapa wartosci, nazwy kolumn to klucze
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.FeedEntry.NAME,name);
        values.put(DataBaseContract.FeedEntry.SPECIES,species);
        values.put(DataBaseContract.FeedEntry.IS_WATERED,is_watered);
        values.put(DataBaseContract.FeedEntry.HOW_OFTEN,how_often);
        values.put(DataBaseContract.FeedEntry.PHOTO,photo);

        // wstawianie nowego rzedu do bazy, zwracamy nr rzedu
        long newRowId = db.insert(DataBaseContract.FeedEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    void readDatabase(){
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


    // definiujemy ktore kolumny chcemy wyswietlac po tej komendzie
        String[] projection = {
                BaseColumns._ID,
                DataBaseContract.FeedEntry.NAME,
                DataBaseContract.FeedEntry.SPECIES,
                DataBaseContract.FeedEntry.IS_WATERED,
                DataBaseContract.FeedEntry.HOW_OFTEN,
                DataBaseContract.FeedEntry.PHOTO
        };

    // filtrujemy rezultaty WHERE "is_watered" = '0'  - wyswietlanie w aplikacji (można tez filtrować po nazwie)
    //czyli tylko te niepodlane

        String selection = DataBaseContract.FeedEntry.IS_WATERED + " = ?";
        String[] selectionArgs = { "0" };

        // w jakiej kolejnosci chcemy wyswietlac roslinki - najpierw te, które wymagaja czestszego podlewania
        String sortOrder =
                DataBaseContract.FeedEntry.HOW_OFTEN + " ASC";

        Cursor cursor = db.query(
                DataBaseContract.FeedEntry.TABLE_NAME,   // nazwa naszej tablicy z roslinami
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DataBaseContract.FeedEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();


    }

    int deleteFromDataBase(){
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());

        //putting info to database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // usuwanie rosliny o danej nazwie
        String selection = DataBaseContract.FeedEntry.NAME + " LIKE ?";
        // podajemy nazwe
        String[] selectionArgs = { "stokrotka" };
        // usuwamy
        int deletedRows = db.delete(DataBaseContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    void updateDataBase(){
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // nowa wartosc dla jakiejs kolumny
        String title = "NowaNazwa";
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.FeedEntry.NAME, title);

        // Which row to update, based on the title
        String selection = DataBaseContract.FeedEntry.NAME + " LIKE ?";
        String[] selectionArgs = { "StaraNazwa" };

        int count = db.update(
                DataBaseContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }



}
