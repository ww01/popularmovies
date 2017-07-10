package pl.fullstack.movies.net.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.fullstack.movies.db.entity.Movie;

/**
 * Created by waldek on 10.07.17.
 */

public class MovieListTypeAdapter extends TypeAdapter<List<Movie>> {
    @Override
    public void write(JsonWriter out, List<Movie> value) throws IOException {
        throw new UnsupportedOperationException("Method not yet implemented.");
    }

    @Override
    public List<Movie> read(JsonReader in) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();

        

        return null;
    }
}
