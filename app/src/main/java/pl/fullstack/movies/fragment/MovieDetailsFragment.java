package pl.fullstack.movies.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.movies.adapter.MovieTrailersAdapter;
import pl.fullstack.movies.db.dao.MovieRepo;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.db.session.DbSession;
import pl.fullstack.movies.listener.ViewCoverListener;
import pl.fullstack.movies.net.InetQueryBuilder;
import pl.fullstack.movies.listener.FavouriteMovieListener;
import pl.fullstack.movies.task.MovieTrailersAsyncTask;
import pl.fullstack.popularmovies.R;


/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailsFragment extends android.support.v4.app.Fragment {

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView author;
        public TextView content;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.review_author);
            this.content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    public static class MovieTrailerViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView cover;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.trailer_title);
            this.cover = (ImageView) itemView.findViewById(R.id.trailer_cover);
        }

    }

    protected boolean isFavedMovie = false;
    protected Movie movie;


    @BindView(R.id.detail_favourite)
    protected FloatingActionButton favouriteView;

    @BindView(R.id.detail_poster)
    protected ImageView posterSmall;

    @BindView(R.id.detail_title)
    protected TextView title;

    @BindView(R.id.detail_rating)
    protected TextView rating;

    @BindView(R.id.detail_details)
    protected TextView synopsis;


    @BindView(R.id.details_trailers_list)
    protected RecyclerView trailersRecycler;

   // @BindView(R.id.detail_card_content)
    //protected LinearLayout cardContent;

    @BindView(R.id.detail_poster_container)
    protected RelativeLayout posterWrapper;

    @BindView(R.id.detail_poster_large)
    protected ImageView posterLarge;

    @BindView(R.id.details_close_poster_large)
    protected ImageView posterLargeClose;

    @BindView(R.id.detail_poster_large_container)
    protected ViewGroup posterLargeContainer;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState){
       View layout =  inflater.inflate(R.layout.movie_detail, container, false);
       ButterKnife.bind(this, layout);
       return layout;
   }


   @Override
   public void onActivityCreated(Bundle state){
       super.onActivityCreated(state);

       Bundle savedArg = this.getArguments();


       if(savedArg != null && savedArg.containsKey(Movie.KEY) && savedArg.getParcelable(Movie.KEY) instanceof Movie){
           this.movie = (Movie) savedArg.getParcelable(Movie.KEY);
           this.isFavedMovie = this.movie.getIsFavourite();

           this.title.setText(movie.getTitle());

           //Picasso.with(this.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into((ImageView)layout.findViewById(R.id.detail_poster));
           Picasso.with(this.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into(posterLarge);
           Picasso.with(this.getContext()).load(InetQueryBuilder.IMAGE_BASE_URI + "w500" + movie.getImage()).into(posterSmall, new Callback() {
               @Override
               public void onSuccess() {
                   posterSmall.post(new Runnable() {
                       @Override
                       public void run() {

                           double ratio = 0.6666;
                           LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) posterWrapper.getLayoutParams();
                           layoutParams.width = (int) (posterWrapper.getMeasuredWidth() * ratio);
                           layoutParams.height = (int) (posterWrapper.getMeasuredHeight() * ratio);

                           posterWrapper.setLayoutParams(layoutParams);
                           favouriteView.setVisibility(View.VISIBLE);
                           posterWrapper.requestLayout();

                       }
                   });
               }

               @Override
               public void onError() {

               }
           });


           LinearLayoutManager trailersLayout = new LinearLayoutManager(this.getContext());
           trailersLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
           this.trailersRecycler.setLayoutManager(trailersLayout);
           MovieTrailersAdapter trailersAdapter = new MovieTrailersAdapter();
           this.trailersRecycler.setAdapter(trailersAdapter);
           MovieTrailersAsyncTask trailersAsyncTask = new MovieTrailersAsyncTask();
           trailersAsyncTask.execute(new MovieTrailersAsyncTask.MovieTrailerTaskConfig(this.getString(R.string.themoviedb_api_key), movie.getTMDBId(), trailersAdapter));


           rating.setText(rating.getText() + ": " + movie.getRating());
           this.synopsis.setText(movie.getSynopsis());

           MovieRepo movieRepo = new MovieRepo(DbSession.getInstance(this.getContext()));

           Movie inDb = movieRepo.getByTmdbId(movie.getTMDBId());

           if(inDb != null) {
               this.favouriteView.setImageResource(R.drawable.ic_clear_white_24dp);
           }

           this.favouriteView.setOnClickListener(new FavouriteMovieListener(movie, this));

           this.posterLargeClose.setOnClickListener(new ViewCoverListener(this.posterLargeContainer));

           this.posterSmall.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   MovieDetailsFragment.this.posterLargeContainer.setVisibility(View.VISIBLE);
               }
           });

       }

   }




}
