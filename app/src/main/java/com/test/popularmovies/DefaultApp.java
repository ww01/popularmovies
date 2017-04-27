package com.test.popularmovies;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import okhttp3.Cache;

/**
 * Created by waldek on 20.04.17.
 */

public class DefaultApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

      /*  Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);*/
        Cache cache = new Cache(this.getCacheDir(), Integer.MAX_VALUE);
        okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient.Builder().cache(cache).build();
        OkHttp3Downloader downloader = new OkHttp3Downloader(okHttp3Client);
        Picasso picasso = new Picasso.Builder(this).downloader(downloader).build();
        Picasso.setSingletonInstance(picasso);

    }
}