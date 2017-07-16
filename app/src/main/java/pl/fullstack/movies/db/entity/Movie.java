package pl.fullstack.movies.db.entity;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import pl.fullstack.movies.db.contract.PopularMoviesContract;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.DaoException;


/**
 * Created by waldek on 05.04.17.
 */

@Entity
public class Movie implements Parcelable, IEntity<Movie> {

    @Transient
    public static final String KEY = "MOVIE";

    @Id
    protected Long _id;

    @SerializedName("id")
    @Expose
    protected int TMDBId; // The Movie DataBase ID

    @SerializedName("title")
    @Expose
    protected String title;


    @SerializedName("poster_path")
    @Expose
    protected String image;

    @SerializedName("overview")
    @Expose
    protected String synopsis;

    protected boolean isFavourite = false;

    @SerializedName("vote_average")
    @Expose
    protected double rating;


    @ToMany
    @JoinEntity(
            entity = MovieListMovie.class,
            sourceProperty = "movieId",
            targetProperty = "movieListId"
    )
    protected List<MovieList> movieLists;

    public Movie(){
    }

    public Movie(Parcel src){

        this._id = (Long) src.readValue(Long.class.getClassLoader());
        this.TMDBId = src.readInt();
        this.title = src.readString();
        this.image = src.readString();
        this.synopsis = src.readString();
        this.isFavourite = src.readInt() > 0;
        this.rating = src.readDouble();
    }

    public Movie(String name, String image, String synopsis, double rating, boolean isFavourite, int TMDBId){
        this.title = name;
        this.image = image;
        this.synopsis = synopsis;
        this.rating = rating;
        this.isFavourite = isFavourite;
        this.TMDBId = TMDBId;
    }

    @Generated(hash = 1692557147)
    public Movie(Long _id, int TMDBId, String title, String image, String synopsis, boolean isFavourite,
            double rating) {
        this._id = _id;
        this.TMDBId = TMDBId;
        this.title = title;
        this.image = image;
        this.synopsis = synopsis;
        this.isFavourite = isFavourite;
        this.rating = rating;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeValue(this._id);
        dest.writeInt(this.TMDBId);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.synopsis);
        dest.writeInt(this.isFavourite? 1:0);
        dest.writeDouble(this.rating);
    }



    public static final IContentValuesCreator<Movie> CONTENT_VALUES_CREATOR = new IContentValuesCreator<Movie>() {

        @Override
        public ContentValues toContentValues(Movie entity) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(PopularMoviesContract.Movie.TMDB_ID, entity.getTMDBId());
            contentValues.put(PopularMoviesContract.Movie.IMAGE, entity.getImage());
            contentValues.put(PopularMoviesContract.Movie.IS_FAVOURITE, entity.getIsFavourite()? 1 : 0);
            contentValues.put(PopularMoviesContract.Movie.RATING, entity.getRating());
            contentValues.put(PopularMoviesContract.Movie.SYNOPSIS, entity.getSynopsis());
            contentValues.put(PopularMoviesContract.Movie.TITLE, entity.getTitle());

            return contentValues;
        }

        @Override
        public Movie fromContentValues(ContentValues contentValues) {
            //Movie(String name, String image, String synopsis, double rating, boolean isFavourite, int TMDBId)
            Movie movie = new Movie(
                    contentValues.getAsString(PopularMoviesContract.Movie.TITLE),
                    contentValues.getAsString(PopularMoviesContract.Movie.IMAGE),
                    contentValues.getAsString(PopularMoviesContract.Movie.SYNOPSIS),
                    contentValues.getAsDouble(PopularMoviesContract.Movie.RATING),
                    contentValues.getAsBoolean(PopularMoviesContract.Movie.IS_FAVOURITE),
                    contentValues.getAsInteger(PopularMoviesContract.Movie.TMDB_ID)
            );

            return movie;
        }
    };

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1042217376)
    private transient MovieDao myDao;




    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getTMDBId() {
        return TMDBId;
    }

    public void setTMDBId(int TMDBId) {
        this.TMDBId = TMDBId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    public boolean getIsFavourite() {
        return this.isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 692249141)
    public List<MovieList> getMovieLists() {
        if (movieLists == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MovieListDao targetDao = daoSession.getMovieListDao();
            List<MovieList> movieListsNew = targetDao._queryMovie_MovieLists(_id);
            synchronized (this) {
                if (movieLists == null) {
                    movieLists = movieListsNew;
                }
            }
        }
        return movieLists;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1782151540)
    public synchronized void resetMovieLists() {
        movieLists = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 215161401)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMovieDao() : null;
    }
}
