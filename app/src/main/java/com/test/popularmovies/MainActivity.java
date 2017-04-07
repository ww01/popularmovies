package com.test.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.test.movies.helpers.ConnectivityHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if(ConnectivityHelper.isNetworkAvailable(this)){
            Picasso.with(this).setLoggingEnabled(true);
            Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into((ImageView)this.findViewById(R.id.img1));
            Log.d("NETWORK_STATE", "połączone");
        } else {
            Toast.makeText(this, "Sieć jest niedostępna.", Toast.LENGTH_SHORT).show();
            Log.d("NETWORK_STATE", "brak połączenia");
        }



        //GridLayout gl = (GridLayout) this.findViewById(R.layout.);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
