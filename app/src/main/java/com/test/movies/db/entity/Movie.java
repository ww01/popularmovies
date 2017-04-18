package com.test.movies.db.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by waldek on 05.04.17.
 */

public class Movie implements Parcelable {

    public static final String KEY = "MOVIE";

    protected int id;

    protected int TMDBId; // The Movie DataBase ID

    protected String title;

    protected String image;

    protected String synopsis;

    protected boolean isFavourite = false;

    protected double rating;

    public Movie(){

    }

    public Movie(Parcel src){

        this.id  = src.readInt();
        this.TMDBId = src.readInt();
        this.title = src.readString();
        this.image = src.readString();
        this.synopsis = src.readString();
        this.isFavourite = src.readInt() > 0;
        this.rating = src.readDouble();
    }

    public Movie(String name, String image, String synopsis, double rating, boolean isFavourite, int TMDBId){
        this.title = name;
        this.image = image;
        this.synopsis = synopsis;
        this.rating = rating;
        this.isFavourite = isFavourite;
        this.TMDBId = TMDBId;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.TMDBId);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.synopsis);
        dest.writeInt(this.isFavourite? 1:0);
        dest.writeDouble(this.rating);
    }
}
