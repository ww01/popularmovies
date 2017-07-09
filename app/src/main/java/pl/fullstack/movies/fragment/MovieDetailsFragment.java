package pl.fullstack.movies.fragment;


import android.database.Cursor;
import android.os.Bundle;
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
import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.adapter.MovieTrailersAdapter;
import pl.fullstack.movies.db.contract.ContractUriBuilder;
import pl.fullstack.movies.db.contract.PopularMoviesContract;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.inet.InetQueryBuilder;
import pl.fullstack.movies.listener.FavouriteMovieListener;
import pl.fullstack.movies.listener.ReviewsScrollListener;
import pl.fullstack.movies.task.MovieTrailersAsyncTask;
import com.fullstack.popularmovies.R;


/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailsFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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
    protected TextView favouriteView;
    private static int LOADER_ID = 222;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){


       NestedScrollView layout = (NestedScrollView) inflater.inflate(R.layout.movie_detail, container, false);

       Bundle savedArg = this.getArguments();
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, savedArg, this);

       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           this.movie = (Movie) savedArg.getParcelable(Movie.KEY);

           //android.support.v4.app.LoaderManager loaderManager = getLoaderManager();


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

           this.favouriteView = ((TextView) layout.findViewById(R.id.detail_favourite));

           if(this.isFavedMovie) {
               this.favouriteView.setText("-");
           }

           (favouriteView).setOnClickListener(new FavouriteMovieListener(movie));

       }

       return layout;
   }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Movie bundleMovie = null;
        if( args != null && args.containsKey(Movie.KEY)){
            bundleMovie = (Movie) args.getParcelable(Movie.KEY);
        }
        ContractUriBuilder contractUriBuilder = new ContractUriBuilder(PopularMoviesContract.AUTHORITY);
        String movieId = bundleMovie != null? String.valueOf(bundleMovie.getTMDBId()) : "-1";

        return new android.support.v4.content.CursorLoader(this.getContext(),
                contractUriBuilder.uriFetch(PopularMoviesContract.ContractName.MOVIE),
                null, null, new String[]{movieId}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.isClosed()) {
            return;
        }


        if(cursor.getCount() != 1){
            Log.d(this.getClass().getSimpleName(), "Non unique result returned from query.");
            return;
        }

        try {
            cursor.moveToFirst();

                Movie fromDB = new Movie(
                        cursor.getLong(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5) > 0,
                        cursor.getDouble(6)
                );

                this.isFavedMovie = this.movie != null && fromDB != null && this.movie.getTitle().equals(fromDB.getTitle());
                if(this.isFavedMovie)
                    this.favouriteView.setText("-");

        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.isFavedMovie = false;
    }
}
