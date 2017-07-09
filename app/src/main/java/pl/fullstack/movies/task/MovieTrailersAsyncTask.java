package pl.fullstack.movies.task;

import android.os.AsyncTask;

import pl.fullstack.movies.adapter.MovieTrailersAdapter;
import pl.fullstack.movies.db.entity.Trailer;
import pl.fullstack.movies.net.Communicator;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by waldek on 07.05.17.
 */

public class MovieTrailersAsyncTask extends AsyncTask<MovieTrailersAsyncTask.MovieTrailerTaskConfig, Integer, ArrayList<Trailer>> {
    public static class MovieTrailerTaskConfig {

        private String apiKey;
        private int movieId;
        private MovieTrailersAdapter adapter;

        public MovieTrailerTaskConfig(String apiKey, int movieId, MovieTrailersAdapter adapter){
            this.apiKey = apiKey;
            this.movieId = movieId;
            this.adapter = adapter;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public MovieTrailersAdapter getAdapter() {
            return adapter;
        }

        public void setAdapter(MovieTrailersAdapter adapter) {
            this.adapter = adapter;
        }
    }


    protected MovieTrailersAdapter adapter;

    @Override
    protected ArrayList<Trailer> doInBackground(MovieTrailerTaskConfig... params) {

        if(params.length == 0)
            throw new IllegalArgumentException("No config provided");

        this.adapter = params[0].getAdapter();

        Communicator communicator = new Communicator(params[0].getApiKey());

        try {
            return communicator.getTrailers(params[0].getMovieId());
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<Trailer>();
    }


    @Override
    public void onPostExecute(ArrayList<Trailer> trailers){
        if(trailers.size() == 0)
            return;

        this.adapter.addTrailers(trailers);
    }

}
