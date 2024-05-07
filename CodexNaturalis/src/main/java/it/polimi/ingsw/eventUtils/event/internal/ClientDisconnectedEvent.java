package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a ClientDisconnectedEvent in the application (server side).
 * A ClientDisconnectedEvent is triggered when a client disconnects from the server.
 */
public class ClientDisconnectedEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CLIENT_DISCONNECTED.getID();

    /**
     * Constructor for creating a new ClientDisconnectedEvent.
     */
    public ClientDisconnectedEvent() {
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
