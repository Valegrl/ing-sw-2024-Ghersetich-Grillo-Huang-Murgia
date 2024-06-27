package it.polimi.ingsw.eventUtils.event.fromView.game.local;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that indicates a new game status.
 */
public class NewGameStatusEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.NEW_GAME_STATUS.getID();

    /**
     * Default constructor. Initializes the event with the unique identifier.
     */
    public NewGameStatusEvent() {
        super(id);
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
