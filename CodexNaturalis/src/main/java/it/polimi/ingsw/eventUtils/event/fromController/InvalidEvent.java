package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an invalid event that is used as a response from the Controller to the Client
 * in the case of an unrecognized event.
 */
public class InvalidEvent extends Event {
    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.INVALID.getID();

    /**
     * Constructs a new InvalidEvent with the unique identifier.
     */
    public InvalidEvent() {
        super(id);
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
