package com.strawbericreations.popularmovies;

import android.widget.ImageView;

/**
 * Created by redrose on 7/25/17.
 */
public class Movie {
    private String image;
    private String title;

    public Movie(){
        super();
    }
    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title= title;
    }

}
