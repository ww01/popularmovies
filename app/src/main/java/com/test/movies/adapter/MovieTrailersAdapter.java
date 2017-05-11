package com.test.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.test.movies.db.entity.Trailer;
import com.test.movies.fragment.MovieDetailsFragment;
import com.test.movies.inet.InetQueryBuilder;
import com.test.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by waldek on 07.05.17.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieDetailsFragment.MovieTrailerViewHolder> {

    protected ArrayList<Trailer> trailers;

    public MovieTrailersAdapter(){
        this.trailers = new ArrayList<Trailer>();
    }

    public MovieTrailersAdapter(ArrayList<Trailer> trailers){
        this.trailers = trailers;
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

        if(position > this.trailers.size() || this.trailers.size() == 0){
            TextView textView = new TextView(context);
            textView.setText(context.getText(R.string.no_trailers));
            ViewGroup parentContainer = (ViewGroup) holder.itemView.getParent().getParent();
            (parentContainer).addView(textView);
            parentContainer.removeView(parentContainer.findViewById(R.id.details_trailers_list));
            return;
        }

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
                    Log.d(MovieTrailersAdapter.this.getClass().getSimpleName(), "Cover image load failed.");
                }
            });
    }

    @Override
    public int getItemCount() {
        return this.trailers.size();
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        this.notifyDataSetChanged();
    }

    public void addTrailer(Trailer trailer){
        this.trailers.add(trailer);
        this.notifyDataSetChanged();
    }

    public void addTrailers(ArrayList<Trailer> trailers){
        this.trailers.addAll(trailers);
        this.notifyDataSetChanged();
    }

    public void clearTrailers(){
        this.trailers.clear();
        this.notifyDataSetChanged();
    }
}
