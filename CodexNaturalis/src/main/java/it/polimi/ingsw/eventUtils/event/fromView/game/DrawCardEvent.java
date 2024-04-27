package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class DrawCardEvent extends Event {

    private final static String id = "DRAW_CARD";

    public DrawCardEvent() {
        super(id);
    }

    public DrawCardEvent(Response response) {
        super(id, response);
    }
}
