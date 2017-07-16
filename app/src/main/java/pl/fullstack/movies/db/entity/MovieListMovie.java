package pl.fullstack.movies.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by waldek on 16.07.17.
 */

@Entity
public class MovieListMovie {

    @Transient
    protected MovieList movieList;
    @Transient
    protected Movie movie;

    protected Long movieListId;

    protected Long movieId;

    @Generated(hash = 108195979)
    public MovieListMovie(Long movieListId, Long movieId) {
        this.movieListId = movieListId;
        this.movieId = movieId;
    }

    @Generated(hash = 1671063036)
    public MovieListMovie() {
    }

    public Long getMovieListId() {
        return this.movieListId;
    }

    public void setMovieListId(Long movieListId) {
        this.movieListId = movieListId;
    }

    public Long getMovieId() {
        return this.movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

}
