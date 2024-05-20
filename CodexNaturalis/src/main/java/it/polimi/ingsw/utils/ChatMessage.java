package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.UUID;

/**
 * This class represents a global chat message in the application.
 * Each ChatMessage has a sender, a message content, a timestamp, and a unique message ID.
 */
public class ChatMessage implements Serializable, Comparable<ChatMessage> {

    /**
     * The timestamp of when the message was created.
     */
    private final LocalTime time;

    /**
     * The unique identifier for the message.
     */
    private final UUID message_id;

    /**
     * The sender of the message.
     */
    private String sender;

    /**
     * The content of the message.
     */
    private final String message;

    /**
     * Constructor for the ChatMessage class.
     * It initializes the message with the provided value, sets the time to the current time,
     * and generates a unique identifier for the message.
     *
     * @param message The content of the message.
     */
    public ChatMessage(String message) {
        this.time = java.time.LocalTime.now();
        this.message_id = UUID.randomUUID();
        this.message = message;
    }

    /**
     * Sets the sender of the message.
     *
     * @param sender The sender of the message.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return The timestamp of when the message was created.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * @return The unique identifier for the message.
     */
    public UUID getMessage_id() {
        return message_id;
    }

    /**
     * @return The sender of the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return The content of the message.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("HH:mm")) + " [" + sender + "]: " + message;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return this.time.compareTo(o.time);
    }
}
