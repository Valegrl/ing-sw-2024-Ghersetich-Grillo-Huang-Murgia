package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a SelfTurnTimerExpiredEvent in the game.
 * A SelfTurnTimerExpiredEvent is triggered when the timer for a player's turn expires.
 * This event is sent only to the player whose turn has expired.
*/
public class SelfTurnTimerExpiredEvent extends Event{
    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.SELF_TURN_TIMER_EXPIRED.getID();

    /**
     * Constructs a new SelfTurnTimerExpiredEvent with the unique identifier.
     */
    public SelfTurnTimerExpiredEvent() {
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
