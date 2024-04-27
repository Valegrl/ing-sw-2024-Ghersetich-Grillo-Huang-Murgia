package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.eventUtils.event.Event;

public class KickedPlayerFromLobbyEvent extends Event {

    private final static String id = "KICKED_PLAYER_FROM_LOBBY";

    public KickedPlayerFromLobbyEvent() {
        super(id);
    }
}
