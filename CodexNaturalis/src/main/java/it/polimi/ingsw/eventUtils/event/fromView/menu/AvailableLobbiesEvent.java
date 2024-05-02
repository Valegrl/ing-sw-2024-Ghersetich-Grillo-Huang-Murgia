package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that provides information about available lobbies.
 * This event is a type of FeedbackEvent, which carries a list of lobby names as data.
 */
public class AvailableLobbiesEvent extends FeedbackEvent<List<String>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.AVAILABLE_LOBBIES.getID();

    /**
     * A list of available lobbies.
     */
    private final List<String> lobbies;

    /**
     * Constructor for View (client). Initializes the list of lobbies to an empty list.
     */
    public AvailableLobbiesEvent() {
        super(id);
        lobbies = new ArrayList<>();
    }

    /**
     * Constructor for Controller (server). Initializes the list of lobbies with the provided list.
     *
     * @param feedback The feedback associated with the event.
     * @param lb The list of available lobbies.
     * @param message The message associated with the event.
     */
    public AvailableLobbiesEvent(Feedback feedback, List<String> lb, String message) {
        super(id, feedback, message);
        lobbies = new ArrayList<>(lb);
    }

    @Override
    public List<String> getData() {
        return new ArrayList<>(lobbies);
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
