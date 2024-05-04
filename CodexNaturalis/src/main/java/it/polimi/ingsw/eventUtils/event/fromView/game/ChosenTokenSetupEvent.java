package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is used to set up the chosen token for the game.
 */
public class ChosenTokenSetupEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHOSEN_TOKEN_SETUP.getID();

    /**
     * The chosen Token color for the game.
     */
    private final Token color;

    /**
     * Constructor for the client side (View).
     *
     * @param c the chosen Token color for the game
     */
    public ChosenTokenSetupEvent(Token c) {
        super(id);
        this.color = c;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback the feedback for the event
     * @param message the message for the event
     */
    public ChosenTokenSetupEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.color = null;
    }

    /**
     * @return the chosen Token color for the game
     */
    public Token getColor() {
        return color;
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
