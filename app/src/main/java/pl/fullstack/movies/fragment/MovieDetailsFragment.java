package pl.fullstack.movies.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.fullstack.activity.R;
import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.adapter.MovieTrailersAdapter;
import pl.fullstack.movies.db.contract.ContractUriBuilder;
import pl.fullstack.movies.db.contract.PopularMoviesContract;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.net.InetQueryBuilder;
import pl.fullstack.movies.listener.FavouriteMovieListener;
import pl.fullstack.movies.listener.ReviewsScrollListener;
import pl.fullstack.movies.task.MovieTrailersAsyncTask;



/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailsFragment extends android.support.v4.app.Fragment {

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

    protected boolean isFavedMovie = false;
    protected Movie movie;
    protected FloatingActionButton favouriteView;
    private static int LOADER_ID = 222;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){


       NestedScrollView layout = (NestedScrollView) inflater.inflate(R.layout.movie_detail, container, false);

       Bundle savedArg = this.getArguments();


       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           this.movie = (Movie) savedArg.getParcelable(Movie.KEY);


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

           this.favouriteView = ((FloatingActionButton) layout.findViewById(R.id.detail_favourite));

           if(this.isFavedMovie) {
               this.favouriteView.setImageResource(R.drawable.ic_clear_white_24dp);
           }

           (favouriteView).setOnClickListener(new FavouriteMovieListener(movie));

       }

       return layout;
   }


}
