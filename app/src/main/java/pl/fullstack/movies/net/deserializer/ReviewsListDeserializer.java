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

import pl.fullstack.movies.db.entity.Review;

/**
 * Created by waldek on 14.08.17.
 */

public class ReviewsListDeserializer implements JsonDeserializer<List<Review>> {
    @Override
    public List<Review> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<Review> reviews = new ArrayList<>();

        JsonArray content = json.getAsJsonObject().get("results").getAsJsonArray();

        if(content == null || content.size() == 0)
            return reviews;
        for(JsonElement jsonElement : content){
            reviews.add(new Gson().fromJson(jsonElement, Review.class));
        }
        return reviews;
    }
}
