package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UIEventReceiver;

import java.util.List;

/**
 * Represents an event that initiates the reconnection to a game.
 * This event is a type of FeedbackEvent, which carries a game ID as data.
 */
public class ReconnectToGameEvent extends FeedbackEvent<String> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = "RECONNECT_TO_GAME";

    /**
     * The ID of the game to reconnect to.
     */
    private final String gameID;

    /**
     * Constructor for View (client). Initializes the event with the specified game ID.
     *
     * @param gameID The ID of the game to reconnect to.
     */
    public ReconnectToGameEvent(String gameID) {
        super(id);
        this.gameID = gameID;
    }

    /**
     * Constructor for Controller (server). Initializes the event with the specified id, feedback, and message.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public ReconnectToGameEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.gameID = null;
    }

    @Override
    public String getData() {
        return gameID;
    }

    @Override
    public void receiveEvent(UIEventReceiver uiEventReceiver) {
        uiEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
