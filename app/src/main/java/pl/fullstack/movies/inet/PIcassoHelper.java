package pl.fullstack.movies.inet;

import android.content.Context;


import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Cache;

/**
 * Created by waldek on 20.04.17.
 */

public class PIcassoHelper {


    private static final int CACHE_SIZE = 67108864;

    private static boolean cacheInitiated = false;

    private PIcassoHelper(){}

    public static Picasso getPicasso(Context context){
        if(!cacheInitiated) {
            Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);
            okhttp3.OkHttpClient okHttp3Client = new okhttp3.OkHttpClient.Builder().cache(cache).build();
            OkHttp3Downloader downloader = new OkHttp3Downloader(okHttp3Client);
            Picasso picasso = new Picasso.Builder(context).downloader(downloader).build();
            Picasso.setSingletonInstance(picasso);
            cacheInitiated = true;
        }
        return Picasso.with(context);
    }
}
