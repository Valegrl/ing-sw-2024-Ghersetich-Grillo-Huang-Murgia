package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that provides information about available lobbies.
 * Each lobby is represented as a pair. The key is the lobby name, and the value is another pair.
 * This inner pair represents the number of online players and the maximum number of players.
 */
public class AvailableLobbiesEvent extends FeedbackEvent<List<Pair<String, Pair<Integer, Integer>>>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.AVAILABLE_LOBBIES.getID();

    /**
     * A list of available lobbies. Each lobby is represented as a pair, where the key is the lobby name
     * and the value is another pair representing the number of online players and the maximum number of players.
     */
    private final List<Pair<String, Pair<Integer, Integer>>> lobbies;

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
     * @param lb The list of available lobbies. Each lobby is represented as a pair, where the key
     *           is the lobby name and the value is another pair representing the number of online players and the
     *           maximum number of players.
     * @param message The message associated with the event.
     */
    public AvailableLobbiesEvent(Feedback feedback, List<Pair<String, Pair<Integer, Integer>>> lb, String message) {
        super(id, feedback, message);
        lobbies = new ArrayList<>(lb);
    }

    /**
     * Retrieves the data associated with this event, which is a list of available lobbies.
     * Each lobby is represented as a pair, where the key is the lobby name and the value is another
     * pair representing the number of online players and the maximum number of players.
     *
     * @return A copy of the list of available lobbies.
     */
    @Override
    public List<Pair<String, Pair<Integer, Integer>>> getData() {
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
