package pl.fullstack.movies.task;

import pl.fullstack.movies.adapter.MoviesListAdapter;
import pl.fullstack.movies.inet.InetQueryBuilder;

/**
 * Created by waldek on 06.05.17.
 */
public class MoviesTaskConfig {
    private String apiKey;
    private InetQueryBuilder.SortOrder sortOrder;
    private MoviesListAdapter adapter;
    private int page;

    public MoviesTaskConfig(String apiKey, InetQueryBuilder.SortOrder sortOrder, MoviesListAdapter adapter, int page) {
        this.apiKey = apiKey;
        this.sortOrder = sortOrder;
        this.adapter = adapter;
        if (this.page < 0)
            throw new IllegalArgumentException("Page numbers should not be negative");
        this.page = page;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public InetQueryBuilder.SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(InetQueryBuilder.SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public MoviesListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MoviesListAdapter adapter) {
        this.adapter = adapter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
