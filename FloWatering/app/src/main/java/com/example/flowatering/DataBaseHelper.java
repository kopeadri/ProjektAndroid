package com.example.flowatering;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // po zmianie schemy zmieniamy nr wersji bazy
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlantsDataBase.db";
    private static final String DATABASE_PATH = "/data/data/com.example.flowatering/databases/";
    //creating a table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseContract.FeedEntry.TABLE_NAME + " (" +
                    DataBaseContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.FeedEntry.NAME + " TEXT," +
                    DataBaseContract.FeedEntry.SPECIES + " TEXT," +
                    DataBaseContract.FeedEntry.IS_WATERED + " INTEGER," +
                    DataBaseContract.FeedEntry.LAST_WATERED + " TEXT," +
                    DataBaseContract.FeedEntry.HOW_OFTEN + " INTEGER," +
                    DataBaseContract.FeedEntry.PHOTO + " TEXT)";

    //deleting a table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataBaseContract.FeedEntry.TABLE_NAME;

    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        if(dbFile.exists()){
            Log.d("Ok","Database exsists");
        }else{
            dbFile.getParentFile().mkdirs();
            Log.d("CreatedPath","Made the right directory");
        }
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

    public void clean() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertFlower (String name,String species,int is_watered,String last_watered,int how_often,String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.FeedEntry.NAME,name);
        values.put(DataBaseContract.FeedEntry.SPECIES,species);
        values.put(DataBaseContract.FeedEntry.IS_WATERED,is_watered);
        values.put(DataBaseContract.FeedEntry.LAST_WATERED,last_watered);
        values.put(DataBaseContract.FeedEntry.HOW_OFTEN,how_often);
        values.put(DataBaseContract.FeedEntry.PHOTO,photo);
        db.insert(DataBaseContract.FeedEntry.TABLE_NAME, null, values);
        return true;
    }

    public ArrayList<String> getData(String name) {
        //String name_ = "Kaktusik";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> element = new ArrayList<String>();
        Cursor res =  db.rawQuery( "select * from "+DataBaseContract.FeedEntry.TABLE_NAME+"  where name = '"+name+"'", null );
        res.moveToFirst();
        element.add(res.getString(res.getColumnIndex(DataBaseContract.FeedEntry.SPECIES)));
        element.add(Integer.toString(res.getInt(res.getColumnIndex(DataBaseContract.FeedEntry.IS_WATERED))));
        element.add(res.getString(res.getColumnIndex(DataBaseContract.FeedEntry.LAST_WATERED)));
        element.add(Integer.toString(res.getInt(res.getColumnIndex(DataBaseContract.FeedEntry.HOW_OFTEN))));
        element.add(res.getString(res.getColumnIndex(DataBaseContract.FeedEntry.PHOTO)));

        res.close();
        return element;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DataBaseContract.FeedEntry.TABLE_NAME);
        return numRows;
    }

    public Integer deleteFlower (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataBaseContract.FeedEntry.TABLE_NAME,
                "_id = ? ",
                new String[] { Integer.toString(id) });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<List<String>> getAllFlowers() {
        List<List<String>> array_list = new ArrayList<List<String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DataBaseContract.FeedEntry.TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            List<String> data = new ArrayList<String>();
            data.add(res.getString(res.getColumnIndex(DataBaseContract.FeedEntry.NAME)));
            data.add(Integer.toString(res.getInt(res.getColumnIndex(DataBaseContract.FeedEntry.IS_WATERED))));
            array_list.add(data);
            updateIfIsNotWatered(Integer.toString(res.getInt(res.getColumnIndex(DataBaseContract.FeedEntry._ID))));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean updateFlower (String name,String species,int is_watered,String last_watered,int how_often,String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.FeedEntry.NAME,name);
        values.put(DataBaseContract.FeedEntry.SPECIES,species);
        values.put(DataBaseContract.FeedEntry.IS_WATERED,is_watered);
        values.put(DataBaseContract.FeedEntry.LAST_WATERED,last_watered);
        values.put(DataBaseContract.FeedEntry.HOW_OFTEN,how_often);
        values.put(DataBaseContract.FeedEntry.PHOTO,photo);
        db.update(DataBaseContract.FeedEntry.TABLE_NAME, values, "name = ? ", new String[] {name} );
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateIfIsNotWatered(String Id){  //uruchamiana raz dziennie na kazdym rekordzie, zeby zupdateowac czy juz wymaga podlania
        //DataBaseHelper dbHelper = new DataBaseHelper(formContext);
        SQLiteDatabase db = getWritableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DataBaseContract.FeedEntry.TABLE_NAME+" where _id = '"+Integer.parseInt(Id)+"'", null );
        res.moveToFirst();
        //obliczanie liczby dni pomiedzy podlewaniami
        Date now = new Date();
        String last = res.getString(res.getColumnIndex(DataBaseContract.FeedEntry.LAST_WATERED));

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            c.setTime(sdf.parse(last));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Instant instantFromDate = now.toInstant();
        int days = daysBetween(now,c.getTime());

        int nr_of_days = res.getInt(res.getColumnIndex(DataBaseContract.FeedEntry.HOW_OFTEN));

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
    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
