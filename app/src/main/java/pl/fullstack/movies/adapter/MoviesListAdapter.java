package pl.fullstack.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.fragment.MoviesListFragment;
import pl.fullstack.movies.net.InetQueryBuilder;
import pl.fullstack.activity.MovieDetailActivity;
import pl.fullstack.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waldek on 18.04.17.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListFragment.ViewHolder> {

    protected ArrayList<Movie> movies = new ArrayList<Movie>();

    public static final String KEY = "NETWORK_MOVIES_LIST_ADAPTER";

    @Override
    public MoviesListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_tile, null);

        return new MoviesListFragment.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final MoviesListFragment.ViewHolder holder, final int position) {
        if (position > this.movies.size())
            return;
        holder.itemView.setVisibility(View.GONE);
        final Context ctx = holder.itemView.getContext();
        final Movie movie = this.movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
                intent.putExtra(Movie.KEY, movie);
                ctx.startActivity(intent);
            }
        });

        Picasso.with(holder.poster.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into(holder.poster, new Callback() {
            @Override
            public void onSuccess() {
                holder.itemView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {

        return this.movies.size();
    }

    public void addItems(List<Movie> movies) {
        this.movies.addAll(movies);

        this.notifyDataSetChanged();
    }

    public void addItem(Movie movie){
        this.movies.add(movie);
        this.notifyDataSetChanged();
    }

    public void clearItems(){
        this.movies.clear();
        this.notifyDataSetChanged();
    }

    public void removeItem(Movie movie){
        this.movies.remove(movie);
        this.notifyDataSetChanged();
    }

    public void removeItems(List<Movie> movies){
        this.movies.removeAll(movies);
        this.notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovies(){
        return this.movies;
    }

    public void setMovies(ArrayList<Movie> movies){
        this.movies = movies;
    }
}
