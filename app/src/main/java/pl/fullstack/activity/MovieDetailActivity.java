package pl.fullstack.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.fullstack.movies.adapter.DetailsViewPagerAdapter;
import pl.fullstack.movies.db.entity.Movie;
import pl.fullstack.movies.fragment.MovieDetailsFragment;

/**
 * Created by waldek on 15.04.17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.details_viewpager)
    protected ViewPager detailsViewPager;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        this.setContentView(R.layout.movie_detail_activity);
        ButterKnife.bind(this);


        Bundle args = getIntent().getExtras();

        if(args == null || !args.containsKey(Movie.KEY)) {
            return;
        }

        Movie movie = args.getParcelable(Movie.KEY);

        HashMap<Integer, String > tabNames = new HashMap<>();

        this.detailsViewPager.setAdapter(new DetailsViewPagerAdapter(getSupportFragmentManager(), movie));


        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(movie.getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            ActionBar.TabListener tabListener = new ActionBar.TabListener() {
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                    MovieDetailActivity.this.detailsViewPager.setCurrentItem(tab.getPosition(), true);
                }

                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                }

                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                }
            };

            actionBar.addTab(actionBar.newTab().setText(this.getText(R.string.detail_strip_overview)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(this.getText(R.string.detail_strip_reviews)).setTabListener(tabListener));

            this.detailsViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    if(position < actionBar.getTabCount())
                        actionBar.selectTab(actionBar.getTabAt(position));

                }
            });

        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
