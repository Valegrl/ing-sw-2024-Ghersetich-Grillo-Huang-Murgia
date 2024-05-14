package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents a private chat message event in the game.
 * This event is used to send a private chat message from one player to another in the game.
 */
public class ChatPMEvent extends FeedbackEvent{

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHAT_PM.getID();

    /**
     * The username of the player who sent the message.
     */
    private final String sender;

    /**
     * The username of the player who will receive the message.
     */
    private final String recipient;

    /**
     * Constructor for the client side (View).
     * The sender is set to null.
     *
     * @param recipient The username of the player who will receive the message.
     * @param message The message to be sent.
     */
    public ChatPMEvent(String recipient, String message) {
        super(id);
        this.sender = null;
        this.recipient = recipient;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback The feedback about the operation.
     * @param sender The username of the player who sent the message.
     * @param recipient The username of the player who will receive the message.
     * @param message The message to be sent.
     */
    public ChatPMEvent(Feedback feedback, String sender, String recipient, String message) {
        super(id, feedback, message);
        this.sender = sender;
        this.recipient = recipient;
    }

    /**
     * @return The username of the sender.
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return The username of the recipient.
     */
    public String getRecipient() {
        return recipient;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {

    }

    @Override
    public void receiveEvent(VirtualView virtualView) {

    }
}
