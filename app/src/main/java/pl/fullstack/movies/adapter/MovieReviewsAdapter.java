package pl.fullstack.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.fragment.MovieDetailsFragment;
import com.fullstack.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by waldek on 05.05.17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieDetailsFragment.MovieReviewViewHolder> {

    protected ArrayList<Review> reviews;

    public MovieReviewsAdapter(){
        this.reviews = new ArrayList<Review>();
    }

    public MovieReviewsAdapter(ArrayList<Review> reviews){
        this.reviews = reviews;
    }


    @Override
    public MovieDetailsFragment.MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review, null);
        return new MovieDetailsFragment.MovieReviewViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MovieDetailsFragment.MovieReviewViewHolder holder, int position) {
        if(holder == null || holder.itemView == null)
            return;
        Review review = this.reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        this.notifyDataSetChanged();
    }

    public void addReviews(ArrayList<Review> reviews){
        this.reviews.addAll(reviews);
        this.notifyDataSetChanged();
    }

    public void clearReviews(){
        this.reviews.clear();
        this.notifyDataSetChanged();
    }
}
