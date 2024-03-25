package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to join a lobby that is already at its maximum capacity.
 * This typically occurs when a player tries to join a lobby, but the lobby is already full.
 */
public class FullLobbyException extends Exception {

    /**
     * Constructs a new FullLobbyException with a fixed detail message;
     */
    public FullLobbyException() {
        super("The lobby you are trying to join is already full.");
    }
}
