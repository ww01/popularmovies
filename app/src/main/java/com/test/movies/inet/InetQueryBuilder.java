package com.test.movies.inet;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Request;

/**
 * Created by waldek on 05.04.17.
 */

public class InetQueryBuilder {

    public static final String MOVIE_BASE_URI = "http://api.themoviedb.org/3/movie/";
    public static final String IMAGE_BASE_URI = "https://image.tmdb.org/t/p/";

    public enum SortOrder{

        POPULAR("popular"),
        HIGHEST_RATED("top_rated");

        private String url;

        SortOrder(String url){
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    };

    protected String apiKey = "";


    public InetQueryBuilder(String apiKey){
        if(apiKey == null || apiKey.length() == 0)
            throw new IllegalArgumentException("Invalid API key");

        this.apiKey = apiKey;
    }

    public URL getMoviesList(SortOrder sortOrder, int page) throws MalformedURLException {
        if(page < 0)
            throw new IllegalArgumentException("Page number should not be negative.");

        Uri uri = Uri.parse(this.MOVIE_BASE_URI + sortOrder.getUrl())
                .buildUpon().appendQueryParameter("api_key", this.apiKey).appendQueryParameter("page", String.valueOf(page)).build();
        return new URL(uri.toString());
    }

    public URL getMovieReviews(int movieId, int page) throws MalformedURLException{
        return new URL(
                Uri.parse(InetQueryBuilder.MOVIE_BASE_URI).buildUpon()
                        .appendQueryParameter("api_key", this.apiKey)
                        .appendPath(String.valueOf(movieId))
                        .appendPath("reviews")
                        .appendQueryParameter("page", String.valueOf(page))
                        .build().toString()
        );
    }

}
