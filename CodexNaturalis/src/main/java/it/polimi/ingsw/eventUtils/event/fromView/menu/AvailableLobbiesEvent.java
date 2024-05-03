package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an event that is triggered when the available lobbies are requested.
 */
public class AvailableLobbiesEvent extends FeedbackEvent {

    /**
     * The unique identifier for an AvailableLobbiesEvent.
     */
    private final static String id = EventID.AVAILABLE_LOBBIES.getID();

    /**
     * The list of available lobbies.
     */
    private final List<LobbyState> lobbies;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and initializes the lobbies list.
     */
    public AvailableLobbiesEvent() {
        super(id);
        lobbies = new ArrayList<>();
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * It also initializes the lobbies list with the provided list.
     *
     * @param feedback The feedback for the event.
     * @param lb The list of lobbies.
     * @param message The message for the event.
     */
    public AvailableLobbiesEvent(Feedback feedback, List<LobbyState> lb, String message) {
        super(id, feedback, message);
        lobbies = new ArrayList<>(lb);
    }

    /**
     * @return A new list containing the lobbies.
     */
    public List<LobbyState> getLobbies() {
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
