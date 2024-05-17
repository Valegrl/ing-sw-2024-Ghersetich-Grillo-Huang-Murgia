package it.polimi.ingsw.utils;

import java.awt.*;
import java.util.LinkedList;

public class ChatMessagesList<E> extends LinkedList<E> {
    private final int maxMessages;

    public ChatMessagesList(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    @Override
    public boolean add(E message) {
        if (size() >= maxMessages) {
            removeFirst();
        }
        return super.add(message);
    }

    public String printMessages() {
        StringBuilder messages = new StringBuilder();
        for (E message : this) {
            messages.append(message).append("\n");
        }
        messages.append("\n");
        return messages.toString();
    }
}
