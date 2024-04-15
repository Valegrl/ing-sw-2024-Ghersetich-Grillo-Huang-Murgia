package it.polimi.ingsw.model;

/**
 * An enumeration representing the possible states of a game.
 */
public enum GameStatus {
    /**
     * The game is in the setup phase.
     */
    SETUP,

    /**
     * The game is in the players' turns phase.
     */
    RUNNING,

    /**
     * The game is waiting for a second player to reconnect, or for the end of a timeout.
     */
    WAITING,

    /**
     * The game is in the last turn phase, on which player will not draw cards.
     */
    LAST_CIRCLE,

    /**
     * The game is ended.
     */
    ENDED
}
