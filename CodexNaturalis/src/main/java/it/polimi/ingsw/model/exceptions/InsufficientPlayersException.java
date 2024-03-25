package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when attempting to start a game with insufficient players.
 * This typically occurs when the game requires a minimum number of players to start,
 * but there are not enough players present.
 *
 * The minimum number of players required to start the game is specified by the {@link it.polimi.ingsw.model.Lobby} class.
 */
public class InsufficientPlayersException extends RuntimeException {
    /**
     * Constructs a new InsufficientPlayersException with a fixed detail message.
     */
    public InsufficientPlayersException() {
        super("Not enough players to start the game");
    }
}
