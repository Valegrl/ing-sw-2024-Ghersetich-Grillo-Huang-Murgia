package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.utils.GlobalChatMessage;
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
    private final GlobalChatMessage globalChatMessage;

    /**
     * Constructor for the client side (View).
     * The sender is set to null.
     *
     * @param globalChatMessage The message to be sent.
     */
    public ChatGMEvent(GlobalChatMessage globalChatMessage) {
        super(id);
        this.globalChatMessage = globalChatMessage;
    }

    /**
     * Constructor for the server side (Controller).
     *
     * @param feedback The feedback about the operation.
     * @param message The feedback message to be sent.
     * @param chatGMEvent The Event containing the chat message to be sent.
     */
    public ChatGMEvent(Feedback feedback, String message, ChatGMEvent chatGMEvent) {
        super(id, feedback, message);
        this.globalChatMessage = chatGMEvent.getGlobalChatMessage();
    }

    /**
     * @return The username of the sender.
     */
    public String getSender() {
        return globalChatMessage.getSender();
    }

    /**
     * @return The message to be sent.
     */
    public String getText() {
        return globalChatMessage.getMessage();
    }

    /**
     * @return The {@link GlobalChatMessage} to be sent.
     */
    public GlobalChatMessage getGlobalChatMessage() {
        return globalChatMessage;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {

    }

    @Override
    public void receiveEvent(VirtualView virtualView) {

    }
}
