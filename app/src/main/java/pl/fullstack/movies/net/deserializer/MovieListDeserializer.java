package pl.fullstack.movies.net.deserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.fullstack.movies.db.entity.Movie;

/**
 * Created by waldek on 10.07.17.
 */

public class MovieListDeserializer implements JsonDeserializer<List<Movie>> {

    @Override
    public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Movie> movies = new ArrayList<>();

        Log.d("deserialization", "start");
        JsonArray content = json.getAsJsonObject().get("results").getAsJsonArray();

        Log.d("json_content", content.toString());

        if(content == null || content.size() == 0)
            return movies;


        for(JsonElement jsonElement : content){
            movies.add(new Gson().fromJson(jsonElement, Movie.class));
        }

        return movies;
    }
}
