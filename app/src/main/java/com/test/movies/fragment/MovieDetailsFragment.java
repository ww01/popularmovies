package com.test.movies.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.movies.adapter.MovieReviewsAdapter;
import com.test.movies.db.entity.Movie;
import com.test.movies.inet.InetQueryBuilder;
import com.test.movies.listener.ReviewsScrollListener;
import com.test.movies.task.MovieReviewsAsyncTask;
import com.test.popularmovies.R;


/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailsFragment extends Fragment {

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView author;
        public TextView content;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.review_author);
            this.content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }


    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){


       ScrollView layout = (ScrollView) inflater.inflate(R.layout.movie_detail, container, false);
       // Log.d(this.getClass().getSimpleName(), String.valueOf(getArguments().getSerializable(Movie.KEY) instanceof Movie));
        //Log.d(this.getClass().getSimpleName(), String.valueOf(savedState==null));
        Bundle savedArg = this.getArguments();
        //Log.d(this.getClass().getSimpleName(), String.valueOf(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getSerializable(Movie.KEY) instanceof Movie));

       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           Movie movie = (Movie) savedArg.getParcelable(Movie.KEY);
           ((TextView)layout.findViewById(R.id.detail_title)).setText(movie.getTitle());
           Log.d(this.getClass().getSimpleName(), (String) ((TextView)layout.findViewById(R.id.detail_title)).getText());
           Picasso.with(this.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into((ImageView)layout.findViewById(R.id.detail_poster));
           TextView rating = (TextView)layout.findViewById(R.id.detail_rating);
           rating.setText(rating.getText() + ": " + movie.getRating());
           ((TextView)layout.findViewById(R.id.detail_details)).setText(movie.getSynopsis());

           RecyclerView reviewsList = (RecyclerView) layout.findViewById(R.id.details_reviews_list);
           MovieReviewsAdapter reviewsAdapter = new MovieReviewsAdapter();
           reviewsList.setAdapter(reviewsAdapter);
           LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
           ReviewsScrollListener reviewsScrollListener = new ReviewsScrollListener(layoutManager, reviewsAdapter, movie.getTMDBId());
           reviewsList.setLayoutManager(layoutManager);
           reviewsList.addOnScrollListener(reviewsScrollListener);
           reviewsScrollListener.loadInitialItems(this.getContext(), 1);


       }

       return layout;
   }

}
