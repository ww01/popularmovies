package pl.fullstack.movies.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;

import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.fragment.CommentsFragment;
import pl.fullstack.movies.fragment.MovieDetailsFragment;

/**
 * Created by waldek on 11.07.17.
 */

public class DetailsViewPagerAdapter extends FragmentPagerAdapter {

    protected Movie movie;


    public DetailsViewPagerAdapter(FragmentManager fm, Movie movie) {
        super(fm);
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                Bundle detailsArgs = new Bundle();
                detailsArgs.putParcelable(Movie.KEY, this.movie);
                detailsFragment.setArguments(detailsArgs);

                return detailsFragment;
            case 1:
                CommentsFragment cm = new CommentsFragment();
                Bundle commentsArgs = new Bundle();
                commentsArgs.putInt(Movie.KEY, this.movie.getTMDBId());
                cm.setArguments(commentsArgs);
                return cm;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
