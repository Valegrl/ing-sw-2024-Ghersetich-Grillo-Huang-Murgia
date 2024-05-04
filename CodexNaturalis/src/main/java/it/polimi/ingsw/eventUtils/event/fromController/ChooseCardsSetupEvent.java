package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is used to set up the cards for the game.
 */
public class ChooseCardsSetupEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHOOSE_CARDS_SETUP.getID();

    /**
     * An array of ImmObjectiveCard objects representing the objective cards for the game.
     */
    private final ImmObjectiveCard[] objectiveCards;

    /**
     * An ImmStartCard object representing the start card for the game.
     */
    private final ImmStartCard startCard;

    /**
     * A string representing the message associated with this event.
     */
    private final String message;

    /**
     * Constructs a new ChooseCardsSetupEvent with the given objective cards, start card, and message.
     *
     * @param obj   the array of ImmObjectiveCard objects representing the objective cards for the game
     * @param start the ImmStartCard object representing the start card for the game
     * @param m     the message associated with this event
     */
    public ChooseCardsSetupEvent(ImmObjectiveCard[] obj, ImmStartCard start, String m) {
        super(id);
        this.message = m;
        this.objectiveCards = obj;
        this.startCard = start;
    }

    /**
     * @return the array of ImmObjectiveCard objects representing the objective cards for the game
     */
    public ImmObjectiveCard[] getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * @return the ImmStartCard object representing the start card for the game
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * @return the message associated with this event
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
