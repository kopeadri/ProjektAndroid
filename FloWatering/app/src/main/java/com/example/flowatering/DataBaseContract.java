package com.example.flowatering;

import android.provider.BaseColumns;

public final class DataBaseContract {
    //prywatny konstruktor
    private DataBaseContract() {}

    //zawartosc tablic w bazie
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "plants";
        public static final String NAME = "name";
        public static final String SPECIES = "species";
        public static final String IS_WATERED = "is_watered";
        public static final String LAST_WATERED = "last_watered";
        public static final String HOW_OFTEN = "how_often";
        public static final String PHOTO = "photo";
    }


// FeedEntry._ID - automatyczny primary key jesli chcemy je numerowac



}
