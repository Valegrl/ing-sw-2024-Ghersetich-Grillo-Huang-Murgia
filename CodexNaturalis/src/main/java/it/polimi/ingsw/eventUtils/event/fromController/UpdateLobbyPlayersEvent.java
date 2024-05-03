package it.polimi.ingsw.eventUtils.event.fromController;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an event that updates the list of players in the lobby.
 */
public class UpdateLobbyPlayersEvent extends Event {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.UPDATE_LOBBY_PLAYERS.getID();

    /**
     * The list of players in the lobby.
     */
    private final List<String> players;

    /**
     * Constructs a new UpdateLobbyPlayersEvent with the unique identifier and the list of players.
     *
     * @param pl The list of players in the lobby.
     */
    public UpdateLobbyPlayersEvent(List<String> pl) {
        super(id);
        this.players = new ArrayList<>(pl);
    }

    /**
     * @return A copy of the list of players in the lobby.
     */
    public List<String> getPlayers() {
        return new ArrayList<>(players);
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
