package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the card played has no constraint but it's supposed to.
 */

public class NonConstraintCardException extends Exception {
    /**
     * Constructs a new NonConstraintCardException with a fixed detail message.
     */
    public NonConstraintCardException(){
        super("The played card has no constraint");
    }
}
