package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a PongEvent in the application.
 * A PongEvent is triggered by both the server and the client in response to a PingEvent to confirm connectivity.
 */
public class PongEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.PONG.getID();

    /**
     * Constructor for creating a new PongEvent.
     */
    public PongEvent() {
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
