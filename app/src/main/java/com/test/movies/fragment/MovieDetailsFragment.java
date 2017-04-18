package com.test.movies.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.movies.db.entity.Movie;
import com.test.popularmovies.R;


/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailsFragment extends Fragment {

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){


       LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.movie_detail, container, false);
       // Log.d(this.getClass().getSimpleName(), String.valueOf(getArguments().getSerializable(Movie.KEY) instanceof Movie));
        //Log.d(this.getClass().getSimpleName(), String.valueOf(savedState==null));
        Bundle savedArg = this.getArguments();
        //Log.d(this.getClass().getSimpleName(), String.valueOf(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getSerializable(Movie.KEY) instanceof Movie));

       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           Movie movie = (Movie) savedArg.getParcelable(Movie.KEY);
           ((TextView)layout.findViewById(R.id.detail_title)).setText(movie.getTitle());
           Log.d(this.getClass().getSimpleName(), (String) ((TextView)layout.findViewById(R.id.detail_title)).getText());
           //Picasso.with(this.getContext()).load();
           TextView rating = (TextView)layout.findViewById(R.id.detail_rating);
           rating.setText(rating.getText() + ": " + movie.getRating());
           ((TextView)layout.findViewById(R.id.detail_details)).setText(movie.getSynopsis());

       }

       return layout;
   }

}
