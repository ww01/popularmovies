package com.test.movies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.db.entity.Movie;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.inet.InetQueryBuilder;
import com.test.movies.listener.TMDBScrollListener;
import com.test.movies.task.MoviesAsyncTask;
import com.test.popularmovies.R;

/**
 * Created by waldek on 07.04.17.
 */

public class MoviesListFragment extends android.support.v4.app.Fragment {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView poster;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.getAdapterPosition();
            this.poster = (ImageView) itemView.findViewById(R.id.tile_poster);
            this.title = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    private MoviesListAdapter adapter;

    protected TMDBScrollListener scrollListener;
    protected RecyclerView recyclerView;
    protected int startPage = 1;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter();
        if(savedState != null && savedState.containsKey(MoviesListAdapter.KEY)){
            this.adapter.addItems(savedState.<Movie>getParcelableArrayList(MoviesListAdapter.KEY));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);
        recyclerView.setAdapter(this.adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


        if(savedInstanceState != null && savedInstanceState.containsKey(TMDBScrollListener.KEY) && savedInstanceState.getSerializable(TMDBScrollListener.KEY) instanceof InetQueryBuilder.SortOrder){
            this.scrollListener = new TMDBScrollListener(layoutManager, this.adapter, (InetQueryBuilder.SortOrder) savedInstanceState.getSerializable(TMDBScrollListener.KEY));
        } else {
            this.scrollListener = new TMDBScrollListener(layoutManager, this.adapter, InetQueryBuilder.SortOrder.HIGHEST_RATED);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(TMDBScrollListener.PAGE_KEY))
            this.scrollListener.setCurrentPage(savedInstanceState.getInt(TMDBScrollListener.PAGE_KEY));

        recyclerView.addOnScrollListener(this.scrollListener);

        if(this.adapter.getItemCount() == 0 ) {
            this.scrollListener.loadInitialItems(this.getContext(), this.startPage);
        }


        return moviesListLayout;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(MoviesListAdapter.KEY, this.adapter.getMovies());
        if(this.scrollListener != null) {
            bundle.putSerializable(TMDBScrollListener.KEY, this.scrollListener.getSortOrder());
            bundle.putInt(TMDBScrollListener.PAGE_KEY, this.scrollListener.getCurrentPage());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void changeSortOrder(InetQueryBuilder.SortOrder sortOrder){
        if(ConnectivityHelper.isNetworkAvailableMsg(this.getContext()) && this.adapter != null && this.scrollListener != null){
            this.adapter.clearItems();
            this.scrollListener.setSortOrder(sortOrder);
            this.scrollListener.resetState();
            if(this.adapter.getItemCount() == 0){
                this.scrollListener.loadInitialItems(this.getContext(), this.startPage);

            }
        }
    }


}
