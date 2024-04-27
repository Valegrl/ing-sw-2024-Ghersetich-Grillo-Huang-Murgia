package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class seeOpponentPlayAreaEvent extends Event {

    private final static String id = "SEE_OPPONENT_PLAY_AREA";

    public seeOpponentPlayAreaEvent() {
        super(id);
    }

    public seeOpponentPlayAreaEvent(Response response) {
        super(id, response);
    }
}
