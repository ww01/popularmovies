package pl.fullstack.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.fullstack.movies.common.DataSourceType;
import pl.fullstack.movies.fragment.MoviesListFragment;
import pl.fullstack.movies.net.helpers.ConnectivityHelper;

public class MainActivity extends AppCompatActivity {

    private class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public static final int ITEMS_NUM = 2;
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {


            MoviesListFragment fragment = new MoviesListFragment();
            Bundle args = new Bundle();
            switch (position){
                case 0:
                    args.putSerializable(MoviesListFragment.DATA_SOURCE, DataSourceType.NETWORK);
                    break;
                case 1:
                    args.putSerializable(MoviesListFragment.DATA_SOURCE, DataSourceType.DATABASE);
                    break;
            }
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
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
