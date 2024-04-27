package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class PlayerUnreadyEvent extends Event {

    private final static String id = "PLAYER_UNREADY";

    public PlayerUnreadyEvent() {
        super(id);
    }

    public PlayerUnreadyEvent(Response response) {
        super(id, response);
    }
}
