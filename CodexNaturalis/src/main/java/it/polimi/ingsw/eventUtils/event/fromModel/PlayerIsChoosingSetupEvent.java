package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.eventUtils.event.Event;

public class PlayerIsChoosingSetupEvent extends Event {

    private final static String id = "PLAYER_IS_CHOOSING_SETUP";

    public PlayerIsChoosingSetupEvent() {
        super(id);
    }
}
