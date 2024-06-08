package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromController.*;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.*;

import java.util.*;
import java.util.Timer;

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
     * A list of PlayerCardsSetup objects, each representing the setup data for a player.
     */
    private List<PlayerCardsSetup> setupData;

    /**
     * A list of PlayerTokenSetup objects, each representing the token setup data for a player.
     */
    private List<PlayerTokenSetup> setupColors;

    /**
     * A list of available tokens for the game.
     */
    private final List<Token> availableTokens;

    /**
     * A Pair object that holds a Timer and a TimerTask for the setup cards phase.
     */
    private Pair<Timer, TimerTask> setupCardsTimer = null;

    /**
     * A Pair object that holds a Timer and a TimerTask for the setup token phase.
     */
    private Pair<Timer, TimerTask> setupTokenTimer = null;

    /**
     * A flag indicating whether the current player, who has the turn, has placed a card.
     */
    private boolean startedMove = false;

    /**
     * A Timer for the action phase of the game (place + draw in the {@link GameStatus#RUNNING} or only place in
     * the {@link GameStatus#LAST_CIRCLE}).
     */
    private Timer actionTimer = null;

    /**
     * A Timer for the waiting phase of the game. This is applicable online if the game was previously in the
     * {@link GameStatus#RUNNING} or in the {@link GameStatus#LAST_CIRCLE}).
     */
    private Timer waitingTimer = null;


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
        this.availableTokens = new ArrayList<>(List.of(Token.values()));
        Controller.getInstance().addToGameControllers(this);
    }

    /**
     * Adds a player to the lobby.
     *
     * @param vv The VirtualView associated with the player joining the lobby.
     * @param account The account of the player joining the lobby, represented as a pair of strings (username, password).
     * @param gl The GameListener associated with the player joining the lobby.
     */
    protected synchronized void addLobbyPlayer(VirtualView vv, Account account, GameListener gl) {
        if (requiredPlayers > joinedPlayers.keySet().size()) {
            joinedPlayers.put(account, gl);
            virtualViewAccounts.put(vv, account);
            this.readyLobbyPlayers.put(account.getUsername(), false);
            this.hostQueue.add(vv);

            notifyAllLobbyPlayersExcept(account.getUsername(), account.getUsername() + " joined the lobby");
        }
    }

    /**
     * Kicks a player from the lobby. Only for the host player.
     *
     * @param vv The VirtualView associated with the player to be kicked.
     * @param playerName The name of the player to be kicked.
     * @return {@link KickFromLobbyEvent} indicating the result of the operation.
     */
    protected synchronized KickFromLobbyEvent kickPlayer(VirtualView vv, String playerName) {
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
                        return new KickFromLobbyEvent(Feedback.FAILURE, "Player " + playerName + " is not in the lobby.");
                    else {
                        readyLobbyPlayers.remove(playerName);
                        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                            if (!entry.getKey().equals(virtualViewAccounts.get(remove)))
                                entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "Player " + playerName + " has been kicked."));
                            else
                                entry.getValue().update(new KickedPlayerFromLobbyEvent("You have been kicked from the lobby '" + id + "'!"));
                        }
                        joinedPlayers.remove(account);
                        virtualViewAccounts.remove(remove);
                        hostQueue.remove(remove);
                        return new KickFromLobbyEvent(Feedback.SUCCESS, "Kick successful!");
                    }
                } else
                    return new KickFromLobbyEvent(Feedback.FAILURE, "You cannot kick yourself.");
            } else
                return new KickFromLobbyEvent(Feedback.FAILURE, "You are not the host of the lobby.");
        }
        return new KickFromLobbyEvent(Feedback.FAILURE, "You cannot kick other players during the game.");
    }

    /**
     * Marks a player as ready to start the game.
     *
     * @param vv The VirtualView associated with the player marking as ready.
     * @return {@link PlayerReadyEvent} indicating the result of the operation.
     */
    protected synchronized PlayerReadyEvent readyToStart(VirtualView vv) {
        if (!gameStarted) {
            if (virtualViewAccounts.containsKey(vv)) {
                Account account = virtualViewAccounts.get(vv);
                if (readyLobbyPlayers.get(account.getUsername())) {
                    return new PlayerReadyEvent(Feedback.SUCCESS, "You were already ready.");
                } else {
                    readyLobbyPlayers.put(account.getUsername(), true);
                    notifyAllLobbyPlayersExcept(account.getUsername(), "Player " + account.getUsername() + " is ready!");
                    return new PlayerReadyEvent(Feedback.SUCCESS, "You are now ready!");
                }
            }
            return new PlayerReadyEvent(Feedback.FAILURE, "You are not in the lobby.");
        }
        return new PlayerReadyEvent(Feedback.FAILURE, "The ready status applies only to the lobby phase.");
    }

    /**
     * Marks a player as not ready to start the game.
     *
     * @param vv The VirtualView associated with the player marking as not ready.
     * @return {@link PlayerUnreadyEvent} indicating the result of the operation.
     */
    protected synchronized PlayerUnreadyEvent unReadyToStart(VirtualView vv) {
        if (!gameStarted) {
            if (virtualViewAccounts.containsKey(vv)) {
                Account account = virtualViewAccounts.get(vv);
                if (!readyLobbyPlayers.get(account.getUsername())) {
                    return new PlayerUnreadyEvent(Feedback.SUCCESS, "You were already unready.");
                } else {
                    readyLobbyPlayers.put(account.getUsername(), false);
                    notifyAllLobbyPlayersExcept(account.getUsername(), "Player " + account.getUsername() + " is unready!");
                    return new PlayerUnreadyEvent(Feedback.SUCCESS, "You are now unready!");
                }
            }
            return new PlayerUnreadyEvent(Feedback.FAILURE, "You are not in the lobby.");
        }
        return new PlayerUnreadyEvent(Feedback.FAILURE, "The unready status applies only to the lobby phase.");
    }

    /**
     * Allows a player to quit the lobby.
     *
     * @param vv The VirtualView associated with the player quitting the lobby.
     * @return {@link QuitLobbyEvent} indicating the result of the operation.
     */
    protected synchronized QuitLobbyEvent quitLobby(VirtualView vv) {
        if (virtualViewAccounts.containsKey(vv)){
            if (!gameStarted){
                Account account = virtualViewAccounts.get(vv);
                joinedPlayers.remove(account);
                virtualViewAccounts.remove(vv);
                readyLobbyPlayers.remove(account.getUsername());
                hostQueue.remove(vv);

                if (!joinedPlayers.isEmpty()){
                    for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                        entry.getValue().update(new UpdateLobbyPlayersEvent(getReadyLobbyPlayers(), "Player " + account.getUsername() + " has left the lobby!"));
                } else
                    deleteGame();

                return new QuitLobbyEvent(Feedback.SUCCESS, "Successfully left the lobby.");
            } else
                return new QuitLobbyEvent(Feedback.FAILURE, "You can only quit a lobby during the session.");
        }
        return new QuitLobbyEvent(Feedback.FAILURE, "You are not in the lobby.");
    }

    /**
     * Starts the cards setup phase of the game.
     */
    protected synchronized void startCardsSetup() {
        if (!gameStarted){
            int count = readyLobbyPlayers.entrySet().stream().filter(Map.Entry::getValue).toList().size();
            if (count == virtualViewAccounts.size() && count == requiredPlayers){
                this.gameStarted = true;
                notifyAllOnlineGamePlayers("The game has started!");

                Map<String, GameListener> players = new HashMap<>();
                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
                    players.put(entry.getKey().getUsername(), entry.getValue());

                this.game = new Game(id, players);
                this.setupData = this.game.gameSetup(); /* update from model */
                this.setupCardsTimer = setupCardsTimer();
            }
        }
    }

    /**
     * Handles a player's choice of cards during the setup phase.
     *
     * @param vv The VirtualView associated with the player making the choice.
     * @param ObjectiveCardID The ID of the objective card chosen by the player.
     * @param StartCardFlipped Whether the player chose to flip the start card.
     * @return {@link ChosenCardsSetupEvent} indicating the result of the operation.
     */
    protected synchronized ChosenCardsSetupEvent chosenCardsSetup(VirtualView vv, String ObjectiveCardID, boolean StartCardFlipped) {
        if (virtualViewAccounts.containsKey(vv)) {
            if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer != null && setupTokenTimer == null) {
                if (setupCardsTimer.value() != null){

                    String username = virtualViewAccounts.get(vv).getUsername();
                    for (PlayerCardsSetup p : setupData)
                        if (username.equals(p.getUsername())){
                            if (!p.isChosen()) {
                                for (ObjectiveCard c : p.getObjectiveCards())
                                    if (c.getId().equals(ObjectiveCardID)) {
                                        boolean done = game.setupPlayerCards(username, c, StartCardFlipped);
                                        if (!done)
                                            throw new RuntimeException("A fatal error occurred with the player's username: " + username);
                                        p.setChosen(true);

                                        notifyAllOnlineGamePlayersExcept(username, "Player " + username + " has chosen a card setup.");
                                        return new ChosenCardsSetupEvent(Feedback.SUCCESS, "Your cards setup has been chosen!");
                                    }
                                return new ChosenCardsSetupEvent(Feedback.FAILURE, "The objective card ID is not valid.");
                            }
                            return new ChosenCardsSetupEvent(Feedback.FAILURE, "Choice has already been made.");
                        }
                    System.err.println("An error occurred during the chosen cards setup phase!");

                }
                return new ChosenCardsSetupEvent(Feedback.FAILURE, "The timer has run out! Your cards setup has been assigned automatically.");
            }
            return new ChosenCardsSetupEvent(Feedback.FAILURE, "You can only choose during the cards-setup phase.");
        }
        return new ChosenCardsSetupEvent(Feedback.FAILURE, "You are not in the game.");
    }

    /**
     * Automatically sets up the cards for players who have not chosen yet.
     * This method is called when the timer for the card setup phase expires.
     * It assigns the first objective card and does not flip the start card for each player who has not chosen yet.
     * If a player is disconnected, their setup is also automatically assigned.
     */
    private synchronized void autoCardsSetup() {
        if (setupCardsTimer.value() == null) {
            for (PlayerCardsSetup p : setupData) {
                String username = p.getUsername();
                if (!p.isChosen()) {
                    //FIXME config file
                    boolean done = game.setupPlayerCards(username, p.getObjectiveCards()[0], false);
                    if (!done)
                        throw new RuntimeException("A fatal error occurred with the player's username: " + username);
                    p.setChosen(true);

                    notifySpecificOnlineGamePlayer(username, "The timer has run out! Your cards setup has been assigned automatically.");
                }
            }
        }
        else {
            for (PlayerCardsSetup p : setupData)
                if (!p.isChosen() && !isPlayerOnline(p.getUsername())){
                    //FIXME config file
                    boolean done = game.setupPlayerCards(p.getUsername(), p.getObjectiveCards()[0], false);
                    if (!done)
                        throw new RuntimeException("A fatal error occurred with the player's username: " + p.getUsername());
                    p.setChosen(true);
                }
        }
    }

    /**
     * Starts the token setup phase of the game.
     * This method is called after all players have chosen their cards during the card setup phase.
     * It sends a ChooseTokenSetupEvent to all online players to prompt them to choose their token.
     */
    protected synchronized void startTokenSetup() {
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer != null && setupTokenTimer == null){
            autoCardsSetup();
            int countTrue = 0;

            for (PlayerCardsSetup p : setupData)
                if (p.isChosen()) {
                    countTrue++;
                }

            if (setupData.size() == countTrue) {
                this.setupColors = new ArrayList<>();
                List<Token> colors = new ArrayList<>(Arrays.asList(Token.values()));

                for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                    String username = entry.getKey().getUsername();
                    PlayerTokenSetup pts = new PlayerTokenSetup(username);
                    if (isPlayerOnline(username))
                        entry.getValue().update(new ChooseTokenSetupEvent(colors, "Choose your colored token from the following available ones:"));
                    else
                        pts.setDisconnected(true);

                    setupColors.add(pts);
                }

                this.setupTokenTimer = setupTokenTimer();
            }
        }
    }

    /**
     * Handles a player's choice of token during the setup phase.
     *
     * @param vv The VirtualView associated with the player making the choice.
     * @param color The color of the token chosen by the player.
     * @return {@link ChosenTokenSetupEvent} indicating the result of the operation.
     */
    protected synchronized ChosenTokenSetupEvent chosenTokenSetup(VirtualView vv, Token color){
        if (virtualViewAccounts.containsKey(vv)) {
            if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer != null && setupTokenTimer != null) {
                if (setupTokenTimer.value() != null) {

                    String username = virtualViewAccounts.get(vv).getUsername();
                    for (PlayerTokenSetup pts : setupColors)
                        if (username.equals(pts.getUsername())) {
                            if (pts.getToken() == null) {
                                if (!pts.isDisconnectedAtLeastOnce()) {
                                    if (availableTokens.contains(color)) {
                                        boolean done = game.setupPlayerToken(username, color);
                                        if (!done)
                                            throw new RuntimeException("A fatal error occurred with the player's username: " + username);
                                        pts.setToken(color);
                                        availableTokens.remove(color);
                                        String mes = "Player " + username + " has chosen the " + color + " token.";
                                        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                                            String other = entry.getKey().getUsername();
                                            if (isPlayerOnline(other) && !username.equals(other))
                                                entry.getValue().update(new ChooseTokenSetupEvent(availableTokens, mes));
                                        }

                                        return new ChosenTokenSetupEvent(Feedback.SUCCESS, "Your colored token has been chosen successfully!");
                                    }
                                    else {
                                        return new ChosenTokenSetupEvent(Feedback.FAILURE, "Token not available.");
                                    }
                                }
                                return new ChosenTokenSetupEvent(Feedback.FAILURE, "You cannot choose after a disconnection, wait for the timer to run out.");
                            }
                            return new ChosenTokenSetupEvent(Feedback.FAILURE, "Choice has already been made.");
                        }
                    System.err.println("An error occurred during the chosen cards setup phase!");

                }
                return new ChosenTokenSetupEvent(Feedback.FAILURE, "The timer has run out! Your token setup has been assigned automatically.");
            }
            return new ChosenTokenSetupEvent(Feedback.FAILURE, "You can only choose during the token-setup phase.");
        }
        return new ChosenTokenSetupEvent(Feedback.FAILURE, "You are not in the game.");
    }

    /**
     * Automatically sets up the tokens for players who have not chosen yet.
     * This method is called when the timer for the token setup phase expires.
     * It assigns a random available token for each player who has not chosen yet.
     * If a player is disconnected, or if they were previously disconnected, their setup is automatically assigned.
     * The setup for disconnected players is performed at the end, after all online players have chosen.
     */
    private synchronized void autoTokenSetup() {
        if (setupTokenTimer.value() == null)
            assignToken();

        else {
            int countOnlineDALO = 0;
            int tokensChosenNonDALO = 0;
            for (PlayerTokenSetup p : setupColors) {
                if (isPlayerOnline(p.getUsername())) {
                    if (p.isDisconnectedAtLeastOnce())
                        countOnlineDALO++;
                    else if (p.getToken() != null)
                        tokensChosenNonDALO++;
                }
            }

            if (virtualViewAccounts.size() - countOnlineDALO == tokensChosenNonDALO)
                assignToken();
        }
    }

    /**
     * Assigns a token to each player based on their setup data.
     * This method is called after all players have chosen their tokens during the token setup phase.
     * It updates each player's token in the game model.
     */
    private synchronized void assignToken() {
        for (PlayerTokenSetup pts : setupColors)
            if (pts.getToken() == null){
                if (!availableTokens.isEmpty()){
                    String username = pts.getUsername();
                    Token token = availableTokens.removeLast();
                    boolean done = game.setupPlayerToken(username, token);
                    if (!done)
                        throw new RuntimeException("A fatal error occurred with the player's username: " + username);
                    pts.setToken(token);

                    notifySpecificOnlineGamePlayer(username, "The timer has run out! Your token setup has been assigned automatically.");
                }
                else
                    throw new RuntimeException("Token are not enough.");
            }
    }


    /**
     * Starts the running phase of the game.
     * This method is called after all players have chosen their tokens during the token setup phase.
     * It initializes the game's turn and starts the action timer for the current player.
     */
    protected synchronized void startRunning() {
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer != null && setupTokenTimer != null){
            autoTokenSetup();
            int countTrue = 0;

            for (PlayerTokenSetup p : setupColors)
                if (p.getToken() != null) {
                    countTrue++;
                }

            if (setupColors.size() == countTrue) {
                this.game.startTurn(); /* update from model */

                if (game.getGameStatus() == GameStatus.RUNNING)
                    this.actionTimer = playerActionTimer();
                else
                    this.waitingTimer = waitingStatusTimer();
            }
        }
    }

    /**
     * Handles a player's action of placing a card during the game.
     *
     * @param vv The VirtualView associated with the player placing the card.
     * @param cardID The ID of the card to be placed.
     * @param pos The position where the card is to be placed.
     * @param flipped Whether the card is to be placed face down.
     * @return {@link PlaceCardEvent} indicating the result of the operation.
     */
    protected synchronized PlaceCardEvent placeCard(VirtualView vv, String cardID, Coordinate pos, boolean flipped) {
        if (virtualViewAccounts.containsKey(vv)) {
            if (gameStarted && (game.getGameStatus() == GameStatus.RUNNING || game.getGameStatus() == GameStatus.LAST_CIRCLE)) {
                String username = virtualViewAccounts.get(vv).getUsername();
                if (game.isTurnPlayer(username)){
                    if (!startedMove){
                        try {
                            game.placeCard(cardID, pos, flipped); /* update from model */
                            if (game.getGameStatus() == GameStatus.LAST_CIRCLE){
                                nextTurn(); /* update from model */
                            } else
                                this.startedMove = true;
                            return new PlaceCardEvent(Feedback.SUCCESS, null);
                        } catch (Exception e) {
                            return new PlaceCardEvent(Feedback.FAILURE, e.getMessage());
                        }
                    } else
                        return new PlaceCardEvent(Feedback.FAILURE, "You have already placed a card.");
                } else
                    return new PlaceCardEvent(Feedback.FAILURE, "It is not your turn.");
            } else
                return new PlaceCardEvent(Feedback.FAILURE, "You can place a card during the specific phase.");
        } else
            return new PlaceCardEvent(Feedback.FAILURE, "You are not in the game.");
    }

    /**
     * Handles a player's action of drawing a card during the game.
     *
     * @param vv The VirtualView associated with the player drawing the card.
     * @param type The type of the card to be drawn.
     * @param index The index of the card to be drawn.
     * @return {@link DrawCardEvent} indicating the result of the operation.
     */
    protected synchronized DrawCardEvent drawCard(VirtualView vv, CardType type, int index) {
        if (virtualViewAccounts.containsKey(vv)) {
            if (gameStarted && (game.getGameStatus() == GameStatus.RUNNING)) {
                String username = virtualViewAccounts.get(vv).getUsername();
                if (game.isTurnPlayer(username)) {
                    if (startedMove) {
                        try {
                            game.drawCard(type, index); /* update from model */
                            nextTurn(); /* update from model */
                            return new DrawCardEvent(Feedback.SUCCESS, null);
                        } catch (Exception e) {
                            return new DrawCardEvent(Feedback.FAILURE, e.getMessage());
                        }
                    } else
                        return new DrawCardEvent(Feedback.FAILURE, "You have to place a card first.");
                } else
                    return new DrawCardEvent(Feedback.FAILURE, "It is not your turn.");
            } else
                return new DrawCardEvent(Feedback.FAILURE, "You can draw a card during the specific phase.");
        } else
            return new DrawCardEvent(Feedback.FAILURE, "You are not in the game.");
    }


    /**
     * Automatically draws a card for the current player.
     * This method is called when the timer for the player's action phase expires, the player has placed a card
     * but has not drawn a card.
     * It tries to draw a card from the resource deck or the gold deck, starting from the top.
     * If all attempts fail, it throws a RuntimeException.
     */
    private synchronized void autoDrawCard(boolean callNextTurn) {
        if (gameStarted && (game.getGameStatus() == GameStatus.RUNNING)) {
            if (startedMove) {
                List<Pair<CardType, Integer>> drawOptions = new ArrayList<>(){{ //FIXME config file
                    add(new Pair<>(CardType.RESOURCE, 2));
                    add(new Pair<>(CardType.GOLD, 2));
                    add(new Pair<>(CardType.RESOURCE, 1));
                    add(new Pair<>(CardType.RESOURCE, 0));
                    add(new Pair<>(CardType.GOLD, 1));
                    add(new Pair<>(CardType.GOLD, 0));
                }};

                int i = 0;
                boolean success = false;
                Pair<CardType, Integer> option;
                while (!success && i < drawOptions.size()) {
                    option = drawOptions.get(i);
                    try {
                        game.drawCard(option.key(), option.value()); /* update from model */
                        success = true;
                    } catch (Exception e) {
                        i++;
                    }
                }
                if (i==drawOptions.size())
                    throw new RuntimeException("There are no drawable cards.");

                if (callNextTurn)
                    nextTurn(); /* update from model */
            }
        }
    }

    /**
     * Allows a player to quit the game.
     *
     * @param vv The VirtualView associated with the player quitting the game.
     * @return {@link QuitGameEvent} indicating the result of the operation.
     */
    protected synchronized QuitGameEvent quitGame(VirtualView vv) {
        if (virtualViewAccounts.containsKey(vv)){
            if (gameStarted){
                Account account = virtualViewAccounts.get(vv);
                joinedPlayers.remove(account);
                virtualViewAccounts.remove(vv);

                notifyAllOnlineGamePlayers("Player " + account.getUsername() + " has left the game!");
                handlePlayerOffline(account.getUsername());

                return new QuitGameEvent(Feedback.SUCCESS, "Successfully abandoned the current game!");
            } else
                return new QuitGameEvent(Feedback.FAILURE, "You can only quit a game during the session.");
        } else
            return new QuitGameEvent(Feedback.FAILURE, "You are not in the lobby.");
    }

    /**
     * Handles a player's disconnection from the game.
     * This method is called when a player disconnects during the game.
     * It updates the player's status and notifies the other players about the disconnection.
     *
     * @param vv The VirtualView associated with the player disconnecting.
     */
    protected synchronized void disconnectFromGame(VirtualView vv) {
        if (virtualViewAccounts.containsKey(vv) && gameStarted){
            Account account = virtualViewAccounts.get(vv);
            joinedPlayers.put(account, null);
            virtualViewAccounts.remove(vv);

            notifyAllOnlineGamePlayers("Player " + account.getUsername() + " has been disconnected!");
            handlePlayerOffline(account.getUsername());
        }
    }

    /**
     * Handles a player's reconnection to the game.
     * This method is called when a player reconnects during the game.
     * It updates the player's status and notifies the other players about the reconnection.
     *
     * @param vv The VirtualView associated with the player reconnecting.
     * @param account The account of the player reconnecting, represented as a pair of strings (username, password).
     * @param gl The GameListener associated with the player reconnecting.
     */
    protected synchronized void reconnectPlayer(VirtualView vv, Account account, GameListener gl) {
        if (joinedPlayers.containsKey(account) && !virtualViewAccounts.containsValue(account) && gameStarted) {
            String waitingPlayer;
            GameStatus preGS = game.getGameStatus();
            if (preGS == GameStatus.WAITING) {
                this.waitingTimer.cancel();
                waitingPlayer = virtualViewAccounts.values().stream().findFirst()
                        .orElseThrow(() -> new NoSuchElementException("At least one player must be online."))
                        .getUsername();
                if (!game.isTurnPlayer(waitingPlayer))
                    this.startedMove = false;
            }

            joinedPlayers.put(account, gl);
            virtualViewAccounts.put(vv, account);
            String username = account.getUsername();
            notifyAllOnlineGamePlayersExcept(username,  "Player " + username + " has reconnected to the game!");

            game.reconnectPlayer(account.getUsername(), gl);

            GameStatus newGS = game.getGameStatus();
            if (preGS == GameStatus.WAITING && newGS != GameStatus.ENDED)
                this.actionTimer = playerActionTimer();
            else if (newGS == GameStatus.ENDED)
                deleteGame();
        }
    }

    /**
     * Creates a timer for the card setup phase of the game.
     * This method is called when the card setup phase starts.
     * It creates a Timer and a TimerTask that will call the autoCardsSetup method when the timer expires.
     *
     * @return A Pair object that holds the Timer and the TimerTask.
     */
    private synchronized Pair<Timer, TimerTask> setupCardsTimer() {
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer == null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.SETUP && setupTokenTimer == null) {
                            GameController.this.setupCardsTimer = new Pair<>(timer, null);
                            GameController.this.startTokenSetup();
                        }
                    }
                }
            };
            timer.schedule(task, 60000); //FIXME config file
            return new Pair<>(timer, task);
        }
        else return null;
    }

    /**
     * Creates a timer for the token setup phase of the game.
     * This method is called when the token setup phase starts.
     * It creates a Timer and a TimerTask that will call the autoTokenSetup method when the timer expires.
     *
     * @return A Pair object that holds the Timer and the TimerTask.
     */
    private synchronized Pair<Timer, TimerTask> setupTokenTimer() {
        if (gameStarted && game.getGameStatus() == GameStatus.SETUP && setupCardsTimer != null && setupTokenTimer == null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.SETUP) {
                            GameController.this.setupTokenTimer = new Pair<>(timer, null);
                            GameController.this.startRunning();
                        }
                    }
                }
            };
            timer.schedule(task, 30000); //FIXME config file
            return new Pair<>(timer, task);
        }
        else return null;
    }

    /**
     * Creates a timer for the action phase of the game.
     * This method is called when the action phase starts for a player.
     * It creates a Timer that will call the autoDrawCard method when the timer expires, if needed.
     *
     * @return The Timer for the action phase.
     */
    private synchronized Timer playerActionTimer(){
        if (gameStarted && (game.getGameStatus() == GameStatus.RUNNING || game.getGameStatus() == GameStatus.LAST_CIRCLE)) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        GameStatus gameStatus = game.getGameStatus();
                        if (gameStatus == GameStatus.RUNNING || gameStatus == GameStatus.LAST_CIRCLE) {
                            String current = game.getCurrentPlayerUsername();
                            for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
                                String checkUsername = entry.getKey().getUsername();
                                if (checkUsername.equals(current) && isPlayerOnline(current)) {
                                    entry.getValue().update(new SelfTurnTimerExpiredEvent());
                                    break;
                                }
                            }
                            notifyAllOnlineGamePlayersExcept(current, "The timer for player " + current + " has run out!");
                            if (gameStatus == GameStatus.RUNNING && startedMove)
                                autoDrawCard(true);
                            else
                                nextTurn();
                        }
                    }
                }
            };
            timer.schedule(task, 60000 * 2); //FIXME config file
            return timer;
        }
        else return null;
    }

    /**
     * Creates a timer for the waiting phase of the game.
     * This method is called when the waiting phase starts for a player.
     * It creates a Timer that will call the deleteGame method when the timer expires.
     *
     * @return The Timer for the waiting phase.
     */
    private synchronized Timer waitingStatusTimer() {
        if (gameStarted && game.getGameStatus() == GameStatus.WAITING) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (GameController.this) {
                        if (game.getGameStatus() == GameStatus.WAITING) {
                            notifyAllOnlineGamePlayers("The timer has run out; the game has ended!");
                            game.endGame();
                            deleteGame();
                        }
                    }
                }
            };
            timer.schedule(task, 60000 * 2); //FIXME config file
            return timer;
        }
        else return null;
    }

    /**
     * Handles a player's disconnection during the game.
     * This method is invoked when a player disconnects from or quits the game.
     * It updates the player's status and notifies the other players about the disconnection.
     *
     * @param username The username of the player who disconnected/quit.
     */
    private synchronized void handlePlayerOffline(String username) {
        if (gameStarted) {
            if (nOnlinePlayers() > 0) {
                game.offlinePlayer(username);

                GameStatus gs = game.getGameStatus();
                if (gs == GameStatus.SETUP) {
                    if (setupTokenTimer == null) {
                        startTokenSetup();
                    } else {
                        for (PlayerTokenSetup pts : setupColors)
                            if (pts.getUsername().equals(username))
                                pts.setDisconnected(true);
                        startRunning();
                    }

                } else if (gs == GameStatus.RUNNING) {
                    if (game.isTurnPlayer(username)) {
                        if (startedMove)
                            autoDrawCard(true);
                        else
                            nextTurn();
                    }

                } else if (gs == GameStatus.LAST_CIRCLE) {
                    if (game.isTurnPlayer(username))
                            nextTurn();

                } else if (game.getGameStatus() == GameStatus.WAITING && joinedPlayers.size() > 1) {
                    if (game.getBackupGameStatus() == GameStatus.RUNNING && game.isTurnPlayer(username))
                        if (startedMove)
                            autoDrawCard(false);

                    this.actionTimer.cancel();
                    this.waitingTimer = waitingStatusTimer();

                } else {
                    game.endGame();
                    deleteGame();
                }
            }
            else
                deleteGame();
        }
    }


    /**
     * Deletes the game.
     * This method is called when the game ends.
     * It deletes the Game instance that this controller manages and removes this GameController from the
     * list of game controllers in the Controller singleton.
     */
    private synchronized void deleteGame() {
        if (gameStarted) {
            GameStatus gc = game.getGameStatus();
            if (gc == GameStatus.SETUP) {
                if (setupTokenTimer == null)
                    setupCardsTimer.key().cancel();
                else
                    setupTokenTimer.key().cancel();
            }else if (gc == GameStatus.WAITING)
                waitingTimer.cancel();
        }
        joinedPlayers.clear();
        virtualViewAccounts.clear();
        readyLobbyPlayers.clear();
        hostQueue.clear();
        Controller.getInstance().removeFromGameControllers(this);
    }

    /**
     * Checks if the game has started.
     * @return true if the game has started, false otherwise.
     */
    protected synchronized boolean isGameStarted() {return gameStarted;}

    /**
     * This method retrieves the unique identifier of the Game instance that this controller manages.
     * @return The unique identifier of the game.
     */
    protected synchronized String getIdentifier() {
        return id;
    }

    /**
     * @return A list of player usernames.
     */
    protected synchronized List<String> getPlayers() {
        List<String> collect = new ArrayList<>();
        for (Account account : joinedPlayers.keySet())
            collect.add(account.getUsername());
        return collect;
    }

    /**
     * @return A map of online player usernames.
     */
    protected synchronized Map<String, Boolean> getOnlinePlayers() {
        Map<String, Boolean> collect = new HashMap<>();
        for (Account account : joinedPlayers.keySet()) {
            if (virtualViewAccounts.containsValue(account))
                collect.put(account.getUsername(), true);
            else
                collect.put(account.getUsername(), false);
        }
        return collect;
    }

    /**
     * @return The number of online players.
     */
    protected synchronized int nOnlinePlayers() {
        return virtualViewAccounts.size();
    }

    /**
     * @return A map with player usernames as keys and their readiness status as values.
     */
    protected synchronized Map<String, Boolean> getReadyLobbyPlayers() {
        Map<String, Boolean> collect = new LinkedHashMap<>();
        List<String> hostQueueNames = hostQueue.stream()
                .map(vv -> virtualViewAccounts.get(vv).getUsername())
                .toList();

        for (String name : hostQueueNames) {
            Boolean status = readyLobbyPlayers.get(name);
            if (status != null) {
                collect.put(name, status);
            }
        }
        return collect;
    }

    /**
     * @return The number of players required to start the game.
     */
    protected synchronized int getRequiredPlayers() {
        return requiredPlayers;
    }

    /**
     * Checks if a player is online.
     * @param username The username of the player to check.
     * @return true if the player is online, false otherwise.
     */
    protected synchronized boolean isPlayerOnline(String username) {
        for (Account account : virtualViewAccounts.values()) {
            if (account.getUsername().equals(username))
                return true;
        }
        return false;
    }

    /**
     * Advances the game to the next turn.
     * This method is called when a player's turn ends.
     * It updates the game's turn and starts the action timer for the next player.
     */
    private synchronized void nextTurn(){
        this.startedMove = false;
        this.actionTimer.cancel();
        game.newTurn(true);

        if (game.getGameStatus() != GameStatus.ENDED) {
            this.actionTimer = playerActionTimer();
        } else
            deleteGame();
    }

    /**
     * Sends a global chat message to all online players in the game.
     * The message is sent from the player associated with the provided VirtualView.
     *
     * @param vv The VirtualView associated with the player sending the message.
     * @param chatMessage The message to be sent.
     * @return {@link ChatGMEvent} with feedback about the operation.
     * @throws RuntimeException if the GameListener associated with a player is null.
     */
    protected synchronized ChatGMEvent chatGlobalMessage(VirtualView vv, ChatMessage chatMessage){
        if (virtualViewAccounts.containsKey(vv)) {
            String sender = virtualViewAccounts.get(vv).getUsername();
            chatMessage.setSender(sender);

            for (Account account : joinedPlayers.keySet()) {
                GameListener listener = joinedPlayers.get(account);
                String other = account.getUsername();
                if (listener != null) {
                    if (isPlayerOnline(other) && !sender.equals(other))
                        listener.update(new ChatGMEvent(Feedback.SUCCESS, "New chat message.", chatMessage));
                } else
                    throw new RuntimeException("Error with listener.");
            }
            return new ChatGMEvent(Feedback.SUCCESS, "New chat message.", chatMessage);
        }
        return new ChatGMEvent(Feedback.FAILURE,"You are not in the game.", chatMessage);
    }

    /**
     * Sends a private chat message to a specific player in the game.
     * The message is sent from the player associated with the provided VirtualView to the recipient.
     *
     * @param vv The VirtualView associated with the player sending the message.
     * @param pChatMessage The message to be sent.
     * @return {@link ChatPMEvent} with feedback about the operation.
     * @throws RuntimeException if the GameListener associated with a player is null.
     */
    protected synchronized ChatPMEvent chatPrivateMessage(VirtualView vv, PrivateChatMessage pChatMessage){
        if (virtualViewAccounts.containsKey(vv)) {
            String sender = virtualViewAccounts.get(vv).getUsername();
            pChatMessage.setSender(sender);
            String recipient = pChatMessage.getRecipient();

            for (Account account : joinedPlayers.keySet()) {
                if (account.getUsername().equals(recipient)) {
                    GameListener listener = joinedPlayers.get(account);
                    if (listener != null) {
                        if(isPlayerOnline(account.getUsername())) {
                            listener.update(new ChatPMEvent(Feedback.SUCCESS,"New chat message.", pChatMessage));
                            return new ChatPMEvent(Feedback.SUCCESS, "New chat message.", pChatMessage);
                        } else
                            return new ChatPMEvent(Feedback.FAILURE, "Player not online.", pChatMessage);
                    } else
                        throw new RuntimeException("Error with listener.");
                }
            }
            return new ChatPMEvent(Feedback.FAILURE, "Player not found.", pChatMessage);
        }
        return new ChatPMEvent(Feedback.FAILURE, "You are not in the game.", pChatMessage);
    }

    /**
     * Sends a message to all players in the lobby except for the specified player.
     * @param username The username of the player to exclude.
     * @param message The message to send.
     */
    private void notifyAllLobbyPlayersExcept(String username, String message) {
        Map<String, Boolean> readyPlayers = getReadyLobbyPlayers();
        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
            if (!entry.getKey().getUsername().equals(username))
                entry.getValue().update(new UpdateLobbyPlayersEvent(readyPlayers, message));
    }

    /**
     * Sends a message to all online players in the game.
     * @param message The message to send.
     */
    private void notifyAllOnlineGamePlayers(String message) {
        Map<String, Boolean> onlinePlayers = getOnlinePlayers();
        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet())
            if (isPlayerOnline(entry.getKey().getUsername()))
                entry.getValue().update(new UpdateGamePlayersEvent(onlinePlayers, message));
    }

    /**
     * Sends a message to all online players in the game except for the specified player.
     * @param username The username of the player to exclude.
     * @param message The message to send.
     */
    private void notifyAllOnlineGamePlayersExcept(String username, String message) {
        Map<String, Boolean> onlinePlayers = getOnlinePlayers();
        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
            String otherUsername = entry.getKey().getUsername();
            if (!otherUsername.equals(username) && isPlayerOnline(otherUsername))
                entry.getValue().update(new UpdateGamePlayersEvent(onlinePlayers, message));
        }
    }

    /**
     * Sends a message to a specific online player in the game.
     * @param username The username of the player to send the message to.
     * @param message The message to send.
     */
    private void notifySpecificOnlineGamePlayer(String username, String message) {
        for (Map.Entry<Account, GameListener> entry : joinedPlayers.entrySet()) {
            String checkUsername = entry.getKey().getUsername();
            if (checkUsername.equals(username) && isPlayerOnline(username)) {
                entry.getValue().update(new UpdateGamePlayersEvent(getOnlinePlayers(), message));
                return;
            }
        }
    }
}
