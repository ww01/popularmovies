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
import pl.fullstack.movies.net.deserializer.MovieTrailersDeserializer;
import pl.fullstack.movies.net.deserializer.ReviewsListDeserializer;
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
        Type reviewListType = new TypeToken<List<Review>>(){}.getType();
        Type trailersListType = new TypeToken<List<Trailer>>(){}.getType();
        Gson gson = new GsonBuilder()
                .setLenient()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(listType, new MovieListDeserializer())
                .registerTypeAdapter(reviewListType, new ReviewsListDeserializer())
                .registerTypeAdapter(trailersListType, new MovieTrailersDeserializer())
                .create();

        this.retrofit =
                new Retrofit.Builder().baseUrl(MOVIE_BASE_URI)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.movieApi = retrofit.create(MovieApi.class);
    }


    public Observable<List<Movie>> getMovies(int page, int perPage, String query){

       return this.movieApi.getMovies("popular", this.apiKey, page);
    }

    public Observable<List<Review>> getReviews(int movieId, int page) {
        return this.movieApi.getReviews(movieId, page, this.apiKey);
    }



    public Observable<List<Trailer>> getTrailers(int movieId) {
        return this.movieApi.getTrailers(movieId, this.apiKey);
    }

}
