package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class QuitGameEvent extends Event {

    private final static String id = "QUIT_GAME";

    public QuitGameEvent() {
        super(id);
    }

    public QuitGameEvent(Response response) {
        super(id, response);
    }
}
