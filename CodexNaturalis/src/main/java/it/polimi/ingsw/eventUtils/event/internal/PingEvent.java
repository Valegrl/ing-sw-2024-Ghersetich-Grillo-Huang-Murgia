package it.polimi.ingsw.eventUtils.event.internal;

import it.polimi.ingsw.eventUtils.event.Event;

public class PingEvent extends Event {

    private final static String id = "PING";

    public PingEvent() {
        super(id);
    }
}
