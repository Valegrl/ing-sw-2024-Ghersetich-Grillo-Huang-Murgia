package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class isMyTurnEvent extends Event {

    private final static String id = "IS_MY_TURN";

    public isMyTurnEvent() {
        super(id);
    }

    public isMyTurnEvent(Response response) {
        super(id, response);
    }
}
