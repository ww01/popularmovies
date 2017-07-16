package pl.fullstack.movies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.listener.ReviewsScrollListener;
import pl.fullstack.popularmovies.R;

/**
 * Created by waldek on 11.07.17.
 */

public class CommentsFragment extends Fragment {



    protected ArrayList<Review> reviews;

    protected int tmdbId;

    protected boolean movieProvided = false;

    @BindView(R.id.details_reviews_list)
    protected RecyclerView reviewsRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state){
        View view = inflater.inflate(R.layout.reviews_fragment, group, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);

        Bundle args = this.getArguments();

        if(args == null || !args.containsKey(Movie.KEY)){
            return;
        }

        this.tmdbId = args.getInt(Movie.KEY);
        this.movieProvided = true;
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);

        if(!movieProvided)
            return;


        MovieReviewsAdapter reviewsAdapter = new MovieReviewsAdapter();
        this.reviewsRecycler.setAdapter(reviewsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        ReviewsScrollListener reviewsScrollListener = new ReviewsScrollListener(layoutManager, reviewsAdapter, this.tmdbId);
        this.reviewsRecycler.setLayoutManager(layoutManager);
        this.reviewsRecycler.addOnScrollListener(reviewsScrollListener);
        reviewsScrollListener.loadInitialItems(this.getContext(), 1, this.reviewsRecycler);

    }

}
