package it.polimi.ingsw.eventUtils;

import com.google.gson.*;

import it.polimi.ingsw.eventUtils.event.Event;

import java.lang.reflect.Type;

/**
 * This class is responsible for serializing/deserializing Event objects with json.
 */
public class EventTypeAdapter implements JsonDeserializer<Event>, JsonSerializer<Event> {

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        if (!jsonObject.has("ID")) {
            throw new JsonParseException("Missing 'ID' field in JSON object");
        }

        String id = jsonObject.get("ID").getAsString();
        return context.deserialize(jsonObject, EventID.getClassForEvent(id));
    }

    @Override
    public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return JsonNull.INSTANCE;

        JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();
        String id = src.getID();
        if (id == null) {
            throw new JsonParseException("Unknown event class: " + src.getClass().getSimpleName());
        }

        jsonObject.addProperty("ID", id);
        return jsonObject;
    }
}
