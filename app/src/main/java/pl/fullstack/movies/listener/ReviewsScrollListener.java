package pl.fullstack.movies.listener;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import pl.fullstack.activity.R;
import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.helpers.ConnectivityHelper;
import pl.fullstack.movies.task.MovieReviewsAsyncTask;

//import pl.fullstack.activity.R;
/**
 * Created by waldek on 05.05.17.
 */

public class ReviewsScrollListener extends EndlessRecyclerViewScrollListener {

    protected MovieReviewsAdapter reviewsAdapter;
    protected int movieId; //TMDB movie _id NOT movie entity _id

    public ReviewsScrollListener(LinearLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    public ReviewsScrollListener(GridLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    public ReviewsScrollListener(StaggeredGridLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if(!ConnectivityHelper.isNetworkAvailable(view.getContext())){
            Toast.makeText(view.getContext(), R.string.no_network , Toast.LENGTH_LONG);
            return;
        }

        MovieReviewsAsyncTask reviewsAsyncTask = new MovieReviewsAsyncTask();
        reviewsAsyncTask.execute(new MovieReviewsAsyncTask.MovieReviewsAsyncTaskConfig(view.getContext().getResources().getString(R.string.themoviedb_api_key),
                this.movieId, page, this.reviewsAdapter, view));
    }

    public void loadInitialItems(Context context, int page, RecyclerView view){
        if(!ConnectivityHelper.isNetworkAvailable(context)){
            Toast.makeText(context, R.string.no_network , Toast.LENGTH_LONG);
            return;
        }

        this.currentPage = page;
        MovieReviewsAsyncTask reviewsAsyncTask = new MovieReviewsAsyncTask();
        reviewsAsyncTask.execute(new MovieReviewsAsyncTask.MovieReviewsAsyncTaskConfig(context.getResources().getString(R.string.themoviedb_api_key),
                this.movieId, 1, this.reviewsAdapter, view));
    }
}
