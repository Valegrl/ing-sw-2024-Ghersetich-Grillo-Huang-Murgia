package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.turnAction.draw.MyDrawCardData;

/**
 * The MyDrawCardEvent class represents an event that occurs when a card is drawn by the current player.
 * It includes the data of the drawn card.
 */
public class MyDrawCardEvent extends Event {

    /**
     * The id of the event.
     */
    private final static String id = EventID.MY_DRAW_CARD.getID();

    /**
     * The data of the drawn card.
     */
    private final MyDrawCardData myDrawCardData;

    /**
     * Constructs a new MyDrawCardEvent with the given drawn card data.
     *
     * @param data The data of the drawn card.
     */
    public MyDrawCardEvent(MyDrawCardData data) {
        super(id);
        this.myDrawCardData = data;
    }

    /**
     * @return The data of the drawn card.
     */
    public MyDrawCardData getMyDrawCardData() {
        return myDrawCardData;
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
