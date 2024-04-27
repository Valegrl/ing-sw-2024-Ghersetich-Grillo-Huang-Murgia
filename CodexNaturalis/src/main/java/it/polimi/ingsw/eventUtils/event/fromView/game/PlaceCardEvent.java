package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class PlaceCardEvent extends Event {

    private final static String id = "PLACE_CARD";

    public PlaceCardEvent() {
        super(id);
    }

    public PlaceCardEvent(Response response) {
        super(id, response);
    }
}
