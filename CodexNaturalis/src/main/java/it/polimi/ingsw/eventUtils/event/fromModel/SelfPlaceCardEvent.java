package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.turnAction.place.SelfPlaceCardData;

/**
 * The SelfPlaceCardEvent class represents an event that occurs when a card is placed by the current player.
 */
public class SelfPlaceCardEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.SELF_PLACE_CARD.getID();

    /**
     * The message associated with the event.
     */
    private final String message;

    /**
     * The data of the placed card.
     */
    private final SelfPlaceCardData selfPlaceCardData;

    /**
     * Constructs a new SelfPlaceCardEvent with the given placed card data and a message.
     *
     * @param data The data of the placed card.
     * @param message The message associated with the event.
     */
    public SelfPlaceCardEvent(SelfPlaceCardData data, String message) {
        super(id);
        this.selfPlaceCardData = data;
        this.message = message;
    }

    /**
     * @return The message associated with the event.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The data of the placed card.
     */
    public SelfPlaceCardData getMyPlaceCardData() {
        return selfPlaceCardData;
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
