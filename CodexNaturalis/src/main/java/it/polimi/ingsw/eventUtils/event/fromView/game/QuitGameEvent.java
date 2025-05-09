package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player quits the game.
*/
public class QuitGameEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.QUIT_GAME.getID();

    /**
     * Constructor for the client side (View).
     */
    public QuitGameEvent() {
        super(id);
    }

    /**
     * Constructor for the server side (Controller).
     * This constructor initializes the QuitGameEvent with a feedback and a message.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public QuitGameEvent(Feedback feedback, String message) {
        super(id, feedback, message);
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
