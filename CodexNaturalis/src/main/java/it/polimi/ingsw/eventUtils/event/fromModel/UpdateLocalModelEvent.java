package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.eventUtils.event.Event;

public class UpdateLocalModelEvent extends Event {

    private final static String id = "UPDATE_LOCAL_MODEL";

    public UpdateLocalModelEvent() {
        super(id);
    }
}
