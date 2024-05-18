package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.view.controller.ViewEventReceiver;

/**
 * Represents a global chat message event in the game.
 * This event is used to send a global chat message from a player to all other players in the game.
 */
public class ChatGMEvent extends FeedbackEvent {

    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.CHAT_GM.getID();

    /**
     * The message to be sent.
     */
    private final ChatMessage chatMessage;

    /**
     * Constructor for the client side (View).
     * It initializes the chat message with the provided value.
     *
     * @param chatMessage The message to be sent.
     */
    public ChatGMEvent(ChatMessage chatMessage) {
        super(id);
        this.chatMessage = chatMessage;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the chat message, feedback, and feedback message with the provided values.
     *
     * @param feedback The feedback about the operation.
     * @param message The feedback message to be sent.
     * @param chatMessage The chat message to be sent.
     */
    public ChatGMEvent(Feedback feedback, String message, ChatMessage chatMessage) {
        super(id, feedback, message);
        this.chatMessage = chatMessage;
    }

    /**
     * @return The {@link ChatMessage} to be sent.
     */
    public ChatMessage getChatMessage() {
        return chatMessage;
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
