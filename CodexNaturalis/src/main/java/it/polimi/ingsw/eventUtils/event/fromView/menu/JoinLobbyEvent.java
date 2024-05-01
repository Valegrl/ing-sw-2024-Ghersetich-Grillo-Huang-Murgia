package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.List;

/**
 * Represents an event that initiates the joining of a lobby.
 * This event is a type of FeedbackEvent, which carries a pair of lobby ID and a list of usernames as data.
 */
public class JoinLobbyEvent extends FeedbackEvent<Pair<String, List<String>>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = "JOIN_LOBBY";

    /**
     * A pair of lobby ID and a list of usernames.
     */
    private final Pair<String, List<String>> info;

    /**
     * Constructor for View (client). Initializes the info with the provided lobby ID and null for the list of usernames.
     *
     * @param lobbyID The ID of the lobby to be joined.
     */
    public JoinLobbyEvent(String lobbyID) {
        super(id);
        info = new Pair<>(lobbyID, null);
    }

    /**
     * Constructor for Controller (server). Initializes the info with null for the lobby ID and the provided list of usernames.
     *
     * @param feedback The feedback associated with the event.
     * @param usernames The list of usernames in the lobby.
     * @param message The message associated with the event.
     */
    public JoinLobbyEvent(Feedback feedback, List<String> usernames, String message) {
        super(id, feedback, message);
        info = new Pair<>(null, usernames);
    }

    @Override
    public Pair<String, List<String>> getData() {
        return info;
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
