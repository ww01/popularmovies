package pl.fullstack.movies.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by waldek on 03.05.17.
 */

@Entity
public class Review implements Parcelable, IEntity<Review> {

    public static final String KEY= Review.class.getSimpleName();

    @Id
    protected Long id;

    protected String TMDBId;

    protected String author;

    protected String content;

    protected String url;


    public Review(){}

    public Review(Parcel in){
        this.id = in.readLong();
        this.TMDBId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Generated(hash = 1449266083)
    public Review(Long id, String TMDBId, String author, String content,
            String url) {
        this.id = id;
        this.TMDBId = TMDBId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTMDBId() {
        return TMDBId;
    }

    public void setTMDBId(String TMDBId) {
        this.TMDBId = TMDBId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
