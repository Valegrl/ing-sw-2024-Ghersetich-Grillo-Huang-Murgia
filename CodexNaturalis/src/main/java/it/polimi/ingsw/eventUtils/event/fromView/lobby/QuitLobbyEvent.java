package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class QuitLobbyEvent extends Event {

    private final static String id = "QUIT_LOBBY";

    public QuitLobbyEvent() {
        super(id);
    }

    public QuitLobbyEvent(Response response) {
        super(id, response);
    }
}
