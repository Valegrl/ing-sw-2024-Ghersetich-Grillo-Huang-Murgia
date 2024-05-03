package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromController.KickedPlayerFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromController.UpdateLobbyPlayersEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChooseSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.DrawCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.PlaceCardEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

/**
 * This class is responsible for managing the game logic.
 * It interacts with the model and the VirtualView to control the flow of the game.
 *
 * <p>Each instance of this class corresponds to a single game in the application.
 * The game is identified by a unique identifier, which is also used as identifier for the {@link GameController}</p>
 *
 * <p>The class provides methods to delete a game and retrieve information about the game.</p>
 *
 * <p>The {@link GameController} is created with the {@link Controller}, which is used to start the game and retrieve
 * the game settings.
 * The {@link GameController} is then added to the list of active game controllers in the {@link Controller} singleton.</p>
 */
public class GameController {
    //TODO review class, implementation
    /**
     * The Game identifier.
     */
    private final String id;

    /**
     * The Game instance that this controller manages.
     * It contains the state of the game, including the list of players, the game board, and the game settings.
     */
    private Game game;

    /**
     * A flag indicating whether the game has started.
     * This is set to true when the game starts, and remains true for the duration of the game.
     */
    private boolean gameStarted;

    /**
     * A map that stores the players who have joined the lobby or were present at the start of the game.
     * The key is an Account object representing the account (username and password), and the value is
     * the corresponding GameListener.
     */
    private final Map<Account, GameListener> joinedPlayers = new HashMap<>();

    /**
     * A map that stores the players who are currently online in the lobby or game.
     * The key is the VirtualView associated with the player, and the value is an Account object
     * representing the account (username and password).
     */
    private final Map<VirtualView, Account> virtualViewAccounts = new HashMap<>();

    /**
     * A map that stores the readiness status of players in the lobby.
     * The key is the VirtualView associated with the player, and the value is a Boolean
     * indicating whether the player is ready to start the game.
     */
    private final Map<String, Boolean> readyLobbyPlayers = new HashMap<>();

    /**
     * A queue that stores the hosts for the game in the order they joined.
     * The VirtualView associated with each host is stored in this queue.
     * The host at the front of the queue is the current host of the game.
     */
    private final Queue<VirtualView> hostQueue = new LinkedList<>();

    /**
     * The number of players required for this game to start.
     */
    private final int requiredPlayers;

    /**
     * Constructs a new GameController with the given lobby.
     * It initializes the game state, adds the first player to the lobby, and adds
     * this controller to the list of game controllers in the Controller singleton.
     *
     * @param vv The VirtualView associated with the player creating the lobby/game.
     * @param account The account of the player creating the lobby/game, represented as a pair of strings (username, password).
     * @param gl The GameListener associated with the player creating the lobby/game.
     * @param gameID The unique identifier for the Game.
     * @param nRequiredPlayers The number of players required to start the game.
     */
    protected GameController(VirtualView vv, Account account, GameListener gl, String gameID, int nRequiredPlayers) {
        this.game = null;
        this.gameStarted = false;
        this.joinedPlayers.put(account, gl);
        this.virtualViewAccounts.put(vv, account);
        this.readyLobbyPlayers.put(account.getUsername(), false);
        this.hostQueue.add(vv);
        this.id = gameID;
        this.requiredPlayers = nRequiredPlayers;
        Controller.getInstance().addToGameControllers(this);
    }

