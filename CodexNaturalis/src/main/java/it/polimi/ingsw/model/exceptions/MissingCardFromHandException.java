package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player removes a card from their hand in the
 * {@link it.polimi.ingsw.model.Player.PlayArea}, but it's missing.
 */

public class MissingCardFromHandException extends Exception {
    /**
     * Constructs a new MissingCardFromHandException with a fixed detail message.
     */
    public MissingCardFromHandException() { super("Your hand doesn't contain such card");}
}
