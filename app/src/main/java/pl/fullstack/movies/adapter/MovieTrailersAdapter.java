package pl.fullstack.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import pl.fullstack.movies.db.entity.Trailer;
import pl.fullstack.movies.fragment.MovieDetailsFragment;
import pl.fullstack.movies.net.InetQueryBuilder;
import pl.fullstack.popularmovies.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by waldek on 07.05.17.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieDetailsFragment.MovieTrailerViewHolder> {

    protected List<Trailer> trailers;

    public MovieTrailersAdapter(){
        this.trailers = new ArrayList<Trailer>();
    }

    @Override
    public MovieDetailsFragment.MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, null);
        return new MovieDetailsFragment.MovieTrailerViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final MovieDetailsFragment.MovieTrailerViewHolder holder, int position) {
        if(holder == null)
            return;

        final Context context = holder.itemView.getContext();


        final Trailer trailer = this.trailers.get(position);
        holder.itemView.setVisibility(View.GONE);
        holder.title.setText(trailer.getName());


        if(trailer.getSourceSite().toLowerCase().equals("youtube"))

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getTrailerKey()));
                    appIntent.putExtra("force_fullscreen", true);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + trailer.getTrailerKey()));
                    try {
                        v.getContext().startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        v.getContext().startActivity(webIntent);
                    }
                }
            });

            Picasso.with(holder.cover.getContext())
                    .load(InetQueryBuilder.getYoutubeThumbPath(trailer.getTrailerKey())).into(holder.cover, new Callback() {
                @Override
                public void onSuccess() {
                    holder.itemView.setVisibility(View.VISIBLE);
                }
                @Override
                public void onError() {
                    holder.itemView.setVisibility(View.VISIBLE);
                }
            });
    }

    @Override
    public int getItemCount() {
        return this.trailers.size();
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        this.notifyDataSetChanged();
    }

    public void addTrailer(Trailer trailer){
        this.trailers.add(trailer);
        this.notifyDataSetChanged();
    }

    public void addTrailers(List<Trailer> trailers){
        this.trailers.addAll(trailers);
        this.notifyDataSetChanged();
    }

    public void clearTrailers(){
        this.trailers.clear();
        this.notifyDataSetChanged();
    }
}
