package com.test.movies.listener;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.movies.db.contract.ContractUriBuilder;
import com.test.movies.db.contract.PopularMoviesContract;
import com.test.movies.db.entity.Movie;
import com.test.popularmovies.R;

/**
 * Created by waldek on 13.05.17.
 */

public class FavouriteMovieListener implements View.OnClickListener {

    protected Movie movie;

    public FavouriteMovieListener(Movie movie){
        this.movie = movie;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();

        ContractUriBuilder uriBuilder = new ContractUriBuilder(PopularMoviesContract.AUTHORITY);

        ContentResolver resolver = context.getContentResolver();

        Cursor res = resolver.query(uriBuilder.uriFetch(PopularMoviesContract.ContractName.MOVIE), null, null,
                new String[]{String.valueOf(this.movie.getTMDBId())}, null);

        for(String column : res.getColumnNames()){
            Log.d("column_name", column);
        }

        boolean exists = res.getCount() > 0;
                //this.daoSession.get().getMovieDao().queryBuilder().where(MovieDao.Properties.TMDBId.eq(this.movie.getTMDBId())).count() > 0;
        String toastText="";
        if(exists){
            resolver.delete(uriBuilder.uriDelete(PopularMoviesContract.ContractName.MOVIE), null, new String[]{String.valueOf(this.movie.getTMDBId())});
            toastText = context.getResources().getString(R.string.movie_unfaved);
            ((TextView) v).setText("+");
        }
        else {
            resolver.insert(
                    uriBuilder.uriInsert(PopularMoviesContract.ContractName.MOVIE),
                    Movie.CONTENT_VALUES_CREATOR.toContentValues(this.movie)
            );

            toastText = context.getResources().getString(R.string.movie_faved);
            ((TextView) v).setText("-");
        }

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }
}
