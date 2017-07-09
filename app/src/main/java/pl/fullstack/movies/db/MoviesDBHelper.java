package pl.fullstack.movies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.fullstack.movies.db.contract.PopularMoviesContract;

/**
 * Created by waldek on 15.05.17.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "popular_movies";
    public static final int DB_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + PopularMoviesContract.ContractName.MOVIE.getName() + " (" +
                        PopularMoviesContract.Movie.ID               + " INTEGER NOT NULL PRIMARY KEY, " +
                        PopularMoviesContract.Movie.TITLE       + " STRING NOT NULL, "     +
                        PopularMoviesContract.Movie.SYNOPSIS + " STRING, "                  +
                        PopularMoviesContract.Movie.IMAGE   + " STRING, "       +
                        PopularMoviesContract.Movie.IS_FAVOURITE   + " INTEGER, "      +
                        PopularMoviesContract.Movie.RATING   + " STRING, " +
                        PopularMoviesContract.Movie.TMDB_ID +" STRING)" +
                        ";";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMoviesContract.ContractName.MOVIE.getName());
        onCreate(sqLiteDatabase);
    }
}
