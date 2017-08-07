package com.strawbericreations.popularmovies;


import java.io.Serializable;


/**
 * Created by redrose on 7/25/17.
 */
public class Movie implements Serializable {
    private String image;
    private String title;
    private int id;
    private String original_title;
    private String overview;
    private String release_date;
    private int vote_average;


    public Movie(){
        super();
    }

    public String getImage(){  return image;  }

    public void setImage(String image){
        this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title= title;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }



}