    protected synchronized void addLobbyPlayer(VirtualView vv, Account account, GameListener gl){
        if (requiredPlayers > joinedPlayers.keySet().size()) {
            joinedPlayers.put(account, gl);
            virtualViewAccounts.put(vv, account);
            this.readyLobbyPlayers.put(account.getUsername(), false);
            this.hostQueue.add(vv);

            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                if (!entry.getKey().equals(account)) {
                    entry.getValue().update(
                            new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "New player " + account.getUsername() + " has joined!"));
                }
            }
        }
    }

    protected synchronized KickFromLobbyEvent kickPlayer(VirtualView vv, String playerName){
        if (!gameStarted) {
            if (vv.equals(hostQueue.peek())) {
                if (!virtualViewAccounts.get(vv).getUsername().equals(playerName)) {
                    VirtualView remove = null;
                    Account account = null;
                    for (Map.Entry<VirtualView, Account> entry : virtualViewAccounts.entrySet()) {
                        if (entry.getValue().getUsername().equals(playerName)) {
                            remove = entry.getKey();
                            account = entry.getValue();
                        }
                    }
                    if (remove == null)
                        return new KickFromLobbyEvent(Feedback.FAILURE, "The player " + playerName + " is not in the lobby.");
                    else {
                        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                            if (!entry.getKey().equals(virtualViewAccounts.get(remove)))
                                entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "The player " + playerName + " has been kicked."));
                            else
                                entry.getValue().update(new KickedPlayerFromLobbyEvent("You have been kicked from the lobby " + id + "!"));
                            joinedPlayers.remove(account);
                            virtualViewAccounts.remove(remove);
                            readyLobbyPlayers.remove(playerName);
                            hostQueue.remove(remove);
                        }
                        return new KickFromLobbyEvent(Feedback.SUCCESS, "Kick successful!");
                    }
                } else
                    return new KickFromLobbyEvent(Feedback.FAILURE, "You cannot kick yourself.");
            } else
                return new KickFromLobbyEvent(Feedback.FAILURE, "You are not the host of the lobby.");
        }
        return new KickFromLobbyEvent(Feedback.FAILURE, "You cannot kick other players during the game.");
    }

    protected synchronized PlayerReadyEvent readyToStart(VirtualView vv){
        if (!gameStarted) {
            if (virtualViewAccounts.containsKey(vv)) {
                Account account = virtualViewAccounts.get(vv);
                if (readyLobbyPlayers.get(account.getUsername())) {
                    return new PlayerReadyEvent(Feedback.SUCCESS, "You were already ready.");
                } else {
                    readyLobbyPlayers.put(account.getUsername(), true);
                    for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                        if (!entry.getKey().equals(virtualViewAccounts.get(vv)))
                            entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "The player " + account.getUsername() + " is ready!"));
                    return new PlayerReadyEvent(Feedback.SUCCESS, "You are now ready!");
                }
            }
            return new PlayerReadyEvent(Feedback.FAILURE, "You are not in the lobby.");
        }
        return new PlayerReadyEvent(Feedback.FAILURE, "The ready status applies only to the lobby phase.");
    }

    protected synchronized PlayerUnreadyEvent unReadyToStart(VirtualView vv){
        if (!gameStarted) {
            if (virtualViewAccounts.containsKey(vv)) {
                Account account = virtualViewAccounts.get(vv);
                if (!readyLobbyPlayers.get(account.getUsername())) {
                    return new PlayerUnreadyEvent(Feedback.SUCCESS, "You were already unready.");
                } else {
                    readyLobbyPlayers.put(account.getUsername(), false);
                    for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                        if (!entry.getKey().equals(virtualViewAccounts.get(vv)))
                            entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "The player " + account.getUsername() + " is unready!"));
                    return new PlayerUnreadyEvent(Feedback.SUCCESS, "You are now unready!");
                }
            }
            return new PlayerUnreadyEvent(Feedback.FAILURE, "You are not in the lobby.");
        }
        return new PlayerUnreadyEvent(Feedback.FAILURE, "The unready status applies only to the lobby phase.");
    }

    protected synchronized QuitLobbyEvent quitLobby(VirtualView vv){
        if (virtualViewAccounts.containsKey(vv)){
            if (!gameStarted){
                Account account = virtualViewAccounts.get(vv);
                joinedPlayers.remove(account);
                virtualViewAccounts.remove(vv);
                readyLobbyPlayers.remove(account.getUsername());
                hostQueue.remove(vv);

                if (!joinedPlayers.isEmpty()){
                    for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                        entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "The player " + account.getUsername() + " has left the lobby!"));
                } else
                    deleteGame();

                return new QuitLobbyEvent(Feedback.SUCCESS, "Quit successful!");
            } else
                return new QuitLobbyEvent(Feedback.FAILURE, "You can only quit a lobby during the session.");
        }
        return new QuitLobbyEvent(Feedback.FAILURE, "You are not in the lobby.");
    }

    protected synchronized void startGame(){
        if (!gameStarted){
            int count = 0;
            for (Boolean isReady : readyLobbyPlayers.values())
                if (isReady)
                    count++;
            if (count == virtualViewAccounts.size()){
                this.gameStarted = true;
                this.game = new Game(id, new ArrayList<>(readyLobbyPlayers.keySet()));
                this.game.gameSetup();
            }
        }
    }

    protected synchronized ChooseSetupEvent chosenSetup(VirtualView vv, String ObjectiveCardID, boolean StartCardFlipped, Token color){
        return null;/**/
    }

    protected synchronized PlaceCardEvent placeCard(VirtualView vv, String CardID, Coordinate pos, boolean flipped){
        return null;
    }

    protected synchronized DrawCardEvent drawCard(VirtualView vv, CardType type, int index){
        return null;
    }

    protected synchronized QuitGameEvent quitGame(VirtualView vv){
        /*remove vv to prevent reconnection*/
        //TODO implementation
        /*If no players, delete game. If 1 player, timer*/
        /*extra events from Model*/
        return null;
    }

    protected synchronized void DisconnectFromGame(VirtualView vv){
        /* remove vv only from virtualViewAccounts */
        /*extra events from Model*/
    }

    protected synchronized void reconnectPlayer(VirtualView vv, Account account, GameListener gl){
        if(joinedPlayers.containsKey(account) && !virtualViewAccounts.containsValue(account) && gameStarted) {
            game.reconnectPlayer(account.getUsername()); //TODO: updated events from Model, check timer/turn
            joinedPlayers.put(account, gl);
            virtualViewAccounts.put(vv, account);
        }
    }

    /**
     * Deletes the game.
     * This method deletes the Game instance that this controller manages and removes this GameController from the
     * list of game controllers in the Controller singleton.
     */
    private synchronized void deleteGame(){
        Controller.getInstance().removeFromGameControllers(this);
    }

    protected synchronized boolean isGameStarted(){return gameStarted;}

    /**
     * Retrieves the unique identifier of the game.
     * This method retrieves the unique identifier of the Game instance that this controller manages.
     *
     * @return The unique identifier of the game.
     */
    protected synchronized String getIdentifier(){
        return id;
    }

    protected synchronized List<String> getPlayers(){
        List<String> collect = new ArrayList<>();
        for (Account account : joinedPlayers.keySet())
            collect.add(account.getUsername());
        return collect;
    }

    protected List<Pair<String, Boolean>> getReadyLobbyPlayers() {
        List<Pair<String, Boolean>> collect = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : readyLobbyPlayers.entrySet())
             collect.add(new Pair<>(entry.getKey(), entry.getValue()));
        return collect;
    }

    protected synchronized Integer getNumOnlinePlayers(){
        return virtualViewAccounts.size();
    }

    protected synchronized int getRequiredPlayers() {
        return requiredPlayers;
    }

    protected synchronized boolean isPlayerOnline(Account account){
        return virtualViewAccounts.containsValue(account);
    }
}
