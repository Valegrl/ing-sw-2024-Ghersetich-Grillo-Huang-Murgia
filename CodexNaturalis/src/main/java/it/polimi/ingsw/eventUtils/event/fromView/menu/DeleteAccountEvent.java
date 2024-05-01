package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents an event that initiates the deletion of an account.
 * This event is a type of FeedbackEvent.
 */
public class DeleteAccountEvent extends FeedbackEvent<Object> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = "DELETE_ACCOUNT";

    /**
     * Constructor for View (client). Initializes the event with the specified id.
     */
    public DeleteAccountEvent() {
        super(id);
    }

    /**
     * Constructor for Controller (server). Initializes the event with the specified id, feedback, and message.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public DeleteAccountEvent(Feedback feedback, String message) {
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
