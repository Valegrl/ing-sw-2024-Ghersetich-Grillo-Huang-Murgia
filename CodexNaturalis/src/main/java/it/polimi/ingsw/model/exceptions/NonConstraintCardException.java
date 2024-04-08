package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the game looks for the card's constraint,
 * but it's a {@link it.polimi.ingsw.model.card.ResourceCard}.
 */

public class NonConstraintCardException extends Exception {
    /**
     * Constructs a new NonConstraintCardException with a fixed detail message.
     */
    public NonConstraintCardException(){
        super("The played card has no constraint");
    }
}
