package com.strawbericreations.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by redrose on 8/22/17.
 */

public class FavouritesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavouritesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 13;

    public FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " +
                FavouritesContract.FavouriteEntry.TABLE_FAVOURITES+ "(" + FavouritesContract.FavouriteEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                FavouritesContract.FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouritesContract.FavouriteEntry.COLUMN_IMAGE +
                " TEXT , " +
                FavouritesContract.FavouriteEntry.COLUMN_OVERVIEW +
                " TEXT , " +
                FavouritesContract.FavouriteEntry.COLUMN_Average_VOTE +
                " INTEGER NOT NULL," +
                FavouritesContract.FavouriteEntry.COLUMN_RELEASE_DATE +
                " TEXT );" ;

        db.execSQL(SQL_CREATE_FAVOURITE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + FavouritesContract.FavouriteEntry.TABLE_FAVOURITES);
       db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavouritesContract.FavouriteEntry.TABLE_FAVOURITES+ "'");
        // re-create database
        onCreate(db);

    }
}
