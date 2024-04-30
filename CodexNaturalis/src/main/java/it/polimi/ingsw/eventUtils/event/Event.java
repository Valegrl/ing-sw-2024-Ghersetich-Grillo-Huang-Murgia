package it.polimi.ingsw.eventUtils.event;

import java.io.Serializable;

public abstract class Event implements Serializable/*<T>*/ {
    private final String ID;

    public Event(String ID) {
        this.ID = ID;
    }

    public String getID() { return ID; }

    /*
    public T getData() {
        return null;
    }
    */
}
