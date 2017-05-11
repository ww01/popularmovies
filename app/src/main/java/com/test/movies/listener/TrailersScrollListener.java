package com.test.movies.listener;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.test.movies.adapter.MovieTrailersAdapter;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.task.MovieTrailersAsyncTask;
import com.test.popularmovies.R;

/**
 * Created by waldek on 07.05.17.
 */

public class TrailersScrollListener extends EndlessRecyclerViewScrollListener {

    protected MovieTrailersAdapter adapter;
    protected int movieId;

    public TrailersScrollListener(LinearLayoutManager layoutManager, MovieTrailersAdapter adapter, int movieId) {
        super(layoutManager);
        this.adapter = adapter;
        this.movieId = movieId;
    }

    public TrailersScrollListener(GridLayoutManager layoutManager, MovieTrailersAdapter adapter, int movieId) {
        super(layoutManager);
        this.adapter = adapter;
        this.movieId = movieId;
    }

    public TrailersScrollListener(StaggeredGridLayoutManager layoutManager, MovieTrailersAdapter adapter, int movieId) {
        super(layoutManager);
        this.adapter = adapter;
        this.movieId = movieId;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

        if(!ConnectivityHelper.isNetworkAvailable(view.getContext())){
            Toast.makeText(view.getContext(), R.string.no_network , Toast.LENGTH_LONG);
            return;
        }

        MovieTrailersAsyncTask trailersAsyncTask = new MovieTrailersAsyncTask();
        trailersAsyncTask.execute(new MovieTrailersAsyncTask.MovieTrailerTaskConfig(view.getResources().getString(R.string.themoviedb_api_key), this.movieId, this.adapter));
    }


    public void loadInitialItems(Context context, int movieId){

        if(!ConnectivityHelper.isNetworkAvailable(context)){
            Toast.makeText(context, R.string.no_network , Toast.LENGTH_LONG);
            return;
        }



    }

}
