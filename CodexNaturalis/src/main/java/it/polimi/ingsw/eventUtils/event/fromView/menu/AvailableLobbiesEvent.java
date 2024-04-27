package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class AvailableLobbiesEvent extends Event {

    private final static String id = "AVAILABLE_LOBBIES";

    public AvailableLobbiesEvent() {
        super(id);
    }

    public AvailableLobbiesEvent(Response response) {
        super(id, response);
    }
}
