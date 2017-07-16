package pl.fullstack.movies.listener;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pl.fullstack.activity.LoginActivity;
import pl.fullstack.activity.MainActivity;
import pl.fullstack.movies.db.dao.MovieRepo;
import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.entity.Movie;

import pl.fullstack.movies.db.session.DbSession;
import pl.fullstack.popularmovies.R;
import pl.fullstack.security.Helper;

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

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        DaoSession dbSession = DbSession.getInstance(context);
        MovieRepo repo = new MovieRepo(dbSession);

        Movie found = repo.getByTmdbId(this.movie.getTMDBId());

        Intent intent = new Intent(context, LoginActivity.class);

        //context.startActivity(intent);
        if(!Helper.isLoggedIn(context)) {
            if(this.fragment.get() != null)
                this.fragment.get().startActivity(intent);
            return;
        }

        
        String toastText = "";
        if (found != null) {
            dbSession.getMovieDao().delete(found);
            toastText = context.getResources().getString(R.string.movie_unfaved);
            ((FloatingActionButton) v).setImageResource(R.drawable.ic_check_white_24dp);
            this.movie.setFavourite(false);

            if(context instanceof AppCompatActivity){
                this.removeItem(movie, (AppCompatActivity) context);
            }

        } else {
            dbSession.getMovieDao().insert(this.movie);
            toastText = context.getResources().getString(R.string.movie_faved);
            ((FloatingActionButton)v).setImageResource(R.drawable.ic_clear_white_24dp);
            this.movie.setFavourite(true);

            if(context instanceof AppCompatActivity){
                this.addItem(movie, (AppCompatActivity) context);
            }
        }

        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
    }

    public boolean getIsFavourite(){
        return this.movie.isFavourite();
    }

    protected void removeItem(Movie movie, AppCompatActivity activity){
        if(this.movie == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ACTION_INTENT_CONTENT, movie);
        activity.setResult(MainActivity.ACTION_ITEM_REMOVE, intent);
    }

    protected void addItem(Movie movie, AppCompatActivity activity){
        if(this.movie == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ACTION_INTENT_CONTENT, movie);
        activity.setResult(MainActivity.ACTION_ITEM_ADD, intent);
    }
}
