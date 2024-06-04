package it.polimi.ingsw.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeTypeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    @Override
    public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("HH:mm"));
    }
}
