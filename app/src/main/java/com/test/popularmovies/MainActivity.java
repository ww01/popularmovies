package com.test.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.test.movies.fragment.IFragmentVisible;
import com.test.movies.fragment.MovieFavouritesFragment;
import com.test.movies.fragment.MoviesListFragment;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.inet.InetQueryBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public static final int ITEMS_NUM = 2;
        //private ArrayList<android.support.v4.app.Fragment> fragments;
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
           // this.fragments = new ArrayList<android.support.v4.app.Fragment>();
           // this.fragments.add(new MoviesListFragment());
            //this.fragments.add(new MovieFavouritesFragment());

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            /*if(position >= this.fragments.size())
                return null;

            return this.fragments.get(position);*/

            Fragment fragment = null;

            switch (position){
                case 0:
                    fragment =  new MoviesListFragment();
                    break;
                case 1:
                    fragment =  new MovieFavouritesFragment();
                    break;
            }

            Log.d("fragment_position", String.valueOf(position));

            return fragment;
        }

        @Override
        public int getCount() {
            //return this.fragments.size();
            return ITEMS_NUM;
        }
    }

    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        this.pagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager());
        this.viewPager.setAdapter(this.pagerAdapter);



        ConnectivityHelper.isNetworkAvailableMsg(this);

        final ActionBar actionBar = this.getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                MainActivity.this.viewPager.setCurrentItem(tab.getPosition(), true);
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        actionBar.addTab(actionBar.newTab().setText(this.getText(R.string.view_pager_tab_popular)).setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(this.getText(R.string.view_pager_tab_favourite)).setTabListener(tabListener));

        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position < actionBar.getTabCount())
                    actionBar.selectTab(actionBar.getTabAt(position));



            }
        });

    }


}
