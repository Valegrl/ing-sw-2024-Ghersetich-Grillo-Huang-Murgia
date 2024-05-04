package it.polimi.ingsw.eventUtils;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.UpdateLocalModelEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.AvailablePositionsEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.IsMyTurnEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.SeeOpponentPlayAreaEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.eventUtils.event.internal.ServerCrashedEvent;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum for identifying different types of events.
 * Each event type is associated with a unique ID and a corresponding class.
 */
public enum EventID {

    /**
     * The {@link ChooseCardsSetupEvent} ID.
     */
    CHOOSE_CARDS_SETUP("CHOOSE_CARDS_SETUP", ChooseCardsSetupEvent.class),

    /**
     * The {@link ChooseTokenSetupEvent} ID.
     */
    CHOOSE_TOKEN_SETUP("CHOOSE_TOKEN_SETUP", ChooseTokenSetupEvent.class),

    /**
     * The {@link InvalidEvent} ID.
     */
    INVALID("INVALID", InvalidEvent.class),

    /**
     * The {@link KickedPlayerFromLobbyEvent} ID.
     */
    KICKED_PLAYER_FROM_LOBBY("KICKED_PLAYER_FROM_LOBBY", KickedPlayerFromLobbyEvent.class),

    /**
     * The {@link UpdateGamePlayersEvent} ID.
     */
    UPDATE_GAME_PLAYERS("UPDATE_GAME_PLAYERS", UpdateGamePlayersEvent.class),

    /**
     * The {@link UpdateLobbyPlayersEvent} ID.
     */
    UPDATE_LOBBY_PLAYERS("UPDATE_LOBBY_PLAYERS", UpdateLobbyPlayersEvent.class),

    /**
     * The {@link UpdateLocalModelEvent} ID.
     */
    UPDATE_LOCAL_MODEL("UPDATE_LOCAL_MODEL", UpdateLocalModelEvent.class),

    /**
     * The {@link AvailablePositionsEvent} ID.
     */
    AVAILABLE_POSITIONS("AVAILABLE_POSITIONS", AvailablePositionsEvent.class),

    /**
     * The {@link IsMyTurnEvent} ID.
     */
    IS_MY_TURN("IS_MY_TURN", IsMyTurnEvent.class),

    /**
     * The {@link SeeOpponentPlayAreaEvent} ID.
     */
    SEE_OPPONENT_PLAY_AREA("SEE_OPPONENT_PLAY_AREA", SeeOpponentPlayAreaEvent.class),

    /**
     * The {@link ChosenCardsSetupEvent} ID.
     */
    CHOSEN_CARDS_SETUP("CHOSEN_CARDS_SETUP", ChosenCardsSetupEvent.class),

    /**
     * The {@link ChosenTokenSetupEvent} ID.
     */
    CHOSEN_TOKEN_SETUP("CHOSEN_TOKEN_SETUP", ChosenTokenSetupEvent.class),

    /**
     * The {@link DrawCardEvent} ID.
     */
    DRAW_CARD("DRAW_CARD", DrawCardEvent.class),

    /**
     * The {@link PlaceCardEvent} ID.
     */
    PLACE_CARD("PLACE_CARD", PlaceCardEvent.class),

    /**
     * The {@link QuitGameEvent} ID.
     */
    QUIT_GAME("QUIT_GAME", QuitGameEvent.class),

    /**
     * The {@link KickFromLobbyEvent} ID.
     */
    KICK_FROM_LOBBY("KICK_FROM_LOBBY", KickFromLobbyEvent.class),

    /**
     * The {@link PlayerReadyEvent} ID.
     */
    PLAYER_READY("PLAYER_READY", PlayerReadyEvent.class),

    /**
     * The {@link PlayerUnreadyEvent} ID.
     */
    PLAYER_UNREADY("PLAYER_UNREADY", PlayerUnreadyEvent.class),

    /**
     * The {@link QuitLobbyEvent} ID.
     */
    QUIT_LOBBY("QUIT_LOBBY", QuitLobbyEvent.class),

