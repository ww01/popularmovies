package pl.fullstack.movies.db.dao;

import pl.fullstack.movies.common.AbstractMovieDataSource;
import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.MovieDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by waldek on 09.07.17.
 */

public class MovieRepo implements AbstractMovieDataSource {

    protected DaoSession daoSession;

    public MovieRepo(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    public Observable<List<Movie>> getMovies(int page, int perPage, String type) {

        if(page<0)
            throw new IllegalArgumentException("Page should not be a negative value.");

        if(perPage <= 0)
            throw new IllegalArgumentException("Number of items per page should be greater than 0");

        QueryBuilder<Movie> movieQueryBuilder = this.daoSession.getMovieDao().queryBuilder().offset(page*perPage).limit(perPage);
        return Observable.fromArray(movieQueryBuilder.build().list());
    }

    public Movie getByTmdbId(int tmdbId){

        Movie movie = null;
        QueryBuilder<Movie> movieQuery = this.daoSession.getMovieDao().queryBuilder().where(MovieDao.Properties.TMDBId.eq("?"));

        try {
            movie = movieQuery.build().setParameter(0, tmdbId).unique();
        } catch(DaoException e){
            e.printStackTrace();
        }

        return movie;
    }

}
