package pl.fullstack.movies.net.service;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.db.entity.Trailer;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by waldek on 10.07.17.
 */

public interface MovieApi {

    @GET("movie/{movieType}")
    Observable<List<Movie>> getMovies(@Path("movieType") String movieType, @Query("api_key") String apiKey, @Query("page") int page);


    @GET("movie/{movieId}")
    Single<Movie> getMovie(@Path("movieId") long tmdbId, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews")
    Observable<List<Review>> getReviews(@Path("movieId") long movieId, @Query("page") int page,  @Query("api_key") String apiKey);

    @GET("movie/{movieId}/videos")
    Observable<List<Trailer>> getTrailers(@Path("movieId") long movieId, @Query("api_key") String apiKey);
}
