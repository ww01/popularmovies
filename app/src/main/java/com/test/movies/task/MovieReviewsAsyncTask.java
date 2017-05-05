package com.test.movies.task;

import android.os.AsyncTask;
import android.util.Log;

import com.test.movies.adapter.MovieReviewsAdapter;
import com.test.movies.db.entity.Review;
import com.test.movies.inet.Communicator;

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

        public MovieReviewsAsyncTaskConfig(String apiKey, int movieId, int page, MovieReviewsAdapter adapter){
            this.apiKey = apiKey;
            this.movieId = movieId;
            this.page = page;
            this.adapter = adapter;
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
            //Log.d("liczba recenzji: ", String.valueOf(reviews.size()));
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
        if(reviews.size() == 0)
            return;
        this.config.adapter.addReviews(reviews);
    }


}
