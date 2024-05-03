package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player is ready in the lobby.
 */
public class PlayerReadyEvent extends FeedbackEvent {

    /**
     * The unique identifier for a PlayerReadyEvent.
     */
    private final static String id = EventID.PLAYER_READY.getID();

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type.
     */
    public PlayerReadyEvent() {
        super(id);
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public PlayerReadyEvent(Feedback feedback, String message) {
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
