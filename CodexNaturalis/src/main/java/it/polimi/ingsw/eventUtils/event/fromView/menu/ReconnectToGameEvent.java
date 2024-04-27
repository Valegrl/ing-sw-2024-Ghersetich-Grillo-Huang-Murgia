package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class ReconnectToGameEvent extends Event {

    private final static String id = "RECONNECT_TO_GAME";

    public ReconnectToGameEvent() {
        super(id);
    }

    public ReconnectToGameEvent(Response response) {
        super(id, response);
    }
}
