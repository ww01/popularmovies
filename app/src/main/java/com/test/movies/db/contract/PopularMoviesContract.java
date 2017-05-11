package com.test.movies.db.contract;

/**
 * Created by waldek on 10.05.17.
 */

public class PopularMoviesContract  {


    public static final String AUTHORITY = "com.test.popularmovies";

    private PopularMoviesContract(){}

    public static class Movie {
        public static final String ID = "id";
        public static final String TMDB_ID = "tmdbId";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String SYNOPSIS = "synopsis";
        public static final String IS_FAVOURITE = "isFavorite";
        public static final String RATING = "rating";
    }

}
