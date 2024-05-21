package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.turnAction.place.MyPlaceCardData;

/**
 * The MyPlaceCardEvent class represents an event that occurs when a card is placed by the current player.
 * It includes the data of the placed card.
 */
public class MyPlaceCardEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.MY_PLACE_CARD.getID();

    /**
     * The data of the placed card.
     */
    private final MyPlaceCardData myPlaceCardData;

    /**
     * Constructs a new MyPlaceCardEvent with the given placed card data.
     *
     * @param data The data of the placed card.
     */
    public MyPlaceCardEvent(MyPlaceCardData data) {
        super(id);
        this.myPlaceCardData = data;
    }

    /**
     * @return The data of the placed card.
     */
    public MyPlaceCardData getMyPlaceCardData() {
        return myPlaceCardData;
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
