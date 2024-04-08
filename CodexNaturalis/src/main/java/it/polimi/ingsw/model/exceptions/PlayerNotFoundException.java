package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when a Player with a given username is not present in Game's players list.
 */
public class PlayerNotFoundException extends RuntimeException {
    /**
     * Constructs a new PlayerNotFoundException with a fixed detail message.
     */
    public PlayerNotFoundException() {
        super("Player with the given username not found");
    }
}
