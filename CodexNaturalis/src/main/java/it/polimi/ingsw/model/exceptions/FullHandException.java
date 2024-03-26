package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when a player's hand is full and it's trying to draw another card.
 */
public class FullHandException extends Exception {

    /**
     * Constructs a new FullHandException with a fixed detail message;
     */
    public FullHandException() {
        super("You can't add more cards to your hand.");
    }
}
