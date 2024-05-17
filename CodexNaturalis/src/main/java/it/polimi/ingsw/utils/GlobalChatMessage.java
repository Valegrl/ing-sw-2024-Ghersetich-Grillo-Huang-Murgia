package it.polimi.ingsw.utils;

public class GlobalChatMessage {
    private final String sender;
    private final String message;

    public GlobalChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + sender + "]: " + message;
    }
}
