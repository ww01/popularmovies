package com.test.movies.inet;

import android.util.Log;

import com.test.movies.db.entity.Movie;

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

            Log.d(this.getClass().getName(), "rozmiar tablicy json: " + jsonArray.length());

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
    }

    protected OkHttpClient okHttpClient = new OkHttpClient();

    protected String apiKey;

    public Communicator(String apiKey){
        this.apiKey = apiKey;
    }

    public ArrayList<Movie> getMovies(int page, InetQueryBuilder.SortOrder sortOrder) throws IOException, JSONException {

        InetQueryBuilder inetQueryBuilder = new InetQueryBuilder(this.apiKey);

        Log.d(this.getClass().getName(), new Request.Builder().url(inetQueryBuilder.getMoviesList(sortOrder, page)).build().toString());

        Response response = this.okHttpClient
                .newCall(
                        new Request.Builder().url(inetQueryBuilder.getMoviesList(sortOrder, page)).build()
                )
                .execute();

        if(response.isSuccessful()){

            return new JsonDecoder().parseMovies(response.body().string());

        }

        throw new IOException(response.code() + "");
    }

}
