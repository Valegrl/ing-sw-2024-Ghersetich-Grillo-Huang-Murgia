package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents an event that is triggered when a user attempts to join a lobby.
 * It contains information about the lobby such as its ID and the ready status of the players.
 */
public class JoinLobbyEvent extends FeedbackEvent {

    /**
     * The unique identifier for a JoinLobbyEvent.
     */
    private final static String id = EventID.JOIN_LOBBY.getID();

    /**
     * The ID of the lobby to join.
     */
    private final String lobbyID;

    /**
     * The map of players and their ready status in the lobby.
     */
    private final Map<String, Boolean> readyStatusPlayers;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the lobby ID.
     *
     * @param lobbyID The ID of the lobby to join.
     */
    public JoinLobbyEvent(String lobbyID) {
        super(id);
        this.lobbyID = lobbyID;
        this.readyStatusPlayers = new LinkedHashMap<>();
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * The lobby ID is set to null and the ready status of the players is initialized.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public JoinLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.lobbyID = null;
        this.readyStatusPlayers = new LinkedHashMap<>();
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * It also sets the lobby ID and initializes the ready status of the players with the provided map.
     *
     * @param feedback The feedback for the event.
     * @param usernames The map of players and their ready status.
     * @param lobbyID The ID of the lobby.
     * @param message The message for the event.
     */
    public JoinLobbyEvent(Feedback feedback, Map<String, Boolean> usernames, String lobbyID, String message) {
        super(id, feedback, message);
        this.lobbyID = lobbyID;
        this.readyStatusPlayers = new LinkedHashMap<>(usernames);
    }

    /**
     * @return The ID of the lobby to join.
     */
    public String getLobbyID() {
        return lobbyID;
    }

    /**
     * @return A new map containing the players and their ready status.
     */
    public Map<String, Boolean> getReadyStatusPlayers() {
        return readyStatusPlayers;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
