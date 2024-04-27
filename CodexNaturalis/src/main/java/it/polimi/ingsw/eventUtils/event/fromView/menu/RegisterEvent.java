package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class RegisterEvent extends Event {

    private final static String id = "REGISTER";

    public RegisterEvent() {
        super(id);
    }

    public RegisterEvent(Response response) {
        super(id, response);
    }
}
