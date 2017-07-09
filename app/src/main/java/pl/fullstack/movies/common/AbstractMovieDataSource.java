package pl.fullstack.movies.common;

import java.util.List;

import io.reactivex.Observable;
import pl.fullstack.movies.db.entity.Movie;

/**
 * Created by waldek on 09.07.17.
 */

public interface AbstractMovieDataSource {
    public Observable<List<Movie>> getMovies(int page, int perPage, String type);
}
