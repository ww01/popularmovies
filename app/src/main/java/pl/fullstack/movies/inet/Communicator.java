package pl.fullstack.movies.inet;

import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.db.entity.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
            return this.reviewsFromJson(object.getJSONArray("results"));

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


        public ArrayList<Trailer> parseTrailers(String string) throws JSONException{
            JSONObject object = new JSONObject(string);

            if(!object.has("results"))
                throw new JSONException("Malforrmed response received - no results present.");
            return this.trailersFromJson(object.getJSONArray("results"));
        }

        protected ArrayList<Trailer> trailersFromJson(JSONArray jsonArray) throws JSONException {
            ArrayList<Trailer> trailers = new ArrayList<Trailer>();

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                Iterator<String> keys = object.keys();

                while (keys.hasNext()) {
                    String key = keys.next();

                    switch (key) {
                       case "id":
                           trailer.setTMDBId(object.getString(key));
                           break;
                       case "iso_639_1":
                           trailer.setLocaleLang(object.getString(key));
                           break;
                       case "iso_3166_1":
                           trailer.setLocaleDialect(object.getString(key));
                           break;
                       case "key":
                           trailer.setTrailerKey(object.getString(key));
                           break;
                       case "name":
                           trailer.setName(object.getString(key));
                           break;
                       case "site":
                           trailer.setSourceSite(object.getString(key));
                           break;
                       case "size":
                           trailer.setResolution(object.getInt(key));
                           break;
                       case "type":
                           trailer.setType(object.getString(key));
                           break;
                    }

                }

                trailers.add(trailer);
            }

            return trailers;
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

    public ArrayList<Trailer> getTrailers(int movieId) throws IOException, JSONException {
        Response response = this.okHttpClient
                .newCall(new Request.Builder().url(this.inetQueryBuilder.getMovieTrailers(movieId)).build())
                .execute();

        if(response.isSuccessful()){
            return new JsonDecoder().parseTrailers(response.body().string());
        }

        throw new IOException(String.valueOf(response.code()));
    }

}
