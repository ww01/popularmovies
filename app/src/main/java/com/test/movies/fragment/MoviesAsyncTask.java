package com.test.movies.fragment;

import android.os.AsyncTask;

import com.test.movies.db.entity.Movie;
import com.test.movies.inet.Communicator;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by waldek on 18.04.17.
 */
class MoviesAsyncTask extends AsyncTask<MoviesListFragment.MoviesTaskConfig, Integer, ArrayList<Movie>> {

    protected MoviesListAdapter adapter;

    @Override
    protected ArrayList<Movie> doInBackground(MoviesListFragment.MoviesTaskConfig... params) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        if (params.length == 0)
            throw new IllegalArgumentException("No config params provided.");

        this.adapter = params[0].getAdapter();

        Communicator communicator = new Communicator(params[0].getApiKey());
        try {
            movies = communicator.getMovies(params[0].getPage(), params[0].getSortOrder());
        } catch (IOException e) {
        } catch (JSONException e) {
        }


        return movies;
    }


    protected void onPostExecute(ArrayList<Movie> movies) {
        this.adapter.addItems(movies);
        //  Log.d("pobór danych","Liczba pobranych: " + movies.size());
    }
}
