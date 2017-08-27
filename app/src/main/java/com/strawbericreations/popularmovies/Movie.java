package com.strawbericreations.popularmovies;


import java.io.Serializable;


/**
 * Created by redrose on 7/25/17.
 */
public class Movie implements Serializable {
    private int id;
    private String title;
    private String image;
    private String overview;
    private int vote_average;
    private String release_date;
    public String reviews;
    public String trailers;
    private String original_title;



 //   public Movie(){
    //    super();
    //}


    public Movie(int id, String title, String image, String overview,
                 int vote_average, String release_date){
        this.id = id;
        this.title = title;
        this.image = image;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
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

    public String getReviews(){ return reviews;}

    public void setReviews(String reviews){
        this.reviews = reviews;
    }

    public String getTrailers(){ return trailers;}

    public void setTrailers(String trailers){
        this.trailers = trailers;
    }

}
