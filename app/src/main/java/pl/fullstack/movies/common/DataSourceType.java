package pl.fullstack.movies.common;

/**
 * Created by waldek on 09.07.17.
 */

public enum DataSourceType {
    NETWORK("NETWORK"), DATABASE("DATABASE");

    private String sourceName;

    DataSourceType(String sourceName){
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
