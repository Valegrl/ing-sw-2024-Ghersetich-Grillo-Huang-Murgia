package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player tries to remove a card from their hand in the
 * {@link it.polimi.ingsw.model.Player.PlayArea}, but it's missing.
 */

public class MissingCardFromHandException extends RuntimeException {
    /**
     * Constructs a new MissingCardFromHandException with a fixed detail message.
     */
    public MissingCardFromHandException() { super("Your hand doesn't contain such card");}
}
