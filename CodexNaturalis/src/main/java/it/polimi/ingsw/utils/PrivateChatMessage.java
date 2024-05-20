package it.polimi.ingsw.utils;

import java.time.format.DateTimeFormatter;

/**
 * This class represents a private chat message in the application.
 * It extends the ChatMessage class and adds a recipient field to represent
 * the intended recipient of the message.
 */
public class PrivateChatMessage extends ChatMessage {

    /**
     * The recipient of the message.
     */
    private final String recipient;

    /**
     * Constructor for the PrivateChatMessage class.
     * It initializes the recipient and message with the provided values.
     *
     * @param recipient The recipient of the message.
     * @param message The content of the message.
     */
    public PrivateChatMessage(String recipient, String message) {
        super(message);
        this.recipient = recipient;
    }

    /**
     * @return The recipient of the message.
     */
    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " \u001B[1mPM from [" + getSender() + "]\u001B[0m: " + getMessage();
    }
}
