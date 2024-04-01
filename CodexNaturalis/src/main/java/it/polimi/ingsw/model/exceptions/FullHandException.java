package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when a player's hand is full, but he's still trying to draw another card.
 */
public class FullHandException extends RuntimeException {

    /**
     * Constructs a new FullHandException with a fixed detail message;
     */
    public FullHandException() {
        super("You can't add more cards to your hand.");
    }
}
