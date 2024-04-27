package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.eventUtils.event.Event;

public class ClientDisconnectedEvent extends Event {

    private final static String id = "CLIENT_DISCONNECTED";

    public ClientDisconnectedEvent() {
        super(id);
    }
}
