package com.test.popularmovies;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.movies.adapter.MoviesListAdapter;
import com.test.movies.fragment.MoviesListFragment;
import com.test.movies.helpers.ConnectivityHelper;
import com.test.movies.inet.InetQueryBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Fragment fragment = getFragmentManager().findFragmentById(R.id.movies_list_fragment);

        if(!(fragment instanceof MoviesListFragment))
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
        getFragmentManager().putFragment(bundle, "moviesList", getFragmentManager().findFragmentById(R.id.movies_list_fragment));
    }

}
