package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class AvailablePositionsEvent extends Event {

    private final static String id = "AVAILABLE_POSITIONS";

    public AvailablePositionsEvent() {
        super(id);
    }

    public AvailablePositionsEvent(Response response) {
        super(id, response);
    }
}
