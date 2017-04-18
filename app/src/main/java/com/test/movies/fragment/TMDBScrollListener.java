package com.test.movies.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.R;

/**
 * Created by waldek on 18.04.17.
 */

public class TMDBScrollListener extends EndlessRecyclerViewScrollListener {

    protected final MoviesListAdapter adapter;

    public TMDBScrollListener(LinearLayoutManager layoutManager, final MoviesListAdapter adapter) {
        super(layoutManager);
        this.adapter = adapter;
    }

    public TMDBScrollListener(GridLayoutManager layoutManager, final MoviesListAdapter adapter) {
        super(layoutManager);
        this.adapter = adapter;
    }

    public TMDBScrollListener(StaggeredGridLayoutManager layoutManager, final MoviesListAdapter adapter) {
        super(layoutManager);
        this.adapter = adapter;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesListFragment.MoviesTaskConfig(view.getContext().getResources().getString(R.string.themoviedb_api_key),
                InetQueryBuilder.SortOrder.HIGHEST_RATED, this.adapter, page));
    }
}
