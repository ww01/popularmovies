package pl.fullstack.movies.net;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by waldek on 05.04.17.
 */

public class InetQueryBuilder {


    public static final String MOVIE_BASE_URI = "http://api.themoviedb.org/3/movie/";
    public static final String IMAGE_BASE_URI = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_THUMB_PATH = "https://img.youtube.com/vi/";

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

    public URL getMovieTrailers(int movieId) throws MalformedURLException{
        return new URL(
                Uri.parse(InetQueryBuilder.MOVIE_BASE_URI).buildUpon()
                .appendQueryParameter("api_key", this.apiKey)
                .appendPath(String.valueOf(movieId))
                .appendPath("videos").build().toString()
        );
    }

    public static String getYoutubeThumbPath(String movieId){
        return Uri.parse(InetQueryBuilder.YOUTUBE_THUMB_PATH).buildUpon()
                .appendPath(movieId).appendPath("hqdefault.jpg").build().toString();
    }

}
