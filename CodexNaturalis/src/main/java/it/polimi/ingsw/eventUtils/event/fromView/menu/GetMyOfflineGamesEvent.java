package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class GetMyOfflineGamesEvent extends Event {

    private final static String id = "GET_MY_OFFLINE_GAMES";

    public GetMyOfflineGamesEvent() {
        super(id);
    }

    public GetMyOfflineGamesEvent(Response response) {
        super(id, response);
    }
}
