package it.polimi.ingsw.eventUtils.event;

public abstract class Event /*<T>*/ {
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
