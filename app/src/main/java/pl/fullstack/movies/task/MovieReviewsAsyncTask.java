package pl.fullstack.movies.task;

import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.fullstack.movies.adapter.MovieReviewsAdapter;
import pl.fullstack.movies.db.entity.Review;
import pl.fullstack.movies.net.Communicator;
import pl.fullstack.popularmovies.R;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by waldek on 03.05.17.
 */

public class MovieReviewsAsyncTask extends AsyncTask<MovieReviewsAsyncTask.MovieReviewsAsyncTaskConfig, Integer, ArrayList<Review>> {

    public static class MovieReviewsAsyncTaskConfig {
        private String apiKey;
        private int movieId;
        private int page;
        private MovieReviewsAdapter adapter;
        private ViewGroup recyclerView;

        public MovieReviewsAsyncTaskConfig(String apiKey, int movieId, int page, MovieReviewsAdapter adapter, ViewGroup recyclerView){
            this.apiKey = apiKey;
            this.movieId = movieId;
            this.page = page;
            this.adapter = adapter;
            this.recyclerView = recyclerView;
        }
    }

    private volatile MovieReviewsAsyncTaskConfig config;

    @Override
    protected ArrayList<Review> doInBackground(MovieReviewsAsyncTaskConfig... params) {

        if(params.length == 0)
            return new ArrayList<Review>();

        MovieReviewsAsyncTaskConfig config = params[0];
        this.config = config;
        Communicator communicator = new Communicator(config.apiKey);

        ArrayList<Review> reviews;

        try {
            reviews = communicator.getReviews(config.movieId, config.page);
        } catch (JSONException e) {
            e.printStackTrace();
            reviews = new ArrayList<Review>();
        } catch (IOException e) {
            e.printStackTrace();
            reviews = new ArrayList<Review>();
        }
        return reviews;
    }


    @Override
    public void onPostExecute(ArrayList<Review> reviews){
        if(reviews.size() == 0 && this.config.adapter.getItemCount() == 0){
            ViewGroup parent = (ViewGroup)this.config.recyclerView.getParent();
            parent.removeView(this.config.recyclerView);

            TextView textView = new TextView(parent.getContext());
            textView.setText(parent.getContext().getText(R.string.no_user_reviews));
            parent.addView(textView);

            if(this.config.recyclerView.getParent() != null)
             ((ViewGroup)this.config.recyclerView.getParent()).removeView(this.config.recyclerView);
            return;
        }


        this.config.adapter.addReviews(reviews);
    }


}
