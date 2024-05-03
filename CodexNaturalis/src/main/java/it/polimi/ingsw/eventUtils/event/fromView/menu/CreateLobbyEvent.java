package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a lobby is created.
 */
public class CreateLobbyEvent extends FeedbackEvent {

    /**
     * The unique identifier for a CreateLobbyEvent.
     */
    private final static String id = EventID.CREATE_LOBBY.getID();

    /**
     * The ID of the lobby to be created.
     */
    private final String lobbyID;

    /**
     * The number of players required for the lobby.
     */
    private final int requiredPlayers;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and sets the lobby ID and the number of players.
     *
     * @param lobbyID The ID of the lobby to be created.
     * @param nPlayers The number of players required for the lobby.
     */
    public CreateLobbyEvent(String lobbyID, Integer nPlayers) {
        super(id);
        this.lobbyID = lobbyID;
        this.requiredPlayers = nPlayers;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * The lobby ID is set to null and the number of players is set to -1.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public CreateLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.lobbyID = null;
        this.requiredPlayers = -1;
    }

    /**
     * @return The ID of the lobby to be created.
     */
    public String getLobbyID() {
        return lobbyID;
    }

    /**
     * @return The number of players required for the lobby.
     */
    public int getRequiredPlayers() {
        return requiredPlayers;
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
