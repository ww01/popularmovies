package com.test.movies.db.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.test.movies.db.contract.ContractUriBuilder;
import com.test.movies.db.contract.PopularMoviesContract;
import com.test.movies.db.entity.DaoSession;
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
    private static final int MARKED_MOVIE_DELETE = 4;
    private static final int MARKED_MOVIE_UPDATE = 5;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/all", MARKED_MOVIES);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/show/#", MARKED_MOVIE);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/add", MARKED_MOVIE_ADD);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/delete", MARKED_MOVIE_DELETE);
        URI_MATCHER.addURI(PopularMoviesContract.AUTHORITY, "/movie/update", MARKED_MOVIE_UPDATE);
    }

    @Override
    public boolean onCreate() {
        this.daoSession = ((DefaultApp)this.getContext().getApplicationContext()).getDaoSession();
        return daoSession != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if(this.daoSession == null || !(this.daoSession.getDatabase().getRawDatabase() instanceof SQLiteDatabase))
            return null;

        switch (URI_MATCHER.match(uri)){
            case MARKED_MOVIES:
                return ((SQLiteDatabase)this.daoSession.getDatabase().getRawDatabase()).query(PopularMoviesContract.ContractName.MOVIE.getName(),
                        null, null, null, null, null, null);
        }

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
            case MARKED_MOVIE_ADD:
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

        Uri resUri = null;
        ContractUriBuilder uriBuilder = new ContractUriBuilder(PopularMoviesContract.AUTHORITY);

        switch (URI_MATCHER.match(uri)){
            case MARKED_MOVIE_ADD:
                Movie movie = Movie.CONTENT_VALUES_CREATOR.fromContentValues(values);
                this.daoSession.insert(movie);
                this.getContext().getContentResolver().notifyChange(uri, null);
                return uriBuilder.uriInsert(PopularMoviesContract.ContractName.MOVIE, movie.get_id());
        }

        return resUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int affected = 0;

        if(!(this.daoSession.getDatabase().getRawDatabase() instanceof SQLiteDatabase))
            return 0;

        switch(URI_MATCHER.match(uri)){
            case MARKED_MOVIE_DELETE:
                affected = ((SQLiteDatabase)this.daoSession.getDatabase().getRawDatabase()).delete("", selection, selectionArgs);
                if(affected > 0)
                    this.getContext().getContentResolver().notifyChange(uri, null);
                break;
        }

        return affected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Basicly, at current stage, we are not performing any updates on stored entities - only database state changing operations are insert and delete,
        // so we can throw not implemented exception here

        throw new RuntimeException("This feature is not implemented.");
    }
}
