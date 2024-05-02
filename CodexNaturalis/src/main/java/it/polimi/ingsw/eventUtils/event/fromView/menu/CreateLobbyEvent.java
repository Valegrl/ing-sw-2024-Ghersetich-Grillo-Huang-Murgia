package it.polimi.ingsw.eventUtils.event.fromView.menu;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents an event that initiates the creation of a new lobby.
 * This event is a type of FeedbackEvent, which carries a pair of lobby ID and number of players as data.
 */
public class CreateLobbyEvent extends FeedbackEvent<Pair<String, Integer>> {

    /**
     * The identifier for this type of event.
     */
    private final static String id = EventID.CREATE_LOBBY.getID();

    /**
     * A pair of lobby ID and number of players.
     */
    private final Pair<String, Integer> setting;

    /**
     * Constructor for View (client). Initializes the setting with the provided lobby ID and number of players.
     *
     * @param lobbyID The ID of the lobby to be created.
     * @param nPlayers The number of players in the lobby.
     */
    public CreateLobbyEvent(String lobbyID, Integer nPlayers) {
        super(id);
        setting = new Pair<>(lobbyID, nPlayers);
    }

    /**
     * Constructor for Controller (server). Initializes the setting with null values.
     *
     * @param feedback The feedback associated with the event.
     * @param message The message associated with the event.
     */
    public CreateLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        setting = new Pair<>(null, null);
    }

    @Override
    public Pair<String, Integer> getData() {
        return setting;
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
