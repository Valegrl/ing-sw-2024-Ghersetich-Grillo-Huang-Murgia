package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents a global chat message event in the game.
 * This event is used to send a global chat message from a player to all other players in the game.
 */
public class ChatGMEvent extends FeedbackEvent{

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHAT_GM.getID();

    /**
     * The username of the player who sent the message.
     */
    private final String sender;

    /**
     * Constructor for the client side (View).
     * The sender is set to null.
     *
     * @param message The message to be sent.
     */
    public ChatGMEvent(String message) {
        super(id);
        sender = null;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback The feedback about the operation.
     * @param sender The username of the player who sent the message.
     * @param message The message to be sent.
     */
    public ChatGMEvent(Feedback feedback, String sender, String message) {
        super(id, feedback, message);
        this.sender = sender;
    }

    /**
     * @return The username of the sender.
     */
    public String getSender() {
        return sender;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {

    }

    @Override
    public void receiveEvent(VirtualView virtualView) {

    }
}
