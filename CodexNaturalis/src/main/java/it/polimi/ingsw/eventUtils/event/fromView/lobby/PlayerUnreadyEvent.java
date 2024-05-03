package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player is not ready in the lobby.
 */
public class PlayerUnreadyEvent extends FeedbackEvent<Object> {

    /**
     * The unique identifier for a PlayerUnreadyEvent.
     */
    private final static String id = EventID.PLAYER_UNREADY.getID();

    /**
     * Constructor for View (client).
     * It initializes the superclass with the unique identifier for this event type.
     */
    public PlayerUnreadyEvent() {
        super(id);
    }

    /**
     * Constructor for Controller (server).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public PlayerUnreadyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
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
