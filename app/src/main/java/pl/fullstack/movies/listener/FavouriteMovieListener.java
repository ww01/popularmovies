package pl.fullstack.movies.listener;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.fullstack.activity.MainActivity;
import pl.fullstack.movies.db.dao.MovieRepo;
import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.entity.Movie;

import pl.fullstack.movies.db.session.DbSession;
import pl.fullstack.popularmovies.R;

/**
 * Created by waldek on 13.05.17.
 */

public class FavouriteMovieListener implements View.OnClickListener {

    protected Movie movie;

    WeakReference<Fragment> fragment;

    public FavouriteMovieListener(Movie movie, Fragment fragment) {
        this.movie = movie;
        this.fragment = new WeakReference<Fragment>(fragment);
    }


    private void displayResultToast(Context context, String toastText) {
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        DaoSession dbSession = DbSession.getInstance(context);
        MovieRepo repo = new MovieRepo(dbSession);

        repo.getByTmdbId(this.movie.getTMDBId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        subscriber -> {
                            dbSession.getMovieDao().delete(subscriber);

                            ((FloatingActionButton) v).setImageResource(R.drawable.ic_check_white_24dp);
                            this.movie.setFavourite(false);

                            if (context instanceof AppCompatActivity) {
                                this.removeItem(movie, (AppCompatActivity) context);
                            }

                            displayResultToast(context, context.getResources().getString(R.string.movie_unfaved));
                        },
                        subscriber -> {
                            dbSession.getMovieDao().insert(movie);
                            ((FloatingActionButton) v).setImageResource(R.drawable.ic_clear_white_24dp);
                            this.movie.setFavourite(true);

                            if (context instanceof AppCompatActivity) {
                                this.addItem(movie, (AppCompatActivity) context);
                            }
                            displayResultToast(context, context.getResources().getString(R.string.movie_faved));
                        }

                );

    }

    public boolean getIsFavourite() {
        return this.movie.isFavourite();
    }

    protected void removeItem(Movie movie, AppCompatActivity activity) {
        if (this.movie == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ACTION_INTENT_CONTENT, movie);
        activity.setResult(MainActivity.ACTION_ITEM_REMOVE, intent);
    }

    protected void addItem(Movie movie, AppCompatActivity activity) {
        if (this.movie == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ACTION_INTENT_CONTENT, movie);
        activity.setResult(MainActivity.ACTION_ITEM_ADD, intent);
    }
}
