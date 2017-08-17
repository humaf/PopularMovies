package com.strawbericreations.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by redrose on 8/13/17.
 */

public class Trailers {

    private Uri trailerUri;
    private String trailerName;
    private String id;
    private String key;

    public Trailers(Uri trailerUri, String trailerName,String id,String key) {
        this.trailerUri = trailerUri;
        this.trailerName = trailerName;
        this.id = id;
        this.key = key;
    }

    public String getTrailerName(){ return trailerName;}

    public void setTrailerName(String trailerName){this.trailerName = trailerName;}

    public Uri getTrailerUri(){ return trailerUri;}

    public void setTrailerUri(Uri trailerUri){ this.trailerUri = trailerUri ;}



    public String getId(){
        return id;
    }

    public void setId(String id){this.id = id;}

    public String getKey(){
        return key;
    }

    public void setKey(String key){

        this. key = key;
    }



}
