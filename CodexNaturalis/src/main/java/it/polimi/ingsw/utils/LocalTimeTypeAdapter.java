package it.polimi.ingsw.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is a custom serializer and deserializer for the LocalTime class.
 * It implements JsonSerializer and JsonDeserializer interfaces provided by Gson.
 */
public class LocalTimeTypeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    /**
     * This method is used to convert a LocalTime object into a JsonElement.
     *
     * @param time The LocalTime object to be serialized.
     * @param typeOfSrc The type of the source object.
     * @param context The context for serialization.
     * @return A JsonElement corresponding to the specified LocalTime object.
     */
    @Override
    public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    /**
     * This method is used to convert a JsonElement back into a LocalTime object.
     *
     * @param json The JsonElement being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context The context for deserialization.
     * @return A LocalTime object corresponding to the specified JsonElement.
     * @throws JsonParseException if json is not in the expected format of LocalTime.
     */
    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("HH:mm"));
    }
}