    /**
     * The {@link AvailableLobbiesEvent} ID.
     */
    AVAILABLE_LOBBIES("AVAILABLE_LOBBIES", AvailableLobbiesEvent.class),

    /**
     * The {@link CreateLobbyEvent} ID.
     */
    CREATE_LOBBY("CREATE_LOBBY", CreateLobbyEvent.class),

    /**
     * The {@link DeleteAccountEvent} ID.
     */
    DELETE_ACCOUNT("DELETE_ACCOUNT", DeleteAccountEvent.class),

    /**
     * The {@link GetMyOfflineGamesEvent} ID.
     */
    GET_MY_OFFLINE_GAMES("GET_MY_OFFLINE_GAMES", GetMyOfflineGamesEvent.class),

    /**
     * The {@link JoinLobbyEvent} ID.
     */
    JOIN_LOBBY("JOIN_LOBBY", JoinLobbyEvent.class),

    /**
     * The {@link LoginEvent} ID.
     */
    LOGIN("LOGIN", LoginEvent.class),

    /**
     * The {@link LogoutEvent} ID.
     */
    LOGOUT("LOGOUT", LogoutEvent.class),

    /**
     * The {@link ReconnectToGameEvent} ID.
     */
    RECONNECT_TO_GAME("RECONNECT_TO_GAME", ReconnectToGameEvent.class),

    /**
     * The {@link RegisterEvent} ID.
     */
    REGISTER("REGISTER", RegisterEvent.class),

    /**
     * The {@link PingEvent} ID.
     */
    PING("PING", PingEvent.class),

    /**
     * The {@link PongEvent} ID.
     */
    PONG("PONG", PongEvent.class),

    /**
     * The {@link ClientDisconnectedEvent} ID.
     */
    CLIENT_DISCONNECTED("CLIENT_DISCONNECTED", ClientDisconnectedEvent.class),

    /**
     * The {@link ServerCrashedEvent} ID.
     */
    SERVER_CRASHED("SERVER_CRASHED", ServerCrashedEvent.class);

    /**
     * The unique ID of the event type.
     */
    private final String ID;

    /**
     * The class associated with the event type.
     */
    private final Class<?> eventClass;

    /**
     * A set of events that are local.
     */
    private static final Set<EventID> localEvents = EnumSet.noneOf(EventID.class);

    static {
        localEvents.add(AVAILABLE_POSITIONS);
        localEvents.add(IS_MY_TURN);
        localEvents.add(SEE_OPPONENT_PLAY_AREA);
    }

    /**
     * Constructs and EventID with the specified ID and {@link it.polimi.ingsw.eventUtils.event.Event Event} class.
     *
     * @param ID The unique ID of the event type.
     * @param eventClass The class associated with the event type.
     */
    EventID(String ID, Class<?> eventClass) {
        this.ID = ID;
        this.eventClass = eventClass;
    }

    /**
     * Get the EventID by its ID.
     *
     * @param ID The ID of the event type.
     * @return The EventID, or INVALID if the ID does not match any EventID.
     */
    private static EventID getByID(String ID) {
        for (EventID eventID : EventID.values()) {
            if (eventID.getID().equals(ID))
                return eventID;
        }
        return INVALID;
    }

    /**
     * Check if an Event is local.
     *
     * @param id The ID of the Event.
     * @return true if the Event is local, false otherwise.
     */
    public static boolean isLocal(String id) {
        return localEvents.contains(getByID(id));
    }

    /**
     * Get the class for an Event with the given ID.
     *
     * @param eventID The ID of the Event.
     * @return The class of the Event, or {@link InvalidEvent} if the ID does not match any Event.
     */
    public static Class<?> getClassForEvent(String eventID) {
        for (EventID id : EventID.values()) {
            if (id.getID().equals(eventID)) {
                return id.getEventClass();
            }
        }
        return INVALID.getEventClass();
    }

    /**
     * Get the ID of the Event.
     *
     * @return The ID of the Event.
     */
    public String getID() {
        return ID;
    }

    /**
     * Get the class of the Event.
     *
     * @return The class of the Event.
     */
    public Class<?> getEventClass() {
        return eventClass;
    }
}
