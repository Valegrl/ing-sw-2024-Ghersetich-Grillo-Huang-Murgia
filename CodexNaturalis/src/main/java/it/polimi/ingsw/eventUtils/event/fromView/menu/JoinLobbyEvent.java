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
 * This class represents an event that initiates the joining of a lobby.
 * Each pair in the list of usernames consists of a username and a boolean value indicating whether the player is ready.
 */
public class JoinLobbyEvent extends FeedbackEvent<Pair<String, List<Pair<String, Boolean>>>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.JOIN_LOBBY.getID();

    /**
     * A pair of lobby ID and a list of usernames.
     * Each pair in the list of usernames consists of a username and a boolean value indicating whether the player is ready.
     */
    private final Pair<String, List<Pair<String, Boolean>>> info;

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
     * @param usernames The list of usernames in the lobby. Each pair in the list consists of a username
     *                  and a boolean value indicating whether the player is ready.
     * @param message The message associated with the event.
     */
    public JoinLobbyEvent(Feedback feedback, List<Pair<String, Boolean>> usernames, String message) {
        super(id, feedback, message);
        info = new Pair<>(null, new ArrayList<>(usernames));
    }

    /**
     * Retrieves the data associated with this event, which is a pair consisting of the lobby ID and a list of usernames.
     *
     * @return The pair consisting of the lobby ID sent to the Controller and the list of usernames
     * received from the Controller. Each pair in the list consists of a username and a boolean value
     * indicating whether the player is ready.
     */
    @Override
    public Pair<String, List<Pair<String, Boolean>>> getData() {
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
