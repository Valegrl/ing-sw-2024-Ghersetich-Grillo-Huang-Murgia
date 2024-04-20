package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;

/**
 * This class is responsible for managing the game logic.
 * It interacts with the model and view components to control the flow of the game.
 *
 * <p>Each instance of this class corresponds to a single game in the application.
 * The game is identified by a unique identifier, which is also used for the lobby from which the game was started.</p>
 *
 * <p>The class provides methods to delete a game and retrieve information about the game.</p>
 *
 * <p>The {@code GameController} is created with a {@code Lobby}, which is used to start the game and retrieve the game settings.
 * The {@code GameController} is then added to the list of active game controllers in the {@code Controller} singleton.</p>
 *
 * <p>This class is part of the {@code it.polimi.ingsw.controller} package, which contains the main classes for controlling
 * the game flow.</p>
 */
public class GameController {
    //TODO review class, implementation, listeners and synchronized methods
    /**
     * The Game instance that this controller manages.
     * It contains the state of the game, including the list of players, the game board, and the game settings.
     */
    private final Game game;

    /**
     * Constructs a new GameController with the given lobby.
     * It starts the game in the lobby, creates a new Game instance with the game settings from the lobby, and adds
     * this controller to the list of active game controllers in the Controller singleton.
     *
     * @param lobby The Lobby from which the game is started.
     */
    public GameController(Lobby lobby) {
        this.game = lobby.startGame();
        Controller.getInstance().addToGameControllers(this);
    }

    /**
     * Deletes the game.
     * This method deletes the Game instance that this controller manages and removes this GameController from the
     * list of active game controllers in the Controller singleton.
     */
    protected synchronized void deleteGame(){

    }

    /**
     * Retrieves the unique identifier of the game.
     * This method retrieves the unique identifier of the Game instance that this controller manages.
     *
     * @return The unique identifier of the game.
     */
    public synchronized String getIdentifier(){
        return game.getId();
    }

    //TODO Game logic and TurnManager
}
