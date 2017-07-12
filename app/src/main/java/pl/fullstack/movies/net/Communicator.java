package pl.fullstack.movies.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import pl.fullstack.movies.common.AbstractMovieDataSource;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.db.entity.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.fullstack.movies.net.deserializer.MovieListDeserializer;
import pl.fullstack.movies.net.service.MovieApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by waldek on 05.04.17.
 */

public class Communicator implements AbstractMovieDataSource {

    public static final String MOVIE_BASE_URI = "http://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URI = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_THUMB_PATH = "https://img.youtube.com/vi/";

    protected Retrofit retrofit;

    protected MovieApi movieApi;


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

        Type listType = new TypeToken<List<Movie>>(){}.getType();

        Gson gson = new GsonBuilder()
                .setLenient()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(listType, new MovieListDeserializer())
                .create();

        this.retrofit =
                new Retrofit.Builder().baseUrl(MOVIE_BASE_URI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.movieApi = retrofit.create(MovieApi.class);
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

    public Observable<List<Movie>> getMovies(int page, int perPage, String query){

       return this.movieApi.getMovies("popular", this.apiKey, page);
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