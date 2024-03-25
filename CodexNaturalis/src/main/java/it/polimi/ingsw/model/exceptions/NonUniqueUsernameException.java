package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when a player attempts to choose a username that is not unique in the selected lobby.
 * This typically occurs when a player tries to join a lobby with a username that is already being used by another player.
 */
public class NonUniqueUsernameException extends Exception {

    /**
     * Constructs a new NonUniqueUsernameException with a fixed detail message.
     */
    public NonUniqueUsernameException() {
        super("The chosen username is not unique in this lobby");
    }
}
