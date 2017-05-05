package com.test.movies.inet;

import android.util.Log;

import com.test.movies.db.entity.Movie;
import com.test.movies.db.entity.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by waldek on 05.04.17.
 */

public class Communicator {

    public class JsonDecoder {
        public ArrayList<Movie> parseMovies(String string) throws JSONException {
            JSONObject object = new JSONObject(string);

            if(!object.has("results"))
                throw new JSONException("Malforrmed response received - no results present.");

            return this.moviesFromJson(object.getJSONArray("results"));
        }

        protected ArrayList<Movie> moviesFromJson(JSONArray jsonArray) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<Movie>();


            for(int i =0; i < jsonArray.length(); i ++){
                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                Iterator<String> keys = object.keys();

                while (keys.hasNext()){ // iterate over retrieved json keys and match with "entity" fields
                    String key = keys.next().toLowerCase();
                    switch (key) {
                        case "title":
                            movie.setTitle(object.getString(key));
                            break;
                        case "vote_average":
                            movie.setRating(object.getDouble(key));
                            break;
                        case "overview":
                            movie.setSynopsis(object.getString(key));
                            break;
                        case "id":
                            movie.setTMDBId(object.getInt(key));
                            break;
                        case "poster_path":
                            movie.setImage(object.getString(key));
                            break;
                    }
                }

                movies.add(movie);
            }

            return movies;
        }

        public ArrayList<Review> parseReviews(String string) throws JSONException {
            JSONObject object = new JSONObject(string);

            if(!object.has("results"))
                throw new JSONException("Malforrmed response received - no results present.");
            ArrayList<Review> rv = this.reviewsFromJson(object.getJSONArray("results"));
            return rv;
        }

        protected ArrayList<Review> reviewsFromJson(JSONArray jsonArray) throws JSONException{
            ArrayList<Review> reviews = new ArrayList<Review>();

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Review review = new Review();
                Iterator<String> keys = object.keys();

                while(keys.hasNext()){
                    String key = keys.next().toLowerCase();
                    switch (key){
                        case "id":
                            review.setTMDBId(object.getString(key));
                            break;
                        case "author":
                            review.setAuthor(object.getString(key));
                            break;
                        case "content":
                            review.setContent(object.getString(key));
                            break;
                        case "url":
                            review.setUrl(object.getString(key));
                            break;
                    }
                }

                reviews.add(review);
            }

            return reviews;
        }
    }

    protected OkHttpClient okHttpClient = new OkHttpClient();

    protected String apiKey;

    protected InetQueryBuilder inetQueryBuilder;

    public Communicator(String apiKey){
        this.apiKey = apiKey;
        this.inetQueryBuilder = new InetQueryBuilder(this.apiKey);
    }

    public ArrayList<Movie> getMovies(int page, InetQueryBuilder.SortOrder sortOrder) throws IOException, JSONException {



        Response response = this.okHttpClient
                .newCall(
                        new Request.Builder().url(this.inetQueryBuilder.getMoviesList(sortOrder, page)).build()
                )
                .execute();

        if(response.isSuccessful()){

            return new JsonDecoder().parseMovies(response.body().string());

        }

        throw new IOException(response.code() + "");
    }

    public ArrayList<Review> getReviews(int movieId, int page) throws IOException, JSONException{

        Response response = this.okHttpClient
                .newCall(new Request.Builder().url(this.inetQueryBuilder.getMovieReviews(movieId, page)).build())
                .execute();

        if(response.isSuccessful()){
            return new JsonDecoder().parseReviews(response.body().string());
        }

        throw new IOException(response.code() + "");
    }

}
