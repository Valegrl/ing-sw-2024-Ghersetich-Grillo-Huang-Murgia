package it.polimi.ingsw.eventUtils.event.fromView;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
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
     * It initializes the private chat message with the provided value.
     *
     * @param privateChatMessage The private message to be sent.
     */
    public ChatPMEvent(PrivateChatMessage privateChatMessage) {
        super(id);
        this.privateChatMessage = privateChatMessage;
    }

    /**
     * Constructor for the server side (Controller).
     * It initializes the private chat message, feedback, and feedback message with the provided values.
     *
     * @param feedback The feedback about the operation.
     * @param message The feedback message to be sent.
     * @param privateChatMessage The private message to be sent.
     */
    public ChatPMEvent(Feedback feedback, String message, PrivateChatMessage privateChatMessage) {
        super(id, feedback, message);
        this.privateChatMessage = privateChatMessage;
    }

    /**
     * @return The {@link PrivateChatMessage} to be sent.
     */
    public PrivateChatMessage getPrivateChatMessage() {
        return privateChatMessage;
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
