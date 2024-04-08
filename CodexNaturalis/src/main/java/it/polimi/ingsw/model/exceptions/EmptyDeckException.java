package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to draw a card from an empty deck.
 */
public class EmptyDeckException extends RuntimeException {
    /**
     * Constructs a new EmptyDeckException with a fixed detail message.
     */
    public EmptyDeckException() {
        super("Can't draw a card from an empty deck");
    }
}
