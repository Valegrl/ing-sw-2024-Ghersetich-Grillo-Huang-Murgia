package it.polimi.ingsw.controller;

import it.polimi.ingsw.listener.GameListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Controller} class is a Singleton that serves as a central point of control for managing game and lobby controllers.
 * It maintains a list of all active {@code GameController} and {@code LobbyController} instances.
 *
 * <p>The class provides methods to create and join lobbies, reconnect to a game, and retrieve lists of available lobbies and games.
 * These operations are synchronized to ensure thread safety.</p>
 *
 * <p>Use the {@link #getInstance()} method to get the single instance of this class.</p>
 *
 * <p>Each {@code LobbyController} and {@code GameController} is identified by a unique identifier, which can be
 * retrieved using their {@code getIdentifier()} methods. These identifiers are used to manage and track the controllers.
 * The identifier is unique across both games and lobbies.
 * Once a lobby is ready to start a game, the same identifier is used for the game, and the lobby is deleted.</p>
 *
 * <p>This class is part of the {@code it.polimi.ingsw.controller} package, which contains the main classes for
 * controlling the game flow.</p>
 */
public class Controller {
    //TODO review synchronized methods
    private final static Controller controller = new Controller();
    private final List<GameController> gameControllers;
    private final List<LobbyController> lobbyControllers;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the lists of game and lobby controllers.
     */
    private Controller() {
        gameControllers = new ArrayList<>();
        lobbyControllers = new ArrayList<>();
    }

    /**
     * Creates a new lobby and returns the corresponding {@code LobbyController}.
     *
     * <p>The lobby is identified by a unique identifier, which is also used for the game once the lobby is ready to start.
     * The lobby is then deleted.</p>
     *
     * @param listener The {@code GameListener} to be associated with the lobby.
     * @param playerName The name of the player creating the lobby.
     * @param lobbyID The unique identifier for the lobby.
     * @param nPlayersRequired The number of players required to start the game.
     * @return The {@code LobbyController} for the newly created lobby.
     */
    public synchronized LobbyController createLobby(GameListener listener, String playerName, String lobbyID, int nPlayersRequired){
        //TODO implementation
        /*check if lobbyID is unique*/
    }

    /**
     * Allows a player to join an existing lobby and returns the corresponding {@code LobbyController}.
     *
     *<p>The lobby is identified by a unique identifier, which is also used for the game once the lobby is ready to start.
     * The lobby is then deleted.</p>
     *
     * @param listener The {@code GameListener} to be associated with the lobby.
     * @param playerName The name of the player joining the lobby.
     * @param lobbyID The unique identifier for the lobby.
     * @return The {@code LobbyController} for the lobby the player joined.
     */
    public synchronized LobbyController joinLobby(GameListener listener, String playerName, String lobbyID){
        //TODO implementation
    }

    /**
     * Allows a player to reconnect to an existing game and returns the corresponding {@code GameController}.
     * Only players who were present when the game started can reconnect.
     *
     * @param listener The {@code GameListener} to be associated with the game.
     * @param playerName The name of the player reconnecting to the game.
     * @param gameID The unique identifier for the game.
     * @return The {@code GameController} for the game the player reconnected to.
     */
    public synchronized GameController reconnectToGame(GameListener listener, String playerName, String gameID){
        //TODO implementation
    }

    /**
     * Retrieves a list of identifiers for all available lobbies.
     *
     * @return A list of identifiers for all available lobbies.
     */
    public synchronized List<String> getLobbiesAvailable(){
        List<String> lobbies = new ArrayList<>();

        for(LobbyController lobby : lobbyControllers)
            lobbies.add(lobby.getIdentifier());

        return lobbies;
    }

    /**
     * Retrieves a list of identifiers for all available games.
     *
     * @return A list of identifiers for all available games.
     */
    public synchronized List<String> getGamesAvailable(){
        List<String> games = new ArrayList<>();

        for(GameController game : gameControllers)
            games.add(game.getIdentifier());

        return games;
    }

    /**
     * Returns the single instance of the Controller class.
     *
     * @return the instance of the Controller class.
     */
    public static Controller getInstance(){
        return controller;
    }

    /**
     * Adds a LobbyController to the list of active lobby controllers.
     *
     * @param lc The LobbyController to be added.
     */
    protected synchronized void addToLobbyControllers(LobbyController lc){
        lobbyControllers.add(lc);
    }

    /**
     * Removes a LobbyController from the list of active lobby controllers.
     *
     * @param lc The LobbyController to be removed.
     */
    protected synchronized void removeFromLobbyControllers(LobbyController lc){
        lobbyControllers.remove(lc);
    }

    /**
     * Adds a GameController to the list of active game controllers.
     *
     * @param gc The GameController to be added.
     */
    protected synchronized void addToGameControllers(GameController gc){
        gameControllers.add(gc);
    }

    /**
     * Removes a GameController from the list of active game controllers.
     *
     * @param gc The GameController to be removed.
     */
    protected synchronized void removeFromGameControllers(GameController gc){
        gameControllers.remove(gc);
    }
}
