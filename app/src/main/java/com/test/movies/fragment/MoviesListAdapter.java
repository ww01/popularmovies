package com.test.movies.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.test.movies.db.entity.Movie;
import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.MovieDetailActivity;
import com.test.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by waldek on 18.04.17.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListFragment.ViewHolder> {

    protected ArrayList<Movie> movies = new ArrayList<Movie>();

    @Override
    public MoviesListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_tile, null);

        return new MoviesListFragment.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MoviesListFragment.ViewHolder holder, final int position) {
        if (position > this.movies.size())
            return;
        final Context ctx = holder.itemView.getContext();
        final Movie movie = this.movies.get(position);
        Log.d(this.getClass().getSimpleName(), this.movies.get(position).getTitle());
        holder.title.setText(movie.getTitle());
        //Log.d(this.getClass().getName(), InetQueryBuilder.IMAGE_BASE_URI+ "w500" + movie.getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtra(Movie.KEY, movie);
                ctx.startActivity(intent);
            }
        });

        Picasso.with(holder.poster.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into(holder.poster);
    }

    @Override
    public int getItemCount() {

        return this.movies.size();
    }

    public void addItems(ArrayList<Movie> movies) {
        this.movies.addAll(movies);

        this.notifyDataSetChanged();
    }
}
