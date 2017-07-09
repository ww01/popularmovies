package pl.fullstack.movies.db.dao;

import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.MovieDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by waldek on 09.07.17.
 */

public class MovieRepo {

    protected DaoSession daoSession;

    public MovieRepo(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Observable<List<Movie>> getFavourites(){
        QueryBuilder<Movie> movieQueryBuilder = this.daoSession.getMovieDao().queryBuilder().where(MovieDao.Properties.IsFavourite.eq(true));
        return Observable.fromArray(movieQueryBuilder.build().list());
    }

}
