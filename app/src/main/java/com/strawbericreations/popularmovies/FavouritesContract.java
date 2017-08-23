package com.strawbericreations.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by redrose on 8/22/17.
 */

public class FavouritesContract implements BaseColumns {


    public static final String CONTENT_AUTHORITY = "com.strawbericreations.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class FavouriteEntry implements BaseColumns{
        // table name
        public static final String TABLE_FAVOURITES = "favourites";
        // columns
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_Average_VOTE = "average_vote";
        public static final String COLUMN_TRAILERS ="trailers";
        public static final String COLUMN_REVIEWS = "reviews";


        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVOURITES).build();
        // create cursor of base type directory for multiple entries
     //   public static final String CONTENT_DIR_TYPE =
       //         ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;

        // for building URIs on insertion
        public static Uri buildFlavorsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
