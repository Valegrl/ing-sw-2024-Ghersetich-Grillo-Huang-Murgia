package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a user attempts to log out.
 */
public class LogoutEvent extends FeedbackEvent {

    /**
     * The unique identifier for a LogoutEvent.
     */
    private final static String id = EventID.LOGOUT.getID();

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type.
     */
    public LogoutEvent() {
        super(id);
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public LogoutEvent(Feedback feedback, String message) {
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
