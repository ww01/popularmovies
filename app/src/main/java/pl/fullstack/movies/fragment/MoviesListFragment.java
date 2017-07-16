package pl.fullstack.movies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import pl.fullstack.activity.MainActivity;

import pl.fullstack.movies.adapter.MoviesListAdapter;
import pl.fullstack.movies.common.AbstractMovieDataSource;
import pl.fullstack.movies.common.DataSourceType;
import pl.fullstack.movies.db.dao.MovieRepo;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.session.DbSession;
import pl.fullstack.movies.listener.MoviesScrollListener;
import pl.fullstack.movies.net.Communicator;
import pl.fullstack.popularmovies.R;


/**
 * Created by waldek on 07.04.17.
 */

public class MoviesListFragment extends android.support.v4.app.Fragment {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView poster;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (ImageView) itemView.findViewById(R.id.tile_poster);
            this.title = (TextView) itemView.findViewById(R.id.tile_title);
        }
    }

    private MoviesListAdapter adapter;

    protected MoviesScrollListener moviesScrollListener;
    protected GridLayoutManager layoutManager;
    protected RecyclerView recyclerView;
    protected int startPage = 1;
    protected static final String LAYOUT_MANAGER = "LAYOUT_MANAGER";
    public static final String DATA_SOURCE = "DATA_SOURCE";

    protected AbstractMovieDataSource dataSource;


    protected AbstractMovieDataSource createDataSource(DataSourceType dataSourceType){

        switch (dataSourceType){
            case DATABASE:
                return new MovieRepo(DbSession.getInstance(this.getContext()));
            case NETWORK:
                return new Communicator(this.getString(R.string.themoviedb_api_key));
            default:
                throw new IllegalArgumentException("Unimplemented datasource type.");
        }
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        this.adapter = new MoviesListAdapter(this);

        Bundle args = this.getArguments();

        if(args != null && args.containsKey(DATA_SOURCE))
            this.dataSource = this.createDataSource((DataSourceType) args.get(DATA_SOURCE));

        if(savedState == null && this.dataSource == null)
            throw new IllegalArgumentException("Unspecified datasource type.");

        this.layoutManager = new GridLayoutManager(this.getContext(), 2);

        if(savedState == null)
            return;

        if(this.dataSource == null && !savedState.containsKey(DATA_SOURCE))
            throw new IllegalArgumentException("Unspecified datasource type.");

        this.dataSource = this.createDataSource((DataSourceType) savedState.get(DATA_SOURCE));


        if(savedState.containsKey(LAYOUT_MANAGER))
            this.layoutManager.onRestoreInstanceState(savedState.getParcelable(LAYOUT_MANAGER));

        if(savedState.containsKey(MoviesListAdapter.KEY)){
            this.adapter.addItems(savedState.<Movie>getParcelableArrayList(MoviesListAdapter.KEY));
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup moviesListLayout = (ViewGroup) inflater.inflate(R.layout.movies_list, container, false);

        recyclerView = (RecyclerView) moviesListLayout.findViewById(R.id.posters_recycler);

        this.moviesScrollListener = new MoviesScrollListener(layoutManager, this.dataSource, this.adapter, "");
        int defaultScrollStartPage = this.dataSource instanceof Communicator ? 1 : 0;
        if(savedInstanceState != null){
          if(savedInstanceState.containsKey(MoviesScrollListener.PAGE_KEY))
              defaultScrollStartPage = savedInstanceState.getInt(MoviesScrollListener.PAGE_KEY);

          this.moviesScrollListener.setQuery(savedInstanceState.containsKey(MoviesScrollListener.QUERY_KEY)? savedInstanceState.getString(MoviesScrollListener.QUERY_KEY) : "");
        }

        this.moviesScrollListener.setCurrentPage(defaultScrollStartPage);

        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(this.moviesScrollListener);

        if(this.adapter.getItemCount() == 0 ) {
            this.moviesScrollListener.onLoadMore(this.dataSource instanceof Communicator ? 1 : 0, 0, this.recyclerView);
        }


        return moviesListLayout;
    }



    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        if(this.adapter != null && this.adapter.getItemCount() > 0)
            bundle.putParcelableArrayList(MoviesListAdapter.KEY, this.adapter.getMovies());

        if(this.moviesScrollListener != null) {
            bundle.putString(MoviesScrollListener.QUERY_KEY, this.moviesScrollListener.getQuery());
            bundle.putInt(MoviesScrollListener.PAGE_KEY, this.moviesScrollListener.getCurrentPage());
        }

        if(this.layoutManager != null)
            bundle.putParcelable(LAYOUT_MANAGER, this.layoutManager.onSaveInstanceState());

        if(this.dataSource != null){
            bundle.putSerializable(DATA_SOURCE, this.dataSource instanceof Communicator ? DataSourceType.NETWORK : DataSourceType.DATABASE );
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode != MainActivity.ACTION_DETAILS_REQUEST_CODE || data == null
                || this.adapter == null || !data.hasExtra(MainActivity.ACTION_INTENT_CONTENT))
            return;


        Movie updatedMovie = data.getParcelableExtra(MainActivity.ACTION_INTENT_CONTENT);


        if(resultCode == MainActivity.ACTION_ITEM_ADD){
            this.adapter.addItem(updatedMovie);
            this.adapter.notifyDataSetChanged();
        } else if(resultCode == MainActivity.ACTION_ITEM_REMOVE){
            this.adapter.removeItem(updatedMovie);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void addItem(Movie movie){
        if(this.adapter != null){
            this.adapter.addItem(movie);
        }
    }

    public void removeItem(Movie movie){
        if(this.adapter != null){
            this.adapter.removeItem(movie);
        }

    }

}
