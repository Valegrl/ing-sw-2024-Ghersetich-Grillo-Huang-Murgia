package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class KickFromLobbyEvent extends Event {

    private final static String id = "KICK_FROM_LOBBY";

    public KickFromLobbyEvent() {
        super(id);
    }

    public KickFromLobbyEvent(Response response) {
        super(id, response);
    }
}
