package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a DrawCardEvent in the game.
 * A DrawCardEvent is triggered when a card is drawn from the deck.
 * It contains information about the card type, the index of the card, and a message associated with the event.
 */
public class DrawCardEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.DRAW_CARD.getID();

    /**
     * The index of the card to be drawn from the visible cards array.
     * If the index is in the range [0, 2), a card is drawn from the visible cards.
     * Otherwise, a card is drawn from the top of the deck.
     */
    private int index;

    /**
     * The type of the card drawn.
     */
    private CardType cardType;

    /**
     * The message associated with the event.
     */
    private String message;

    /**
     * Constructor for the client side (View). New DrawCardEvent with a card type and index.
     *
     * @param cardType The type of the card drawn.
     * @param index The index of the card.
     */
    public DrawCardEvent(CardType cardType, int index) {
        super(id);
        this.cardType = cardType;
        this.index = index;
    }

    /**
     * Constructor for the server side (Controller). New DrawCardEvent with a feedback and message.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public DrawCardEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.message = message;
        this.cardType = null;
    }

    /**
     * @return The index of the card.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return The type of the card drawn.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
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
