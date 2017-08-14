package pl.fullstack.movies.listener;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.net.Communicator;
import pl.fullstack.movies.net.helpers.ConnectivityHelper;
import pl.fullstack.popularmovies.R;


/**
 * Created by waldek on 05.05.17.
 */

public class ReviewsScrollListener extends EndlessRecyclerViewScrollListener {

    protected MovieReviewsAdapter reviewsAdapter;
    protected int movieId; //TMDB movie _id NOT movie entity _id
    protected Communicator communicator;
    protected boolean emptyListSearched =  false; // remember is search of "empty view" took place
    protected View emptyList;

    public ReviewsScrollListener(LinearLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    public ReviewsScrollListener(GridLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    public ReviewsScrollListener(StaggeredGridLayoutManager layoutManager, MovieReviewsAdapter reviewsAdapter, int movieId) {
        super(layoutManager);
        this.reviewsAdapter = reviewsAdapter;
        this.movieId = movieId;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        final Context context = view.getContext();

        if(!ConnectivityHelper.isNetworkAvailable(context)){
            Toast.makeText(context, R.string.no_network , Toast.LENGTH_LONG);
            return;
        }

        if(this.communicator == null){
            this.communicator = new Communicator(context.getString(R.string.themoviedb_api_key));
        }

        this.communicator.getReviews(this.movieId, page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Review>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Review> reviews) {
                        reviewsAdapter.addReviews(reviews);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(
                                context,
                                String.format(context.getString(R.string.error_loading_data), context.getString(R.string.data_type_review)),
                                Toast.LENGTH_LONG
                        ).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                        if(reviewsAdapter.getItemCount() == 0){
                            if(emptyList == null && !emptyListSearched){
                                emptyList = ((ViewGroup)view.getParent()).findViewById(R.id.reviews_no_results);
                            }

                            if(emptyList != null)
                                emptyList.setVisibility(View.VISIBLE);

                        } else {
                            if(emptyList != null && emptyList.getVisibility() == View.VISIBLE)
                                emptyList.setVisibility(View.GONE);
                        }
                    }
                });

    }

}
