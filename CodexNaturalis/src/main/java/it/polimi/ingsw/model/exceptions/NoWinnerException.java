package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to calculate the Game's winner player, and it can't be found.
 */
public class NoWinnerException extends RuntimeException {
    /**
     * Constructs a new NoWinnerException with the given detail message.
     *
     * @param message The detailed message for the exception;
     */
    public NoWinnerException(String message) {
        super(message);
    }
}
