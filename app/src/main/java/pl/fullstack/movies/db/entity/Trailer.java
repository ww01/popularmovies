package pl.fullstack.movies.db.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by waldek on 07.05.17.
 */

public class Trailer implements IEntity {

    /*
    "_id": "58f33221c3a368086700edf5",
      "iso_639_1": "en",
      "iso_3166_1": "US",
      "key": "JwMKRevYa_M",
      "name": "Official Trailer",
      "site": "YouTube",
      "size": 1080,
      "type": "Trailer"
     */

    @Expose
    @SerializedName("id")
    protected String TMDBId;

    protected String localeLang;

    protected String localeDialect;

    @Expose
    @SerializedName("key")
    protected String trailerKey;

    @Expose
    protected String name;

    @Expose
    @SerializedName("site")
    protected String sourceSite;

    @Expose
    @SerializedName("size")
    protected int resolution;

    @Expose
    protected String type;

    public Trailer(){}

    protected Trailer(String tmdbId, String localeLang, String localeDialect, String trailerKey, String name, String sourceSite, int resolution, String type){
        this.TMDBId = tmdbId;
        this.localeLang = localeLang;
        this.localeDialect = localeDialect;
        this.trailerKey = trailerKey;
        this.name = name;
        this.sourceSite = sourceSite;
        this.resolution = resolution;
        this.type = type;
    }

    @Generated(hash = 1774890834)
    public Trailer(int id, String TMDBId, String localeLang, String localeDialect, String trailerKey, String name, String sourceSite, int resolution, String type) {
        this.TMDBId = TMDBId;
        this.localeLang = localeLang;
        this.localeDialect = localeDialect;
        this.trailerKey = trailerKey;
        this.name = name;
        this.sourceSite = sourceSite;
        this.resolution = resolution;
        this.type = type;
    }



    public String getTMDBId() {
        return TMDBId;
    }

    public void setTMDBId(String TMDBId) {
        this.TMDBId = TMDBId;
    }

    public String getLocaleLang() {
        return localeLang;
    }

    public void setLocaleLang(String localeLang) {
        this.localeLang = localeLang;
    }

    public String getLocaleDialect() {
        return localeDialect;
    }

    public void setLocaleDialect(String localeDialect) {
        this.localeDialect = localeDialect;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(String sourceSite) {
        this.sourceSite = sourceSite;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
