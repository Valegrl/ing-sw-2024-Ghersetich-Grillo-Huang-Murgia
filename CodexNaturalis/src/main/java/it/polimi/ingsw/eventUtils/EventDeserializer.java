package it.polimi.ingsw.eventUtils;

import com.google.gson.*;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromController.KickedPlayerFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromController.UpdateLobbyPlayersEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.PlayerIsChoosingSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChooseSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EventDeserializer implements JsonDeserializer<Event> {

    private static final Map<String, Class<?>> idToEvent = new HashMap<>() {{
        put("KICKED_PLAYER_FROM_LOBBY", KickedPlayerFromLobbyEvent.class);
        put("UPDATE_LOBBY_PLAYERS", UpdateLobbyPlayersEvent.class);
        put("PLAYER_IS_CHOOSING_SETUP", PlayerIsChoosingSetupEvent.class);
        put("UPDATE_LOCAL_MODEL", UpdateLocalModelEvent.class);
        put("CHOOSE_SETUP", ChooseSetupEvent.class);
        put("DRAW_CARD", DrawCardEvent.class);
        put("PLACE_CARD", PlaceCardEvent.class);
        put("QUIT_GAME", QuitGameEvent.class);
        put("KICK_FROM_LOBBY", KickFromLobbyEvent.class);
        put("PLAYER_READY", PlayerReadyEvent.class);
        put("PLAYER_UNREADY", PlayerUnreadyEvent.class);
        put("QUIT_LOBBY", QuitLobbyEvent.class);
        put("AVAILABLE_LOBBIES", AvailableLobbiesEvent.class);
        put("CREATE_LOBBY", CreateLobbyEvent.class);
        put("DELETE_ACCOUNT", DeleteAccountEvent.class);
        put("GET_MY_OFFLINE_GAMES", GetMyOfflineGamesEvent.class);
        put("JOIN_LOBBY", JoinLobbyEvent.class);
        put("LOGIN", LoginEvent.class);
        put("LOGOUT", LogoutEvent.class);
        put("RECONNECT_TO_GAME", ReconnectToGameEvent.class);
        put("REGISTER", RegisterEvent.class);
        put("PING", PingEvent.class);
        put("PONG", PongEvent.class);
    }};

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        String id = jsonObject.get("ID").getAsString();

        return context.deserialize(jsonObject, idToEvent.get(id));
    }
}
