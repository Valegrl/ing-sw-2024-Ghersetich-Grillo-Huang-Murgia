package it.polimi.ingsw.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CoordinateTypeAdapter implements JsonSerializer<Coordinate>, JsonDeserializer<Coordinate> {
    @Override
    public JsonElement serialize(Coordinate coordinate, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", coordinate.getX());
        jsonObject.addProperty("y", coordinate.getY());
        return jsonObject;
    }

    @Override
    public Coordinate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Coordinate(jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt());
    }
}
