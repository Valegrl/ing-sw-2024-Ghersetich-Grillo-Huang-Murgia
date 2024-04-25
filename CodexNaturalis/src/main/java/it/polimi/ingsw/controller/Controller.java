package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Controller} class is a Singleton that serves as a central point of control for managing game controllers.
 * It maintains a list of {@code GameController} instances, each corresponding to a game that is not yet in the
 * {@code GameStatus.ENDED} state.
 *
 * <p>The class provides methods to create and join lobbies, reconnect to a game, and retrieve lists of available
 * lobbies and games.</p>
 *
 * <p>Use the {@link #getInstance()} method to get the single instance of this class.</p>
 */
public class Controller {
    //TODO review synchronized methods. Manage player username & password
    private final static Controller controller = new Controller();
    private final List<GameController> gameControllers;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the lists of game controllers.
     */
    private Controller() {
        gameControllers = new ArrayList<>();
    }

    public synchronized GameController createLobby(String playerName, String lobbyID, int nPlayersRequired){
        //TODO implementation
        /*check if lobbyID is unique*/
        return null;
    }

    public synchronized GameController joinLobby(String playerName, String lobbyID){
        //TODO implementation
        return null;
    }

    public synchronized GameController reconnectToGame(String playerName, String gameID){
        //TODO implementation
        return null;
    }

    public synchronized List<String> getLobbiesAvailable(){
        /*for on GameControllers*/
        return null;
    }

    public synchronized List<String> getGamesAvailable(){
        /*for on GameControllers*/
        return null;
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
     * Adds a GameController to the list of game controllers.
     *
     * @param gc The GameController to be added.
     */
    protected synchronized void addToGameControllers(GameController gc){
        gameControllers.add(gc);
    }

    /**
     * Removes a GameController from the list of game controllers.
     *
     * @param gc The GameController to be removed.
     */
    protected synchronized void removeFromGameControllers(GameController gc){
        gameControllers.remove(gc);
    }
}
