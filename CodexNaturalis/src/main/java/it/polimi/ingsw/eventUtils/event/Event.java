package it.polimi.ingsw.eventUtils.event;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import java.io.Serializable;

/**
 * Abstract class representing an event.
 * This class is serializable and provides a method for receiving the event.
 */
public abstract class Event implements Serializable {
    /**
     * The identifier for this type of event.
     */
    private final String ID;

    /**
     * Constructor. Initializes the event with the specified id.
     *
     * @param ID The id of the event.
     */
    public Event(String ID) {
        this.ID = ID;
    }

    /**
     * @return The id of the event.
     */
    public String getID() { return ID; }

    /**
     * Method to be implemented by subclasses to handle the event when received by a ViewEventReceiver.
     *
     * @param viewEventReceiver The ViewEventReceiver that receives the event.
     */
    public abstract void receiveEvent(ViewEventReceiver viewEventReceiver);

    /**
     * Method to be implemented by subclasses to handle the event when received by a VirtualView.
     *
     * @param virtualView The VirtualView that receives the event.
     */
    public abstract void receiveEvent(VirtualView virtualView);
}
