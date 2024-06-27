package it.polimi.ingsw.utils;

import java.util.LinkedList;

/**
 * This class represents a list of chat messages with a maximum size.
 * It extends LinkedList and overrides the add method to ensure the list size does not exceed the maximum.
 * If the list size is at the maximum and a new message is added, the oldest message (the first one) is removed.
 * The toString method is also overridden to provide a string representation of the list, with each message on a new line.
 *
 * @param <E> the type of elements in this list
 */
public class ChatMessagesList<E> extends LinkedList<E> {

    /**
     * The maximum number of messages this list can hold.
     */
    private final int maxMessages;

    /**
     * Constructs a new ChatMessagesList with the specified maximum number of messages.
     *
     * @param maxMessages the maximum number of messages this list can hold
     */
    public ChatMessagesList(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    /**
     * Adds a message to the list. If the list size is at the maximum, the oldest message is removed before the new one is added.
     *
     * @param message the message to add
     * @return true (as specified by Collection.add(E))
     */
    @Override
    public boolean add(E message) {
        if (size() >= maxMessages) {
            removeFirst();
        }
        return super.add(message);
    }

    /**
     * Returns a string representation of the list, with each message on a new line.
     *
     * @return a string representation of the list
     */
    @Override
    public String toString() {
        StringBuilder messages = new StringBuilder();
        this.stream().sorted().forEach(message -> messages.append(message).append("\n"));
        messages.append("\n");
        return messages.toString();
    }
}
