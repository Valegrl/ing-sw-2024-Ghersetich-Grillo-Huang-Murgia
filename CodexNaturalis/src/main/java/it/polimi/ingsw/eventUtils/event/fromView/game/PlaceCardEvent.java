package it.polimi.ingsw.eventUtils.event.fromView.game;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents a PlaceCardEvent in the application.
 * A PlaceCardEvent is triggered when a card is placed on the playArea.
 */
public class PlaceCardEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.PLACE_CARD.getID();

    /**
     * The ID of the card being placed.
     */
    private String cardID;

    /**
     * The position where the card is being placed.
     */
    private Coordinate pos;

    /**
     * The state of the card, whether it's flipped or not.
     */
    private boolean flipped;

    /**
     * Constructor for the client side (View).
     *
     * @param cardID The ID of the card being placed.
     * @param pos The position where the card is being placed.
     * @param flipped The state of the card, whether it's flipped or not.
     */
    public PlaceCardEvent(String cardID, Coordinate pos, boolean flipped) {
        super(id);
        this.cardID = cardID;
        this.pos = new Coordinate(pos.getX(), pos.getY());
        this.flipped = flipped;
    }

    /**
     * Constructor for the server side (Controller). New DrawCardEvent with a feedback and message.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public PlaceCardEvent(Feedback feedback, String message) {
        super(id, feedback, message);
    }

    /**
     * @return The ID of the card.
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * @return The position where the card is being placed.
     */
    public Coordinate getPos() {
        return pos;
    }

    /**
     * @return The state of the card, whether it's flipped or not.
     */
    public boolean isFlipped() {
        return flipped;
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
