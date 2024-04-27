package it.polimi.ingsw.eventUtils.event;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;

public abstract class Event {
    private final String ID;

    public String getID() { return ID; }

    public Event(String ID) {
        this.ID = ID;
    }
}
