package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class CreateLobbyEvent extends Event {

    private final static String id = "CREATE_LOBBY";

    public CreateLobbyEvent() {
        super(id);
    }

    public CreateLobbyEvent(Response response) {
        super(id, response);
    }
}
