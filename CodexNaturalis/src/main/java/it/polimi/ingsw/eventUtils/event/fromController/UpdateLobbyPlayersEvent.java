package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an event that updates the list of players in the lobby.
 * Each pair in the list of players consists of a username and a boolean value indicating whether the player is ready.
 */
public class UpdateLobbyPlayersEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.UPDATE_LOBBY_PLAYERS.getID();

    /**
     * The list of players in the lobby.
     * Each pair in the list consists of a username and a boolean value indicating whether the player is ready.
     */
    private final List<Pair<String, Boolean>> players;

    /**
     * The message associated with the event.
     */
    private final String message;

    /**
     * Constructs a new UpdateLobbyPlayersEvent with the unique identifier and the list of players.
     *
     * @param pl The list of players in the lobby. Each pair in the list consists of a username
     *           and a boolean value indicating whether the player is ready.
     */
    public UpdateLobbyPlayersEvent(List<Pair<String, Boolean>> pl, String m) {
        super(id);
        this.players = new ArrayList<>(pl);
        this.message = m;
    }

    /**
     * @return A copy of the list of players in the lobby. Each pair in the list consists of a username
     *         and a boolean value indicating whether the player is ready.
     */
    public List<Pair<String, Boolean>> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * @return The message associated with the event.
     */
    public String getMessage() {
        return message;
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
