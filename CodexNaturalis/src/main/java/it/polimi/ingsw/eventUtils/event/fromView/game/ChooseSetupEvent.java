package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Response;

public class ChooseSetupEvent extends Event {

    private final static String id = "CHOOSE_SETUP";

    public ChooseSetupEvent() {
        super(id);
    }

    public ChooseSetupEvent(Response response) {
        super(id, response);
    }
}
