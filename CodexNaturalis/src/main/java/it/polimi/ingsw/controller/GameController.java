package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing the game logic.
 * It interacts with the model and the VirtualView to control the flow of the game.
 *
 * <p>Each instance of this class corresponds to a single game in the application.
 * The game is identified by a unique identifier, which is also used as identifier for the {@code GameController}</p>
 *
 * <p>The class provides methods to delete a game and retrieve information about the game.</p>
 *
 * <p>The {@code GameController} is created with the {@code Controller}, which is used to start the game and retrieve
 * the game settings.
 * The {@code GameController} is then added to the list of active game controllers in the {@code Controller} singleton.</p>
 */
public class GameController {
    //TODO review class, implementation and synchronized methods
    /**
     * The Game identifier.
     */
    private final String id;

    /**
     * The Game instance that this controller manages.
     * It contains the state of the game, including the list of players, the game board, and the game settings.
     */
    private final Game game;

    /**
     * A flag indicating whether the game has started.
     * This is set to true when the game starts, and remains true for the duration of the game.
     */
    private final boolean gameStarted;

    /**
     * The list of Players' usernames that joined the Lobby.
     */
    private final List<String> joinedPlayers;

    /**
     * The number of players required for this game to start.
     */
    private final int requiredPlayers;

    /**
     * Constructs a new GameController with the given lobby.
     * It starts the game in the lobby, creates a new Game instance with the game settings from the lobby, and adds
     * this controller to the list of game controllers in the Controller singleton.
     *
     * @param playerName The name of the player creating the lobby/game.
     * @param gameID The unique identifier for the Game.
     * @param nPlayersRequired The number of players required to start the game.
     */
    public GameController(String playerName, String gameID, int nPlayersRequired) {
        this.game = null;
        this.gameStarted = false;
        this.joinedPlayers = new ArrayList<>();
        this.joinedPlayers.add(playerName);
        this.id = gameID;
        this.requiredPlayers = nPlayersRequired;
        Controller.getInstance().addToGameControllers(this);
    }

    protected synchronized void addPlayer(String playerName){

    }

    protected synchronized void removePlayer(String playerName){

    }

    private void startGame(){

    }

    /**
     * Deletes the game.
     * This method deletes the Game instance that this controller manages and removes this GameController from the
     * list of game controllers in the Controller singleton.
     */
    protected synchronized void deleteGame(){

    }

    public synchronized String getHost(){
        return null;
    }

    public synchronized List<String> getPlayers(){
        return null;
    }

    public synchronized boolean isGameStarted(){return gameStarted;}

    /**
     * Retrieves the unique identifier of the game.
     * This method retrieves the unique identifier of the Game instance that this controller manages.
     *
     * @return The unique identifier of the game.
     */
    public synchronized String getIdentifier(){
        return id;
    }

    //TODO Game logic
}
