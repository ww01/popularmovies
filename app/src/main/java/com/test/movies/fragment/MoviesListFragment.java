package com.test.movies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
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
            this.poster = (ImageView) itemView.findViewById(R.id.tile_poster);
            this.title = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListFragment.ViewHolder> {

        protected ArrayList<Movie> movies = new ArrayList<Movie>();
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_tile, null);

            return  new ViewHolder(itemLayoutView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if(position > this.movies.size())
                return;
            Movie movie = this.movies.get(position);
            holder.title.setText(movie.getTitle());
             //Log.d(this.getClass().getName(), InetQueryBuilder.IMAGE_BASE_URI+ "w500" + movie.getImage());

            Picasso.with(holder.poster.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into(holder.poster);
        }

        @Override
        public int getItemCount() {

            return this.movies.size();
        }

        public void addItems(ArrayList<Movie>  movies){
            this.movies.addAll(movies);

            this.notifyDataSetChanged();
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
            Log.d("pobór danych","Liczba pobranych: " + movies.size());
        }
    }

    private MoviesListAdapter adapter;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter();

        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesTaskConfig(this.getContext().getResources().getString(R.string.themoviedb_api_key),
                InetQueryBuilder.SortOrder.HIGHEST_RATED));

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        RecyclerView recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        MoviesAsyncTask task = new MoviesAsyncTask();
        task.execute(new MoviesTaskConfig(getResources().getString(R.string.themoviedb_api_key), InetQueryBuilder.SortOrder.HIGHEST_RATED));


        return moviesListLayout;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }
}
