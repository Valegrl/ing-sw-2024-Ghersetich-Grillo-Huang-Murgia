package it.polimi.ingsw.eventUtils;

import com.google.gson.*;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.KickedPlayerFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromController.UpdateLobbyPlayersEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.PlayerIsChoosingSetupEvent;

import java.lang.reflect.Type;

public class EventDeserializer implements JsonDeserializer<Event> {

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        String id = jsonObject.get("ID").getAsString();

        return switch (id) {
            case ("KICKED_PLAYER_FROM_LOBBY") -> context.deserialize(json, KickedPlayerFromLobbyEvent.class);
            case ("UPDATE_LOBBY_PLAYERS") -> context.deserialize(json, UpdateLobbyPlayersEvent.class);
            default -> null;
        };
    }
}
