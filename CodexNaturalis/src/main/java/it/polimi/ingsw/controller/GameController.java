package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PlayerCardsSetup;

import java.util.*;
import java.util.stream.Collectors;

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

    private List<Pair<PlayerCardsSetup, Boolean>> setupData;

    private List<Pair<Token, Boolean>> colors;

    private Pair<Timer, TimerTask> timerSetupCards = null;
    private Pair<Timer, TimerTask> timerSetupToken = null;
    private Pair<Timer, TimerTask> timerAction = null; /* ? */
    private Timer timerWaiting = null;


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
                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                    entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), "The game has started!"));

                this.game = new Game(id, new ArrayList<>(readyLobbyPlayers.keySet()));
                List<PlayerCardsSetup> player = this.game.gameSetup();

                this.setupData = new ArrayList<>();
                for (PlayerCardsSetup p : player)
                    setupData.add(new Pair<>(p, false));

                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()){
                    String username = entry.getKey().getUsername();
                    PlayerCardsSetup playerSetup = Objects.requireNonNull(setupData.stream()
                            .filter(setup -> setup.key().getUsername().equals(username))
                            .findFirst()
                            .orElse(new Pair<>(null,null))).key();

                    if (playerSetup != null) {
                        ImmObjectiveCard[] obj = playerSetup.getImmObjectiveCards();
                        ImmStartCard start = playerSetup.getImmStartCard();
                        entry.getValue().update(new ChooseCardsSetupEvent(obj, start, "Choose your setup!"));
                    } else
                        System.err.println("An error occurred during the cards setup phase!");
                }
                this.timerSetupCards = setupCardsTimer();
            }
        }
    }

    protected synchronized ChosenCardsSetupEvent chosenCardsSetup(VirtualView vv, String ObjectiveCardID, boolean StartCardFlipped){
        if (virtualViewAccounts.containsKey(vv)) {
            if (gameStarted && game.getGameStatus() == GameStatus.SETUP && timerSetupCards != null && timerSetupToken == null) {
                if (timerSetupCards.value() != null){

                    String username = virtualViewAccounts.get(vv).getUsername();
                    for (Pair<PlayerCardsSetup, Boolean> p : setupData)
                        if (username.equals(p.key().getUsername())){
                            if (!p.value()) {
                                for (ObjectiveCard c : p.key().getObjectiveCards())
                                    if (c.getId().equals(ObjectiveCardID)) {
                                        boolean done = game.setupPlayerCards(username, c, StartCardFlipped);
                                        if (!done)
                                            throw new RuntimeException("A fatal error occurred with the player's username: " + username);
                                        setupData.remove(p);
                                        setupData.add(new Pair<>(p.key(), true));

                                        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                                            if (isPlayerOnline(entry.getKey()) && !entry.getKey().getUsername().equals(username))
                                                entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), "The player " + username + " has chosen a card setup."));

                                        return new ChosenCardsSetupEvent(Feedback.SUCCESS, "Your cards setup has been chosen!");
                                    }
                                return new ChosenCardsSetupEvent(Feedback.FAILURE, "The objective card ID is not valid.");
                            }
                            return new ChosenCardsSetupEvent(Feedback.FAILURE, "Choice has already been made.");
                        }
                    System.err.println("An error occurred during the chosen cards setup phase!");

                }
                return new ChosenCardsSetupEvent(Feedback.FAILURE, "The timer has run out! Your setup has been assigned automatically.");
            }
            return new ChosenCardsSetupEvent(Feedback.FAILURE, "You can only choose during the cards-setup phase.");
        }
        return new ChosenCardsSetupEvent(Feedback.FAILURE, "You are not in the game.");
    }

    private synchronized void AutoCardsSetup(){ //TODO: call this method from handlePlayerOffline, if we are in this phase.
        /* check timertask, se scaduto setup per tutti i false */

        /* se timertask non scaduto auto setup solo per i disconnected. */

        /* startTokenSetup sempre alla fine di entrambi*/
    }

    protected synchronized void startTokenSetup(){
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && timerSetupCards != null && timerSetupToken == null){
            int countTrue = 0;

            for (Pair<PlayerCardsSetup, Boolean> pair : setupData)
                if (pair.value()) {
                    countTrue++;
                }

            if (setupData.size() == countTrue) {
                this.colors = new ArrayList<>();
                for(Token t : Token.values())
                    colors.add(new Pair<>(t, true));

                List<Token> tokenList = colors.stream()
                        .map(Pair::key)
                        .toList();

                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) //TODO move?
                    if(isPlayerOnline(entry.getKey()))
                        entry.getValue().update(new ChooseTokenSetupEvent(tokenList, "Choose your color!"));

                this.timerSetupToken = setupTokenTimer();

                /* autotoken for offline player*/
            }
        }
    }

    protected synchronized ChosenTokenSetupEvent chosenTokenSetup(VirtualView vv, Token color){
        return null;
    }

    private synchronized void AutoTokenSetup(){ //TODO: ?call this method from handlePlayerOffline?, if we are in this phase.
        /* check timertask, se scaduto setup per tutti i false + (startRunning new timer action + set running)) */

        /* se timertask non scaduto auto setup solo per i disconnected. */
    }

    protected synchronized PlaceCardEvent placeCard(VirtualView vv, String CardID, Coordinate pos, boolean flipped){
        return null;
    }

    protected synchronized DrawCardEvent drawCard(VirtualView vv, CardType type, int index){
        return null;
    }

    protected synchronized QuitGameEvent quitGame(VirtualView vv){
        if (virtualViewAccounts.containsKey(vv)){
            if (gameStarted){
                Account account = virtualViewAccounts.get(vv);
                joinedPlayers.remove(account);
                virtualViewAccounts.remove(vv);

                List<String> onlinePlayers = getOnlinePlayers();
                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                    if (isPlayerOnline(entry.getKey()))
                        entry.getValue().update(new UpdateGamePlayersEvent(onlinePlayers, "The player " + account.getUsername() + " has left the game!"));

                handlePlayerOffline(account.getUsername());

                return new QuitGameEvent(Feedback.SUCCESS, "Quit successful!");
            } else
                return new QuitGameEvent(Feedback.FAILURE, "You can only quit a game during the session.");
        } else
            return new QuitGameEvent(Feedback.FAILURE, "You are not in the lobby.");
    }

    protected synchronized void DisconnectFromGame(VirtualView vv){
        if (virtualViewAccounts.containsKey(vv) && gameStarted){
            Account account = virtualViewAccounts.get(vv);
            joinedPlayers.put(account, null);
            virtualViewAccounts.remove(vv);

            List<String> onlinePlayers = getOnlinePlayers();
            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                if (isPlayerOnline(entry.getKey()))
                    entry.getValue().update(new UpdateGamePlayersEvent(onlinePlayers, "The player " + account.getUsername() + " has been disconnected!"));

            handlePlayerOffline(account.getUsername());
        }
    }

    protected synchronized void reconnectPlayer(VirtualView vv, Account account, GameListener gl){
        if (joinedPlayers.containsKey(account) && !virtualViewAccounts.containsValue(account) && gameStarted) {
            if (game.getGameStatus() == GameStatus.WAITING) {
                this.timerWaiting.cancel();
                this.timerWaiting = null;
            }
            game.reconnectPlayer(account.getUsername(), gl); //TODO: updated events from Model, check timer/turn/gameStatus
            joinedPlayers.put(account, gl);
            virtualViewAccounts.put(vv, account);
        }
    }

    private synchronized Pair<Timer, TimerTask> setupCardsTimer(){
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && timerSetupCards == null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.SETUP && timerSetupToken == null) {
                            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                                if (isPlayerOnline(entry.getKey()))
                                    entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), "The timer has run out!"));
                            GameController.this.timerSetupCards = new Pair<>(timer, null);
                            GameController.this.AutoCardsSetup();
                        }
                    }
                }
            };
            timer.schedule(task, 60000); //TODO config file
            return new Pair<>(timer, task);
        }
        else return null;
    }

    private synchronized Pair<Timer, TimerTask> setupTokenTimer(){
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && timerSetupCards != null && timerSetupToken == null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.SETUP) {
                            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                                if (isPlayerOnline(entry.getKey()))
                                    entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), "The timer has run out!"));
                            //TODO if for automatic choices + next phase (timer) + set null this
                            GameController.this.timerSetupToken = new Pair<>(timer, null);
                            GameController.this.AutoTokenSetup();
                        }
                    }
                }
            };
            timer.schedule(task, 30000); //TODO config file
            return new Pair<>(timer, task);
        }
        else return null;
    }

    private synchronized Pair<Timer, TimerTask> playerActionTimer(){
        return null;
    }

    private synchronized Timer waitingStatusTimer(){
        if (gameStarted && game.getGameStatus() == GameStatus.WAITING) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.WAITING) {
                            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                                if (isPlayerOnline(entry.getKey()))
                                    entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), "The timer has run out; the game has ended!"));
                            game.endGame();
                            deleteGame();
                        }
                    }
                }
            };
            timer.schedule(task, 60000 * 2); //TODO config file
            return timer;
        }
        else return null;
    }

    private synchronized void handlePlayerOffline(String username){
        game.offlinePlayer(username); //TODO automatic actions in model (place, draw, setup, status, turn)
        if (game.getGameStatus() == GameStatus.ENDED)
            deleteGame();
        else if (game.getGameStatus() == GameStatus.WAITING)
            timerWaiting = waitingStatusTimer();
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

    protected synchronized List<String> getOnlinePlayers(){
        List<String> collect = new ArrayList<>();
        for (Account account : virtualViewAccounts.values())
            collect.add(account.getUsername());
        return collect;
    }

    protected List<Pair<String, Boolean>> getReadyLobbyPlayers() {
        List<Pair<String, Boolean>> collect = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : readyLobbyPlayers.entrySet())
             collect.add(new Pair<>(entry.getKey(), entry.getValue()));
        return collect;
    }

    protected synchronized int getRequiredPlayers() {
        return requiredPlayers;
    }

    protected synchronized boolean isPlayerOnline(Account account){
        return virtualViewAccounts.containsValue(account);
    }
}
