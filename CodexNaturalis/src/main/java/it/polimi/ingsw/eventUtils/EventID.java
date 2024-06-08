package it.polimi.ingsw.eventUtils;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromModel.*;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.AvailablePositionsEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.local.NewGameStatusEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetLobbyInfoEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.eventUtils.event.internal.ServerDisconnectedEvent;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Enum for identifying different types of events.
 * Each event type is associated with a unique ID and a corresponding class.
 */
public enum EventID {

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
     * The {@link SelfTurnTimerExpiredEvent} ID.
     */
    SELF_TURN_TIMER_EXPIRED("SELF_TURN_TIMER_EXPIRED", SelfTurnTimerExpiredEvent.class),

    /**
     * The {@link UpdateGamePlayersEvent} ID.
     */
    UPDATE_GAME_PLAYERS("UPDATE_GAME_PLAYERS", UpdateGamePlayersEvent.class),

    /**
     * The {@link ChooseCardsSetupEvent} ID.
     */
    CHOOSE_CARDS_SETUP("CHOOSE_CARDS_SETUP", ChooseCardsSetupEvent.class),

    /**
     * The {@link SelfDrawCardEvent} ID.
     */
    SELF_DRAW_CARD("SELF_DRAW_CARD", SelfDrawCardEvent.class),

    /**
     * The {@link SelfPlaceCardEvent} ID.
     */
    SELF_PLACE_CARD("SELF_PLACE_CARD", SelfPlaceCardEvent.class),

    /**
     * The {@link NewTurnEvent} ID.
     */
    NEW_TURN("NEW_TURN", NewTurnEvent.class),

    /**
     * The {@link OtherDrawCardEvent} ID.
     */
    OTHER_DRAW_CARD("OTHER_DRAW_CARD", OtherDrawCardEvent.class),

    /**
     * The {@link OtherPlaceCardEvent} ID.
     */
    OTHER_PLACE_CARD("OTHER_PLACE_CARD", OtherPlaceCardEvent.class),

    /**
     * The {@link EndedGameEvent} ID.
     */
    ENDED_GAME("ENDED_GAME", EndedGameEvent.class),

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
     * The {@link NewGameStatusEvent} ID.
     */
    NEW_GAME_STATUS("SEE_OPPONENT_PLAY_AREA", NewGameStatusEvent.class),

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
     * The {@link ChatGMEvent} ID.
     */
    CHAT_GM("CHAT_GM", ChatGMEvent.class),

    /**
     * The {@link ChatPMEvent} ID.
     */
    CHAT_PM("CHAT_PM", ChatPMEvent.class),

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
     * The {@link ServerDisconnectedEvent} ID.
     */
    SERVER_DISCONNECTED("SERVER_DISCONNECTED", ServerDisconnectedEvent.class),

    /**
     * The {@link GetLobbyInfoEvent} ID.
     */
    GET_LOBBY_INFO("GET_LOBBY_INFO", GetLobbyInfoEvent.class),

    /**
     * The {@link GetChatMessagesEvent} ID.
     */
    GET_CHAT_MESSAGES("GET_CHAT_MESSAGES", GetChatMessagesEvent.class);

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
        localEvents.add(NEW_GAME_STATUS);
        localEvents.add(GET_LOBBY_INFO);
        localEvents.add(GET_CHAT_MESSAGES);
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
    public static EventID getByID(String ID) {
        return Arrays.stream(values())
                .filter(eventID -> eventID.getID().equals(ID))
                .findFirst()
                .orElse(INVALID);
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
