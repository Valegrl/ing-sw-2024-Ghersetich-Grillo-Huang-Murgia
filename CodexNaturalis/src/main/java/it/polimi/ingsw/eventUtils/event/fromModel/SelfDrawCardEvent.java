package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.turnAction.draw.SelfDrawCardData;

/**
 * The SelfDrawCardEvent class represents an event that occurs when a card is drawn by the current player.
 */
public class SelfDrawCardEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.SELF_DRAW_CARD.getID();

    /**
     * The message associated with the event.
     */
    private final String message;

    /**
     * The data of the drawn card.
     */
    private final SelfDrawCardData selfDrawCardData;

    /**
     * Constructs a new SelfDrawCardEvent with the given drawn card data and a message.
     *
     * @param data The data of the drawn card.
     * @param message The message associated with the event.
     */
    public SelfDrawCardEvent(SelfDrawCardData data, String message) {
        super(id);
        this.selfDrawCardData = data;
        this.message = message;
    }

    /**
     * @return The message associated with the event.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The data of the drawn card.
     */
    public SelfDrawCardData getMyDrawCardData() {
        return selfDrawCardData;
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
