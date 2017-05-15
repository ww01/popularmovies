package com.test.movies.listener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.test.movies.db.entity.DaoSession;
import com.test.movies.db.entity.Movie;
import com.test.movies.db.entity.MovieDao;
import com.test.popularmovies.DefaultApp;
import com.test.popularmovies.R;

import java.lang.ref.WeakReference;

/**
 * Created by waldek on 13.05.17.
 */

public class FavouriteMovieListener implements View.OnClickListener {

    protected Movie movie;
    protected WeakReference<DaoSession> daoSession;

    public FavouriteMovieListener(DaoSession daoSession, Movie movie){
        this.movie = movie;
        //declared as weak reference in case of screen rotation we don't want this to hang in memory
        this.daoSession = new WeakReference<DaoSession>(daoSession);
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        if(this.daoSession == null)
            return;

        /*
        Operation is neither resource heavy or long so I skipped using ContentProvider
         */
        boolean exists = this.daoSession.get().getMovieDao().queryBuilder().where(MovieDao.Properties.TMDBId.eq(this.movie.getTMDBId())).count() > 0;
        String toastText="";
        if(exists){
            this.daoSession.get().getMovieDao().delete(this.movie);
            toastText = context.getResources().getString(R.string.movie_unfaved);
        }
        else {
            Log.d("inserted_movie_id", String.valueOf(this.movie.get_id()));

            this.daoSession.get().getMovieDao().insert(this.movie);
            toastText = context.getResources().getString(R.string.movie_faved);
        }

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }
}
