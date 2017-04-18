package com.test.movies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.movies.db.entity.Movie;
import com.test.movies.inet.Communicator;
import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

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

        public MoviesTaskConfig(String apiKey, InetQueryBuilder.SortOrder sortOrder) {
            this.apiKey = apiKey;
            this.sortOrder = sortOrder;
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
    }

    private class MoviesAsyncTask extends AsyncTask<MoviesListFragment.MoviesTaskConfig, Integer, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(MoviesTaskConfig... params) {
            ArrayList<Movie> movies = new ArrayList<Movie>();
            if(params.length == 0)
                throw new IllegalArgumentException("No config params provided.");
            Communicator communicator = new Communicator(params[0].getApiKey());

            try {
                movies = communicator.getMovies(1, params[0].getSortOrder());
            } catch (IOException e) {}
            catch (JSONException e) {}


            return movies;
        }


        protected void onPostExecute(ArrayList<Movie> movies){
            MoviesListFragment.this.adapter.addItems(movies);
          //  Log.d("pobór danych","Liczba pobranych: " + movies.size());
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
                InetQueryBuilder.SortOrder.HIGHEST_RATED));
        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        RecyclerView recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));


       // MoviesAsyncTask task = new MoviesAsyncTask();
      //  task.execute(new MoviesTaskConfig(getResources().getString(R.string.themoviedb_api_key), InetQueryBuilder.SortOrder.HIGHEST_RATED));


        return moviesListLayout;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

   
}
