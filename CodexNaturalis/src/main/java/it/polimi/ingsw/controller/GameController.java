package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * A map that stores the players who have joined the lobby or were present at the start of the game.
     * The key is a pair of strings representing the account (username and password), and the value is
     * the corresponding GameListener.
     */
    private final Map<Pair<String, String>, GameListener> joinedPlayers;

    /**
     * A map that stores the players who are currently online in the lobby or game.
     * The key is the VirtualView associated with the player, and the value is a pair of strings
     * representing the account (username and password).
     */
    private final Map<VirtualView, Pair<String, String>> virtualViewAccounts;

    /**
     * The number of players required for this game to start.
     */
    private final int requiredPlayers;

    private final String host;

    /**
     * Constructs a new GameController with the given lobby.
     * It starts the game in the lobby, creates a new Game instance with the game settings from the lobby, and adds
     * this controller to the list of game controllers in the Controller singleton.
     *
     * @param account The name of the player creating the lobby/game.
     * @param gameID The unique identifier for the Game.
     * @param nRequiredPlayers The number of players required to start the game.
     */
    protected GameController(VirtualView vv, Pair<String, String> account, GameListener gl, String gameID, int nRequiredPlayers) {
        this.game = null;
        this.gameStarted = false;
        this.joinedPlayers = new HashMap<>();
        this.joinedPlayers.put(account, gl);
        this.virtualViewAccounts = new HashMap<>();
        this.virtualViewAccounts.put(vv, account);
        this.host = account.key();
        this.id = gameID;
        this.requiredPlayers = nRequiredPlayers;
        Controller.getInstance().addToGameControllers(this);
    }

    protected synchronized void addPlayer(VirtualView vv, Pair<String, String> account, GameListener gl){

    }

    protected synchronized void removePlayer(String playerName){

    }

    protected synchronized Integer readyToStart(String playerName){
        return null;
    }

    protected synchronized Integer unReadyToStart(String playerName){
        return null;
    }

    private void startGame(){

    }

    protected synchronized Integer chosenSetup(String playerName, String ObjectiveCardID, boolean StartCardFlipped, Token color){
        return null;/**/
    }

    protected synchronized Integer placeCard(String playerName, String CardID, Coordinate pos, boolean flipped){
        return null;
    }

    protected synchronized Integer drawCard(String playerName, CardType type, int index){
        return null;
    }

    protected synchronized QuitLobbyEvent quitLobby(VirtualView vv){
        if (!virtualViewAccounts.containsKey(vv))
            return new QuitLobbyEvent(Feedback.FAILURE, "The user is not logged in.");

        //TODO extra events in gameController
        return null;
    }

    protected synchronized QuitGameEvent quitGame(VirtualView vv){
        //TODO implementation
        /*extra events from Model*/
        return null;
    }

    /**
     * Deletes the game.
     * This method deletes the Game instance that this controller manages and removes this GameController from the
     * list of game controllers in the Controller singleton.
     */
    protected synchronized void deleteGame(){

    }

    protected synchronized boolean isGameStarted(){return gameStarted;}

    protected synchronized List<String> getPlayers(){
        List<String> collect = new ArrayList<>();
        for (Pair<String, String> account : joinedPlayers.keySet())
            collect.add(account.key());
        return collect;
    }

    protected synchronized boolean isPlayerOffline(Pair<String, String> account){
        return !virtualViewAccounts.containsValue(account);
    }

    protected synchronized void reconnectPlayer(VirtualView vv, Pair<String, String> account, GameListener gl){
        if(joinedPlayers.containsKey(account) && !virtualViewAccounts.containsValue(account) && gameStarted)
            game.reconnectPlayer(account.key());
    }
    /**
     * Retrieves the unique identifier of the game.
     * This method retrieves the unique identifier of the Game instance that this controller manages.
     *
     * @return The unique identifier of the game.
     */
    protected synchronized String getIdentifier(){
        return id;
    }
}
