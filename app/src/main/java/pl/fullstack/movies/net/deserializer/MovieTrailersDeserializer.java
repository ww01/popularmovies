package pl.fullstack.movies.net.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.fullstack.movies.db.entity.Trailer;

/**
 * Created by waldek on 14.08.17.
 */

public class MovieTrailersDeserializer implements JsonDeserializer<List<Trailer>> {
    @Override
    public List<Trailer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Trailer> trailers = new ArrayList<>();

        JsonArray array = json.getAsJsonObject().get("results").getAsJsonArray();

        if(array == null || array.size() == 0)
            return trailers;

        for(JsonElement element : array){
            trailers.add(new Gson().fromJson(element, Trailer.class));
        }

        return trailers;
    }
}
