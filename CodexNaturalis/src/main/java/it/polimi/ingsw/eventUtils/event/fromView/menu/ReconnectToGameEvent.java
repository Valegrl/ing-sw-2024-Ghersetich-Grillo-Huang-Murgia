package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a user attempts to reconnect to a game.
 */
public class ReconnectToGameEvent extends FeedbackEvent {

    /**
     * The unique identifier for a ReconnectToGameEvent.
     */
    private final static String id = EventID.RECONNECT_TO_GAME.getID();

    /**
     * The ID of the game to reconnect to.
     */
    private final String gameID;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the game ID.
     *
     * @param gameID The ID of the game to reconnect to.
     */
    public ReconnectToGameEvent(String gameID) {
        super(id);
        this.gameID = gameID;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * It also initializes the game ID with null.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public ReconnectToGameEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.gameID = null;
    }

    /**
     * @return The ID of the game to reconnect to.
     */
    public String getGameID() {
        return gameID;
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
