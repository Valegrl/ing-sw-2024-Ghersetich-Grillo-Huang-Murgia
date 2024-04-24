package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing the lobby logic.
 * It interacts with the model and view components to control the flow of the lobby.
 *
 * <p>Each instance of this class corresponds to a single lobby in the game.
 * The lobby is identified by a unique identifier, which is also used for the game once the lobby is ready to start.
 * The lobby is then deleted.</p>
 *
 * <p>The class provides methods to add and remove players from the lobby, start a game, and retrieve information about the lobby.
 * These operations are synchronized to ensure thread safety.</p>
 */
public class LobbyController{
    //TODO move LobbyController in Controller class and delete Lobby from model
    /**
     * The Lobby instance that this controller manages.
     * It contains the state of the lobby, including the list of players and the game settings.
     */
    private final Lobby lobby;

    /**
     * Constructs a new LobbyController with the given listener, player name, lobby ID, and number of required players.
     * It also adds this controller to the list of active lobby controllers in the Controller singleton.
     *
     * @param playerName The name of the player creating the lobby.
     * @param lobbyID The unique identifier for the lobby.
     * @param nPlayersRequired The number of players required to start the game.
     */
    protected LobbyController(String playerName, String lobbyID, int nPlayersRequired) {
        lobby = new Lobby(lobbyID, playerName, nPlayersRequired);
        Controller.getInstance().addToLobbyControllers(this);
    }

    /**
     * Adds a player to the lobby.
     *
     * @param playerName The name of the player to be added.
     */
    protected synchronized void addPlayer(String playerName){

    }

    /**
     * Removes a player from the lobby.
     *
     * @param playerName The name of the player to be removed.
     */
    protected synchronized void removePlayer(String playerName){

    }

    /**
     * Deletes the lobby.
     */
    protected synchronized void deleteLobby(){

    }

    /**
     * Starts a game within the lobby and creates a GameController for the game.
     * This method is not synchronized because it is only called from within other synchronized methods.
     */
    private void startGame(){
        Controller c = Controller.getInstance();
        synchronized (c){
            new GameController(lobby);
            c.removeFromLobbyControllers(this);
        }
    }

    /**
     * Retrieves the name of the player who created the lobby.
     *
     * @return The name of the player who created the lobby.
     */
    public synchronized String getHost(){
        return lobby.getJoinedPlayers().getFirst();
    }

    /**
     * Retrieves the list of players in the lobby.
     *
     * @return The list of players in the lobby.
     */
    public synchronized List<String> getPlayers(){
        return new ArrayList<>(lobby.getJoinedPlayers());
    }

    /**
     * Retrieves the unique identifier of the lobby.
     *
     * @return The unique identifier of the lobby.
     */
    public synchronized String getIdentifier(){
        return lobby.getId();
    }

}
