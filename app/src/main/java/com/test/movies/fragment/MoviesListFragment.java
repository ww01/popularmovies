package com.test.movies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.R;

/**
 * Created by waldek on 07.04.17.
 */

public class MoviesListFragment extends Fragment {

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

    public static class MoviesTaskConfig {
        private String apiKey;
        private InetQueryBuilder.SortOrder sortOrder;
        private MoviesListAdapter adapter;
        private int page;

        public MoviesTaskConfig(String apiKey, InetQueryBuilder.SortOrder sortOrder, MoviesListAdapter adapter, int page) {
            this.apiKey = apiKey;
            this.sortOrder = sortOrder;
            this.adapter = adapter;
            if(this.page < 0)
                throw new IllegalArgumentException("Page numbers should not be negative");
            this.page = page;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public InetQueryBuilder.SortOrder getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(InetQueryBuilder.SortOrder sortOrder) {
            this.sortOrder = sortOrder;
        }

        public MoviesListAdapter getAdapter() {
            return adapter;
        }

        public void setAdapter(MoviesListAdapter adapter) {
            this.adapter = adapter;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }

    private MoviesListAdapter adapter;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter();




    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesTaskConfig(this.getContext().getResources().getString(R.string.themoviedb_api_key),
                InetQueryBuilder.SortOrder.HIGHEST_RATED, this.adapter, 1));
        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        RecyclerView recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);
        recyclerView.setAdapter(this.adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new TMDBScrollListener(layoutManager, this.adapter));

        return moviesListLayout;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(MoviesListAdapter.KEY, this.adapter.movies);

    }
}
