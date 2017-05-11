package com.test.movies.db.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.movies.db.contract.PopularMoviesContract;
import com.test.movies.db.entity.DaoSession;
import com.test.movies.db.entity.IContentValuesCreator;
import com.test.movies.db.entity.Movie;
import com.test.popularmovies.DefaultApp;

/**
 * Created by waldek on 06.05.17.
 */

public class MovieContentProvider extends ContentProvider {

    private DaoSession daoSession;

    private static final UriMatcher URI_MATCHER;
    private static final int MARKED_MOVIES = 1;
    private static final int MARKED_MOVIE = 2;
    private static final int MARKED_MOVIE_ADD = 3;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/marked", MARKED_MOVIES);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/show/#", MARKED_MOVIE);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/add", MARKED_MOVIE_ADD);
    }

    @Override
    public boolean onCreate() {
        this.daoSession = ((DefaultApp)this.getContext().getApplicationContext()).getDaoSession();
        return daoSession != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type;
        switch(URI_MATCHER.match(uri)){
            case MARKED_MOVIES:
                type = "vnd.android.cursor.dir/com.test.movies.db.entity.Movie";
            break;
            case MARKED_MOVIE:
                type = "vnd.android.cursor.item/com.test.movies.db.entity.Movie";
                break;
            default:
                throw new IllegalArgumentException("Unsupported query option");
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(this.getClass().getSimpleName(), values.toString());

        switch (URI_MATCHER.match(uri)){
            case MARKED_MOVIE_ADD:
                Movie movie = Movie.CONTENT_VALUES_CREATOR.fromContentValues(values);
                this.daoSession.insert(movie);
                break;
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
