package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.turnAction.draw.OtherDrawCardData;

/**
 * The OtherDrawCardEvent class represents an event that occurs when a card is drawn by another player.
 * It includes the data of the drawn card.
 */
public class OtherDrawCardEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.OTHER_DRAW_CARD.getID();

    /**
     * The data of the drawn card.
     */
    private final OtherDrawCardData otherDrawCardData;

    /**
     * Constructs a new OtherDrawCardEvent with the given drawn card data.
     *
     * @param data The data of the drawn card.
     */
    public OtherDrawCardEvent(OtherDrawCardData data) {
        super(id);
        this.otherDrawCardData = data;
    }

    /**
     * @return The data of the drawn card.
     */
    public OtherDrawCardData getOtherDrawCardData() {
        return otherDrawCardData;
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
