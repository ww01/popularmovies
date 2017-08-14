package pl.fullstack.movies.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by waldek on 03.05.17.
 */


public class Review implements Parcelable, IEntity<Review> {

    public static final String KEY= Review.class.getSimpleName();



    @Expose
    @SerializedName("_id")
    protected String TMDBId;

    @Expose
    protected String author;

    @Expose
    protected String content;

    @Expose
    protected String url;


    public Review(){}

    public Review(Parcel in){

        this.TMDBId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Generated(hash = 1449266083)
    public Review(Long id, String TMDBId, String author, String content,
            String url) {

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
