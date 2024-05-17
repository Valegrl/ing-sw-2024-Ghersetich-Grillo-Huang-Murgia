package it.polimi.ingsw.utils;

public class PrivateChatMessage {
    private final String sender;
    private final String recipient;
    private final String message;

    public PrivateChatMessage(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "\u001B[1mPM from [" + sender + "]\u001B[0m: " + message;
    }
}
