package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to place a gold card without satisfying the constraint.
 */
public class InvalidConstraintException extends Exception {
    /**
     * Constructs a new InvalidConstraintException with the given detail message.
     *
     * @param message The detailed message for the exception;
     */
    public InvalidConstraintException(String message) {
        super(message);
    }
}
