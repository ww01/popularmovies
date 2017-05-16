package com.test.movies.fragment;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.db.contract.ContractUriBuilder;
import com.test.movies.db.contract.PopularMoviesContract;
import com.test.movies.db.entity.Movie;
import com.test.popularmovies.DefaultApp;
import com.test.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by waldek on 13.05.17.
 */

public class MovieFavouritesFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, IFragmentVisible {

    protected MoviesListAdapter adapter;
    protected RecyclerView recyclerView;
    private  static int LOADER_ID = 0x11;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.favourite_movies_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.favourites_recycler);
        recyclerView.setAdapter(this.adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        return view;
    }


    @Override
    public void reInitLoader() {
        this.updateAdapterState();
    }


    @Override

    public void onResume(){
        super.onResume();
        this.updateAdapterState();
    }


    private void updateAdapterState(){

        this.getLoaderManager().restartLoader(LOADER_ID, null, this);
        return;
      /*  if(this.getContext() == null)
            return;

        ArrayList<Movie> movies = (ArrayList<Movie>)((DefaultApp)this.getContext().getApplicationContext()).getDaoSession().getMovieDao().loadAll();

        ArrayList<Movie> remove = new ArrayList<Movie>();

        for(Movie favedMovie : this.adapter.getMovies()){
            boolean found = false;
            for(Movie refreshed : movies){
                if(refreshed.get_id().equals(favedMovie.get_id())){
                    found = true;
                    break;
                }
            }
            if(!found)
                remove.add(favedMovie);
        }

        for(Movie refreshed: movies){
            boolean found = false;
            for(Movie faved : this.adapter.getMovies()){
                if(faved.get_id().equals(refreshed.get_id())){
                    found = true;
                    break;
                }
            }

            if(!found)
                this.adapter.getMovies().add(refreshed);
        }

        this.adapter.removeItems(remove);*/
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.isClosed()) {
            return;
        }

        try {
            this.adapter.clearItems();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                this.adapter.addItem(new Movie(
                        cursor.getLong(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5) > 0,
                        cursor.getDouble(6)
                ));

                cursor.moveToNext();
            }

        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        this.adapter.clearItems();
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ContractUriBuilder contractUriBuilder = new ContractUriBuilder(PopularMoviesContract.AUTHORITY);
        return new android.support.v4.content.CursorLoader(this.getContext(), contractUriBuilder.uriFetchAll(PopularMoviesContract.ContractName.MOVIE), null, null, null, null);
    }
}
