package com.test.popularmovies;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.fragment.IFragmentVisible;
import com.test.movies.fragment.MovieFavouritesFragment;
import com.test.movies.fragment.MoviesListFragment;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.inet.InetQueryBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private ArrayList<android.support.v4.app.Fragment> fragments;
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<android.support.v4.app.Fragment>();
            this.fragments.add(new MoviesListFragment());
            this.fragments.add(new MovieFavouritesFragment());
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if(position >= this.fragments.size())
                return null;

            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
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
        //ActionBar should be replaced with PagerTabStrip

        final ActionBar actionBar = this.getSupportActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                MainActivity.this.viewPager.setCurrentItem(tab.getPosition(), true);
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
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

                if( pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof IFragmentVisible) {
                    /*android.support.v4.app.Fragment dynamicFragment = pagerAdapter.getItem(viewPager.getCurrentItem());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); //getFragmentManager().beginTransaction();
                    ft.detach(dynamicFragment).attach(dynamicFragment).commit();*/
                    ((IFragmentVisible)pagerAdapter.getItem(viewPager.getCurrentItem())).reInitLoader();
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        android.support.v4.app.Fragment fragment = this.pagerAdapter.getItem(this.viewPager.getCurrentItem());
        if(!( fragment instanceof MoviesListFragment))
            return false;


        switch(item.getItemId()){
            case R.id.menu_sort_popular:
                ((MoviesListFragment) fragment).changeSortOrder(InetQueryBuilder.SortOrder.POPULAR);
                break;
            case R.id.menu_sort_top_rated:
                ((MoviesListFragment) fragment).changeSortOrder(InetQueryBuilder.SortOrder.HIGHEST_RATED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
       // getFragmentManager().putFragment(bundle, "moviesList", getFragmentManager().findFragmentById(R.id.movies_list_fragment));
    }

}
