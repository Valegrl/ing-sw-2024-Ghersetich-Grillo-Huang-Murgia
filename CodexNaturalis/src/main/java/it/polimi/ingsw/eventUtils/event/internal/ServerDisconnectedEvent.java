package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event in the application that manages a server crash on the client side.
 */
 public class ServerDisconnectedEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.SERVER_DISCONNECTED.getID();

    /**
     * Constructs a new ServerCrashedEvent.
     */
    public ServerDisconnectedEvent() {
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
