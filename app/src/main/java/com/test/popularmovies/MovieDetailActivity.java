package com.test.popularmovies;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.test.movies.db.entity.Movie;
import com.test.movies.fragment.MovieDetailsFragment;

/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailActivity extends Activity {


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.setContentView(R.layout.movie_detail_activity);

        Log.d(this.getClass().getSimpleName(), String.valueOf(getIntent().hasExtra(Movie.KEY)));

        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(); //(MovieDetailsFragment) getFragmentManager().findFragmentById(R._id.movie_details_fragment);
        movieDetailsFragment.setArguments(getIntent().getExtras());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
       // ft.add(movieDetailsFragment, "DETAILS").commit();
        ft.add(R.id.details_holder, movieDetailsFragment).commit();
        //ft.add(CONTENT_VIEW_ID, newFragment).commit();
    }

}
