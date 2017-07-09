package pl.fullstack.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.fullstack.movies.fragment.MovieDetailsFragment;

/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.setContentView(R.layout.movie_detail_activity);

        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(getIntent().getExtras());
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.details_holder, movieDetailsFragment).commit();
    }

}
