package com.test.movies.listener;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.test.movies.task.MoviesTaskConfig;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.task.MoviesAsyncTask;
import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.R;

/**
 * Created by waldek on 18.04.17.
 */

public class TMDBScrollListener extends EndlessRecyclerViewScrollListener {

    public final int DEFAULT_START_PAGE = 2;

    protected boolean networkAvailable = true;

    public static final String KEY = "TMDBScrollListener";
    public static final String PAGE_KEY = "TMDBScrollListenerPage";
    protected final MoviesListAdapter adapter;
    protected InetQueryBuilder.SortOrder sortOrder;

    public TMDBScrollListener(LinearLayoutManager layoutManager, final MoviesListAdapter adapter, InetQueryBuilder.SortOrder sortOrder) {
        super(layoutManager);
        this.adapter = adapter;
        this.sortOrder = sortOrder;
    }

    public TMDBScrollListener(GridLayoutManager layoutManager, final MoviesListAdapter adapter, InetQueryBuilder.SortOrder sortOrder) {
        super(layoutManager);
        this.adapter = adapter;
        this.sortOrder = sortOrder;

    }

    public TMDBScrollListener(StaggeredGridLayoutManager layoutManager, final MoviesListAdapter adapter, InetQueryBuilder.SortOrder sortOrder) {
        super(layoutManager);
        this.adapter = adapter;
        this.sortOrder = sortOrder;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if(!ConnectivityHelper.isNetworkAvailable(view.getContext())){
            Toast.makeText(view.getContext(), R.string.no_network , Toast.LENGTH_LONG);
            return;
        }

        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesTaskConfig(view.getContext().getResources().getString(R.string.themoviedb_api_key),
                this.sortOrder, this.adapter, page));
    }

    public InetQueryBuilder.SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(InetQueryBuilder.SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void loadInitialItems(Context context, int page){
        if(!ConnectivityHelper.isNetworkAvailable(context)){
            Toast.makeText(context, R.string.no_network , Toast.LENGTH_LONG);
            return;
        }
        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesTaskConfig(context.getResources().getString(R.string.themoviedb_api_key),
                this.sortOrder, this.adapter, page));

       this.currentPage = page;
    }
}
