package com.test.movies.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.db.entity.Movie;
import com.test.movies.inet.InetQueryBuilder;
import com.test.movies.listener.TMDBScrollListener;
import com.test.popularmovies.R;

/**
 * Created by waldek on 07.04.17.
 */

public class MoviesListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {

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
    protected GridLayoutManager layoutManager;
    protected RecyclerView recyclerView;
    protected int startPage = 1;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter();
        this.layoutManager = new GridLayoutManager(this.getContext(), 2);
        if(savedState != null && savedState.containsKey(MoviesListAdapter.KEY)){
            this.adapter.addItems(savedState.<Movie>getParcelableArrayList(MoviesListAdapter.KEY));
        }

        if(savedState != null && savedState.containsKey(TMDBScrollListener.KEY) && savedState.getSerializable(TMDBScrollListener.KEY) instanceof InetQueryBuilder.SortOrder){
            this.scrollListener = new TMDBScrollListener(layoutManager, this.adapter, (InetQueryBuilder.SortOrder) savedState.getSerializable(TMDBScrollListener.KEY));
        } else {
            this.scrollListener = new TMDBScrollListener(layoutManager, this.adapter, InetQueryBuilder.SortOrder.HIGHEST_RATED);
        }

        if(savedState != null && savedState.containsKey(TMDBScrollListener.PAGE_KEY))
            this.scrollListener.setCurrentPage(savedState.getInt(TMDBScrollListener.PAGE_KEY));
        
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(layoutManager);

        Spinner spinner = (Spinner) moviesListLayout.findViewById(R.id.sort_order);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sort_order, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);


        recyclerView.addOnScrollListener(this.scrollListener);

        if(this.adapter.getItemCount() == 0 ) {
            this.scrollListener.loadInitialItems(this.getContext(), this.startPage);
        }


        return moviesListLayout;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        InetQueryBuilder.SortOrder sortOrder = null;

        switch(position){
            case 0:
                sortOrder = InetQueryBuilder.SortOrder.HIGHEST_RATED;
                break;
            case 1:
                sortOrder = InetQueryBuilder.SortOrder.POPULAR;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

        this.adapter.clearItems();
        this.scrollListener.setSortOrder(sortOrder);
        this.scrollListener.resetState();
        if(this.adapter.getItemCount() == 0){
            this.scrollListener.loadInitialItems(this.getContext(), this.startPage);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Intentioanlly left blank
    }
}
