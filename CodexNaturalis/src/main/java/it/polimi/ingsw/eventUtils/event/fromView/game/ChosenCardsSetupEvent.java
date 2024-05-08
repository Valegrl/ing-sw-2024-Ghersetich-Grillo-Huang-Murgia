package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is used to set up the chosen cards for the game.
 */
public class ChosenCardsSetupEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHOSEN_CARDS_SETUP.getID();

    /**
     * The ID of the chosen objective card.
     */
    private final String objectiveCardID;

    /**
     * A boolean indicating whether the start card should be flipped.
     */
    private final Boolean flipStartCard;

    /**
     * Constructor for the client side (View).
     *
     * @param objectiveCardID the ID of the chosen objective card
     * @param flip whether the start card should be flipped
     */
    public ChosenCardsSetupEvent(String objectiveCardID, boolean flip) {
        super(id);
        this.objectiveCardID = objectiveCardID;
        this.flipStartCard = flip;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback the feedback for the event
     * @param message the message for the event
     */
    public ChosenCardsSetupEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.objectiveCardID = null;
        this.flipStartCard = null;
    }

    /**
     * @return the ID of the chosen objective card
     */
    public String getObjectiveCardID() {
        return objectiveCardID;
    }

    /**
     * @return whether the start card should be flipped
     */
    public Boolean getFlipStartCard() {
        return flipStartCard;
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
