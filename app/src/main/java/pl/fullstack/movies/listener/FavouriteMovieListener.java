package pl.fullstack.movies.listener;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.fullstack.movies.db.contract.ContractUriBuilder;
import pl.fullstack.movies.db.contract.PopularMoviesContract;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.activity.R;

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


        boolean exists = res.getCount() > 0;
                //this.daoSession.get().getMovieDao().queryBuilder().where(MovieRepo.Properties.TMDBId.eq(this.movie.getTMDBId())).count() > 0;
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
