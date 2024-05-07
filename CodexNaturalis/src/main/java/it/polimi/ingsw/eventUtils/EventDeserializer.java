package it.polimi.ingsw.eventUtils;

import com.google.gson.*;

import it.polimi.ingsw.eventUtils.event.Event;

import java.lang.reflect.Type;

/**
 * This class is responsible for deserializing JSON into Event objects.
 */
public class EventDeserializer implements JsonDeserializer<Event> {

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        String id = jsonObject.get("ID").getAsString();

        return context.deserialize(jsonObject, EventID.getClassForEvent(id));
    }
}
