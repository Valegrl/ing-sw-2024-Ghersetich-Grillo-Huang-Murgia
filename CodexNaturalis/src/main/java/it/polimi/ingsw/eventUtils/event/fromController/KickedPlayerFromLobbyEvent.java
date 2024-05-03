package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player has been kicked from the lobby.
 */
public class KickedPlayerFromLobbyEvent extends Event {

    /**
     * The unique identifier for a KickedPlayerFromLobbyEvent.
     */
    private final static String id = EventID.KICKED_PLAYER_FROM_LOBBY.getID();

    /**
     * The message associated with the event.
     */
    private final String message;

    /**
     * Constructor for creating a new KickedPlayerFromLobbyEvent with a message.
     *
     * @param m The message associated with the event.
     */
    public KickedPlayerFromLobbyEvent(String m) {
        super(id);
        this.message = m;
    }

    /**
     * Method for getting the message of the event.
     *
     * @return The message associated with the event.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
