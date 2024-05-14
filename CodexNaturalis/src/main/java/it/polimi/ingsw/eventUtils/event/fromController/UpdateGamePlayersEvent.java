package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an event that updates the list of players in the game.
 */
public class UpdateGamePlayersEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.UPDATE_GAME_PLAYERS.getID();

    /**
     * The map of online players in the game.
     */
    private final Map<String, Boolean> players;

    /**
     * The message associated with the event.
     */
    private final String message;

    /**
     * Constructs a new UpdateLobbyPlayersEvent with the unique identifier and the list of players.
     *
     * @param pl The list of players in the lobby.
     */
    public UpdateGamePlayersEvent(Map<String, Boolean> pl, String m) {
        super(id);
        this.players = new HashMap<>(pl);
        this.message = m;
    }

    /**
     * @return A copy of the map of online players in the game.
     */
    public Map<String, Boolean> getPlayers() {
        return new HashMap<>(players);
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
