package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to draw a visible card that is null.
 */
public class NullVisibleCardException extends RuntimeException {
    /**
     * Constructs a new NullVisibleCardException with a fixed detail message.
     */
    public NullVisibleCardException() {
        super("The visible card that Game is trying to draw is null");
    }

}
