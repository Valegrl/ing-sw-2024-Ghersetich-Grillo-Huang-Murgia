package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.eventUtils.event.Event;

public class UpdateLobbyPlayersEvent extends Event {

    private final static String id = "UPDATE_LOBBY_PLAYERS";

    public UpdateLobbyPlayersEvent() {
        super(id);
    }
}
