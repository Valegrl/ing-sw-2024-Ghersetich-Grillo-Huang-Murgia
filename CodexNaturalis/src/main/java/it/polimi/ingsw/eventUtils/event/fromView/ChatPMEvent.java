package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.utils.GlobalChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
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
     * The private message to be sent.
     */
    private final PrivateChatMessage privateChatMessage;

    /**
     * Constructor for the client side (View).
     * The sender is set to null.
     *
     * @param privateChatMessage The message to be sent.
     */
    public ChatPMEvent(PrivateChatMessage privateChatMessage) {
        super(id);
        this.privateChatMessage = privateChatMessage;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback The feedback about the operation.
     * @param message The feedback message to be sent.
     * @param chatPMEvent The Event containing the message to be sent.
     */
    public ChatPMEvent(Feedback feedback, String message, ChatPMEvent chatPMEvent) {
        super(id, feedback, message);
        this.privateChatMessage = chatPMEvent.getPrivateChatMessage();
    }

    /**
     * @return The username of the sender.
     */
    public String getSender() {
        return privateChatMessage.getSender();
    }

    /**
     * @return The username of the recipient.
     */
    public String getRecipient() {
        return privateChatMessage.getRecipient();
    }

    /**
     * @return The message to be sent.
     */
    public String getText() {
        return privateChatMessage.getMessage();
    }

    /**
     * @return The {@link PrivateChatMessage} to be sent.
     */
    public PrivateChatMessage getPrivateChatMessage() {
        return privateChatMessage;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {

    }

    @Override
    public void receiveEvent(VirtualView virtualView) {

    }
}
