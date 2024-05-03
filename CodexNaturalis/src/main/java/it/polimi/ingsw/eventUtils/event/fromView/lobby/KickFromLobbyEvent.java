package it.polimi.ingsw.eventUtils.event.fromView.lobby;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.FeedbackEvent;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * This class represents an event that is triggered when a player wants to kick another player.
 */
public class KickFromLobbyEvent extends FeedbackEvent {

    /**
     * The unique identifier for a KickFromLobbyEvent.
     */
    private final static String id = EventID.KICK_FROM_LOBBY.getID();

    /**
     * The player to be kicked from the lobby.
     */
    private final String playerToKick;

    /**
     * Constructor for the client side (View).
     * It initializes the superclass with the unique identifier for this event type and sets the player to be kicked.
     *
     * @param p The player to be kicked.
     */
    public KickFromLobbyEvent(String p) {
        super(id);
        this.playerToKick = p;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the superclass with the unique identifier for this event type, feedback, and a message.
     * The player to be kicked is set to null.
     *
     * @param feedback The feedback for the event.
     * @param message The message for the event.
     */
    public KickFromLobbyEvent(Feedback feedback, String message) {
        super(id, feedback, message);
        this.playerToKick = null;
    }

    /**
     * @return The player to be kicked.
     */
    public String getPlayerToKick() {
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
