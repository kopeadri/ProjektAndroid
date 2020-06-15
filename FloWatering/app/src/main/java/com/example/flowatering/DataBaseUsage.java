package com.example.flowatering;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseUsage {
    //creating a database

    long createWritable() {
        DataBaseHelper dbHelper = new DataBaseHelper(MyApplication.getAppContext());

        //putting info to database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //mapa wartosci, nazwy kolumn to klucze
        ContentValues values = new ContentValues();
        String name = "Nazwa";
        String species = "gatunek1";
        int is_watered = 1;
        String last_watered = "14-06-2020";
        int how_often = 3;
        String photo = "www.kwiatek1.pl";
        values.put(DataBaseContract.FeedEntry.NAME,name);
        values.put(DataBaseContract.FeedEntry.SPECIES,species);
        values.put(DataBaseContract.FeedEntry.IS_WATERED,is_watered);
        values.put(DataBaseContract.FeedEntry.LAST_WATERED,last_watered);
        values.put(DataBaseContract.FeedEntry.HOW_OFTEN,how_often);
        values.put(DataBaseContract.FeedEntry.PHOTO,photo);

        // wstawianie nowego rzedu do bazy, zwracamy nr rzedu
        long newRowId = db.insert(DataBaseContract.FeedEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void readDatabase(){
        DataBaseHelper dbHelper = new DataBaseHelper(MyApplication.getAppContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        // definiujemy ktore kolumny chcemy wyswietlac po tej komendzie
        String[] projection = {
                BaseColumns._ID,
                DataBaseContract.FeedEntry.NAME,
                DataBaseContract.FeedEntry.SPECIES,
                DataBaseContract.FeedEntry.IS_WATERED,
                DataBaseContract.FeedEntry.LAST_WATERED,
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

            updateIfIsNotWatered(DataBaseContract.FeedEntry._ID);

            itemIds.add(itemId);
        }
        cursor.close();


    }

    int deleteFromDataBase(){
        DataBaseHelper dbHelper = new DataBaseHelper(MyApplication.getAppContext());

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
        DataBaseHelper dbHelper = new DataBaseHelper(MyApplication.getAppContext());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateIfIsNotWatered(String Id){  //uruchamiana raz dziennie na kazdym rekordzie, zeby zupdateowac czy juz wymaga podlania
        DataBaseHelper dbHelper = new DataBaseHelper(MyApplication.getAppContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //obliczanie liczby dni pomiedzy podlewaniami
        Date now = new Date();
        LocalDate last = LocalDate.parse(DataBaseContract.FeedEntry.LAST_WATERED);


        Instant instantFromDate = now.toInstant();
        int days = (int) ChronoUnit.DAYS.between(instantFromDate, last);
        System.out.println(days);

        int nr_of_days = Integer.parseInt(DataBaseContract.FeedEntry.HOW_OFTEN);

        if(days > nr_of_days){
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.FeedEntry.IS_WATERED, 0);

            // Which row to update, based on the id
            String selection = DataBaseContract.FeedEntry._ID + " LIKE ?";
            String[] selectionArgs = { Id };

            int count = db.update(
                    DataBaseContract.FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        }



    }



}
