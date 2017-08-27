package com.strawbericreations.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by redrose on 8/22/17.
 */

public class FavouritesContract implements BaseColumns {


        private FavouritesContract() {

        }


    public static final String PATH_MOVIES = "movies";

    public static final String CONTENT_AUTHORITY="com.strawbericreations.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static abstract class FavouriteEntry implements BaseColumns{
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
        public static final String PATH_MOVIES = "movies";

      // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
               ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;

        // for building URIs on insertion
        public static Uri buildFavouritesUri(int id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] PROJECTION_ALL =
                {COLUMN_ID, COLUMN_IMAGE, COLUMN_TITLE, COLUMN_OVERVIEW,
                        COLUMN_Average_VOTE, COLUMN_RELEASE_DATE, COLUMN_REVIEWS, COLUMN_TRAILERS};

    }
}
