package pl.fullstack.movies.listener;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import pl.fullstack.movies.db.dao.MovieRepo;
import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.activity.R;
import pl.fullstack.movies.db.session.DbSession;

/**
 * Created by waldek on 13.05.17.
 */

public class FavouriteMovieListener implements View.OnClickListener {

    protected Movie movie;

    public FavouriteMovieListener(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();

        DaoSession dbSession = DbSession.getInstance(context);
        MovieRepo repo = new MovieRepo(dbSession);

        Movie found = repo.getByTmdbId(this.movie.getTMDBId());

        String toastText = "";
        if (found != null) {
            dbSession.getMovieDao().delete(found);
            toastText = context.getResources().getString(R.string.movie_unfaved);
            ((FloatingActionButton) v).setImageResource(R.drawable.ic_check_white_24dp);
            this.movie.setFavourite(false);
        } else {
            dbSession.getMovieDao().insert(this.movie);
            toastText = context.getResources().getString(R.string.movie_faved);
            ((FloatingActionButton)v).setImageResource(R.drawable.ic_clear_white_24dp);
            this.movie.setFavourite(true);
        }

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }

    public boolean getIsFavourite(){
        return this.movie.isFavourite();
    }
}
