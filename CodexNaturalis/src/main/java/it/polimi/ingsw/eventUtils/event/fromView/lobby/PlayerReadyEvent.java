package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class PlayerReadyEvent extends Event {

    private final static String id = "PLAYER_READY";

    public PlayerReadyEvent() {
        super(id);
    }

    public PlayerReadyEvent(Response response) {
        super(id, response);
    }
}
