package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class LoginEvent extends Event {

    private final static String id = "LOGIN";

    public LoginEvent() {
        super(id);
    }

    public LoginEvent(Response response) {
        super(id, response);
    }
}
