package com.test.movies.db.entity;

/**
 * Created by waldek on 05.04.17.
 */

public class Movie {

    protected int id;

    protected int TMDBId; // The Movie DataBase ID

    protected String title;

    protected String image;

    protected String synopsis;

    protected boolean isFavourite = false;

    protected double rating;

    public Movie(){

    }

    public Movie(String name, String image, String synopsis, double rating, boolean isFavourite){
        this.title = name;
        this.image = image;
        this.synopsis = synopsis;
        this.rating = rating;
        this.isFavourite = isFavourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTMDBId() {
        return TMDBId;
    }

    public void setTMDBId(int TMDBId) {
        this.TMDBId = TMDBId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
