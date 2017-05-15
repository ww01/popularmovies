package com.test.movies.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.test.movies.adapter.MovieReviewsAdapter;
import com.test.movies.adapter.MovieTrailersAdapter;
import com.test.movies.db.entity.Movie;
import com.test.movies.db.entity.MovieDao;
import com.test.movies.inet.InetQueryBuilder;
import com.test.movies.listener.FavouriteMovieListener;
import com.test.movies.listener.ReviewsScrollListener;
import com.test.movies.task.MovieReviewsAsyncTask;
import com.test.movies.task.MovieTrailersAsyncTask;
import com.test.popularmovies.DefaultApp;
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

    public static class MovieTrailerViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView cover;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.trailer_title);
            this.cover = (ImageView) itemView.findViewById(R.id.trailer_cover);
        }

    }


    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){


       NestedScrollView layout = (NestedScrollView) inflater.inflate(R.layout.movie_detail, container, false);

       Bundle savedArg = this.getArguments();

       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           Movie movie = (Movie) savedArg.getParcelable(Movie.KEY);
           ((TextView)layout.findViewById(R.id.detail_title)).setText(movie.getTitle());

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
           reviewsScrollListener.loadInitialItems(this.getContext(), 1, reviewsList);

           RecyclerView trailersList = (RecyclerView) layout.findViewById(R.id.details_trailers_list);
           LinearLayoutManager trailersLayout = new LinearLayoutManager(this.getContext());
           trailersLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
           trailersList.setLayoutManager(trailersLayout);
           MovieTrailersAdapter trailersAdapter = new MovieTrailersAdapter();
           trailersList.setAdapter(trailersAdapter);
           MovieTrailersAsyncTask trailersAsyncTask = new MovieTrailersAsyncTask();
           trailersAsyncTask.execute(new MovieTrailersAsyncTask.MovieTrailerTaskConfig(this.getString(R.string.themoviedb_api_key), movie.getTMDBId(), trailersAdapter));

           TextView favouriteView = ((TextView) layout.findViewById(R.id.detail_favourite));

           if(((DefaultApp)this.getContext().getApplicationContext()).getDaoSession().getMovieDao().queryBuilder().where(MovieDao.Properties.TMDBId.eq(movie.getTMDBId())).count() > 0)
               favouriteView.setText("-");

           (favouriteView).setOnClickListener(new FavouriteMovieListener(
                   ((DefaultApp)this.getContext().getApplicationContext()).getDaoSession(), movie
           ));

       }

       return layout;
   }

}
