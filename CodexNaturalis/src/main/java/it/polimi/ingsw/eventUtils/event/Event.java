package it.polimi.ingsw.eventUtils.event;

import it.polimi.ingsw.eventUtils.event.fromView.Response;

public abstract class Event {
    private final String ID;

    private Response response = null;

    public String getID() { return ID; }

    public Response getResponse() { return response; }


    public Event(String ID) {
        this.ID = ID;
    }

    public Event(String ID, Response response) {
        this.ID = ID;
        this.response = response;
    }
}
