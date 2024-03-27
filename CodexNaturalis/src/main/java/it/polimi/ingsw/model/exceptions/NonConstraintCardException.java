package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the game checks the card for constraints but it's a resource card.
 */

public class NonConstraintCardException extends Exception {
    /**
     * Constructs a new NonConstraintCardException with a fixed detail message.
     */
    public NonConstraintCardException(){
        super("The played card has no constraint");
    }
}
