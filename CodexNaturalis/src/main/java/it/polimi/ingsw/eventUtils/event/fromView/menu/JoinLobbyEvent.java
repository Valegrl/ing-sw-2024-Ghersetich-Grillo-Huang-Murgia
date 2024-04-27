package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class JoinLobbyEvent extends Event {

    private final static String id = "JOIN_LOBBY";

    public JoinLobbyEvent() {
        super(id);
    }

    public JoinLobbyEvent(Response response) {
        super(id, response);
    }
}
