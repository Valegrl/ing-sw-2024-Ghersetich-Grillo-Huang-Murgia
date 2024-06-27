package it.polimi.ingsw.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is a custom serializer and deserializer for the Coordinate class.
 * It implements JsonSerializer and JsonDeserializer interfaces provided by Gson.
 * The serialize method converts a Coordinate object into a JsonElement.
 * The deserialize method converts a JsonElement back into a Coordinate object.
 */
public class CoordinateTypeAdapter implements JsonSerializer<Coordinate>, JsonDeserializer<Coordinate> {

    /**
     * This method is used to convert a Coordinate object into a JsonElement.
     *
     * @param coordinate The Coordinate object to be converted.
     * @param typeOfSrc The specific generic type of src.
     * @param context The context for serialization.
     * @return A JsonElement corresponding to the specified Coordinate.
     */
    @Override
    public JsonElement serialize(Coordinate coordinate, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", coordinate.getX());
        jsonObject.addProperty("y", coordinate.getY());
        return jsonObject;
    }

    /**
     * This method is used to convert a JsonElement back into a Coordinate object.
     *
     * @param json The JsonElement being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context The context for deserialization.
     * @return A Coordinate object corresponding to the specified JsonElement.
     * @throws JsonParseException if json is not in the expected format of Coordinate.
     */
    @Override
    public Coordinate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Coordinate(jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt());
    }
}
