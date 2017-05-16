package com.test.movies.db.contract;

import android.net.Uri;

/**
 * Created by waldek on 10.05.17.
 */

public class PopularMoviesContract  {

    public static final String AUTHORITY = "com.test.popularmovies";

    public static  enum ContractName {

        MOVIE("movie");

        private final  String name;

        ContractName(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    };


    private PopularMoviesContract(){}

    public static class Movie {
        public static final String ID = "_id";
        public static final String TMDB_ID = "TMDBID";
        public static final String TITLE = "TITLE";
        public static final String IMAGE = "IMAGE";
        public static final String SYNOPSIS = "SYNOPSIS";
        public static final String IS_FAVOURITE = "IS_FAVOURITE";
        public static final String RATING = "RATING";
    }

}
