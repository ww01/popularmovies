package pl.fullstack.movies.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.fullstack.movies.adapter.MoviesListAdapter;
import pl.fullstack.movies.common.AbstractMovieDataSource;
import pl.fullstack.movies.db.entity.Movie;

/**
 * Created by waldek on 09.07.17.
 */

public class MoviesScrollListener extends EndlessRecyclerViewScrollListener {

    public static final String PAGE_KEY = "PAGE_KEY";
    public static final String  QUERY_KEY = "QUERY_KEY";

    protected AbstractMovieDataSource dataSource;
    protected MoviesListAdapter adapter;
    protected String query="";


    public MoviesScrollListener(LinearLayoutManager layoutManager, AbstractMovieDataSource dataSource, MoviesListAdapter adapter, String query) {
        super(layoutManager);
        this.dataSource = dataSource;
        this.adapter = adapter;
        this.query = query;
    }

    public MoviesScrollListener(GridLayoutManager layoutManager, AbstractMovieDataSource dataSource, MoviesListAdapter adapter, String query) {
        super(layoutManager);
        this.dataSource = dataSource;
        this.adapter = adapter;
        this.query = query;
    }

    public MoviesScrollListener(StaggeredGridLayoutManager layoutManager, AbstractMovieDataSource dataSource, MoviesListAdapter adapter, String query) {
        super(layoutManager);
        this.dataSource = dataSource;
        this.adapter = adapter;
        this.query = query;
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

        Observable<List<Movie>> movieObservable = this.dataSource.getMovies(page, 30, this.query);

        movieObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(res -> {
            this.adapter.addItems(res);
            Log.d("num_res", res.size()+"");
        });

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
