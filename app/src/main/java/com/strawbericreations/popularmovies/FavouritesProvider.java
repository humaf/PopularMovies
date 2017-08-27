package com.strawbericreations.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by redrose on 8/22/17.
 */

public class FavouritesProvider extends ContentProvider {

    static final String TAG = FavouritesProvider.class.getSimpleName();

    //To query content provider, we should specify the query string in the form of a URI of the below format
    // <prefix>://<authority>/<data_type>/<id>
    // <prefix> is always set to content://
    // authority specifies the name of the content provider. For third party content providers this should be fully qualified name
    // data_type indicates the particular data the provider provides.

    static final String PROVIDER_NAME = "com.strawbericreations.popularmovies.FavouritesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/favorites";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final int FAVORITES = 1;
    static final int FAVORITES_ID = 2;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavouritesDBHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouritesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority,FavouritesContract.PATH_MOVIES, FAVORITES);
        matcher.addURI(authority, FavouritesContract.PATH_MOVIES + "/#", FAVORITES_ID);

        return matcher;
    }

   // static final UriMatcher uriMatcher;
  /*
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"favorites",FAVORITES);
        uriMatcher.addURI(PROVIDER_NAME, "favorites/#", FAVORITES_ID);
    }
    */

    private SQLiteDatabase favDB;

    @Override
    public boolean onCreate() {
        Log.e(TAG,"FavoritesProvider onCreate called" );
        Context context = getContext();
        FavouritesDBHelper dbHelper = new FavouritesDBHelper(context);
        favDB = dbHelper.getWritableDatabase();
        return (favDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (match) {
            case FAVORITES:
                qb.setTables(FavouritesContract.FavouriteEntry.TABLE_FAVOURITES);
                Log.e(TAG,"query uriMatcher favorites");
                break;
            case FAVORITES_ID: {
                qb.setTables(FavouritesContract.FavouriteEntry.TABLE_FAVOURITES);
                Log.e(TAG, "query uriMatcher FAVORITES_ID");
                qb.appendWhere(FavouritesContract.FavouriteEntry.COLUMN_ID + "=" +
                        uri.getLastPathSegment());
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursor = qb.query (
                favDB,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.setNotificationUri(getContext().getContentResolver(),CONTENT_URI);
        return cursor;

    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /** Add a new favorites record */

        long rowID = favDB.insert(FavouritesContract.FavouriteEntry.TABLE_FAVOURITES, "", values);
        Log.e(TAG,"FavoritesProvider insert rowID:"+rowID);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        try {
            throw new SQLException("Failed to add new record into "+uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                count = favDB.delete(FavouritesContract.FavouriteEntry.TABLE_FAVOURITES, selection, selectionArgs);
                break;
            case FAVORITES_ID:

                String id = uri.getPathSegments().get(1);
                count = favDB.delete(FavouritesContract.FavouriteEntry.TABLE_FAVOURITES,FavouritesContract.FavouriteEntry.COLUMN_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case FAVORITES:
                rowsUpdated = db.update(
                        FavouritesContract.FavouriteEntry.TABLE_FAVOURITES,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
