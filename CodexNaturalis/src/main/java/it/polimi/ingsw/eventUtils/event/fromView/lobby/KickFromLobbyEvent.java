package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player wants to kick another player from the lobby.
 */
public class KickFromLobbyEvent extends FeedbackEvent<String> {

    /**
     * The unique identifier for a KickFromLobbyEvent.
     */
    private final static String id = EventID.KICK_FROM_LOBBY.getID();

    /**
     * The name of the player to be kicked.
     */
    private final String playerToKick;

    /**
     * Constructor for View (client). New KickFromLobbyEvent with the player's name.
     *
     * @param p The name of the player to be kicked.
     */
    public KickFromLobbyEvent(String p) {
        super(id);
        this.playerToKick = p;
    }

    /**
     * Constructor for Controller (server). New KickFromLobbyEvent with feedback and a message.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public KickFromLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.playerToKick = null;
    }

    /**
     * Method for getting the data of the event.
     *
     * @return The name of the player to be kicked.
     */
    @Override
    public String getData() {
        return playerToKick;
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
