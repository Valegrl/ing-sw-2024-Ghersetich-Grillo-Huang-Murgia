package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a PingEvent in the application.
 * A PingEvent is triggered by both the server and the client to verify connectivity.
 */
public class PingEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.PING.getID();

    /**
     * Constructor for creating a new PingEvent.
     */
    public PingEvent() {
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
