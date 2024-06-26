package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.GameListener;
import it.polimi.ingsw.utils.Account;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

/**
 * The {@link Controller} class is a Singleton that serves as the main hub for managing game controllers and user
 * accounts in the application. It maintains a list of {@link GameController} instances, each corresponding to a
 * game that is currently active.
 *
 * <p>This class is responsible for handling user login, logout, registration, and account deletion events. It also
 * manages the creation and joining of game lobbies, as well as the reconnection of players to existing games.
 * Furthermore, it provides methods to retrieve lists of available lobbies and offline games for a user.</p>
 *
 * The {@link Controller} class also manages the association between user accounts and their respective
 * {@link VirtualView} instances, which represent the user's view of the game. Use the {@link #getInstance()}
 * method to get the single instance of this class.
 */
public class Controller {

    /**
     * The singleton instance of the Controller.
     */
    private static Controller controller = null;

    /**
     * The list of GameController instances.
     */
    private final List<GameController> gameControllers;

    /**
     * All existing accounts.
     */
    private final Set<Account> userAccounts = new HashSet<>();

    /**
     * Only the accounts that are currently logged in are associated with a VirtualView.
     */
    private final Map<VirtualView, Account> virtualViewAccounts = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     * Initializes the lists of game controllers.
     */
    private Controller() {
        gameControllers = new ArrayList<>();
    }

    /**
     * Handles the user login event.
     *
     * @param vv The user's VirtualView.
     * @param account The user's account.
     * @return The login event.
     */
    protected synchronized LoginEvent login(VirtualView vv, Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        if ( isFormatInvalid(username) || isFormatInvalid(password) || !userAccounts.contains(account))
            return new LoginEvent(Feedback.FAILURE, username, "The username or password is not valid.");

        if (virtualViewAccounts.containsKey(vv)) {
            if (virtualViewAccounts.get(vv).equals(account))
                return new LoginEvent(Feedback.FAILURE, username, "You are already logged in to this account.");
            else
                return new LoginEvent(Feedback.FAILURE, username, "You are already logged in to another account.");
        }

        if (virtualViewAccounts.containsValue(account))
            return new LoginEvent(Feedback.FAILURE, username, "This account is already connected to another instance.");

        virtualViewAccounts.put(vv, account);
        return new LoginEvent(Feedback.SUCCESS, username, "Login successful!");
    }

    /**
     * Handles the user logout event.
     *
     * @param vv The user's VirtualView.
     * @return The logout event.
     */
    protected synchronized LogoutEvent logout(VirtualView vv){
        if (!virtualViewAccounts.containsKey(vv))
            return new LogoutEvent(Feedback.SUCCESS, "You have already logged out.");

        String username = virtualViewAccounts.get(vv).getUsername();

        for (GameController gc : gameControllers)
            if (gc.getPlayers().contains(username)){
                if (!gc.isGameStarted())
                    gc.quitLobby(vv);
                else
                    gc.quitGame(vv);

                virtualViewAccounts.remove(vv);
                return new LogoutEvent(Feedback.SUCCESS, "Logout successful!");
            }

        virtualViewAccounts.remove(vv);
        return new LogoutEvent(Feedback.SUCCESS, "Logout successful!");
    }

    /**
     * Handles the event of registering a new account.
     *
     * @param newAccount The new account to register.
     * @return The registration event.
     */
    protected synchronized RegisterEvent register(Account newAccount){
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();

        if ( isFormatInvalid(username) || isFormatInvalid(password))
            return new RegisterEvent(Feedback.FAILURE, "Both the username and password must not contain spaces and must not be empty.");

        for (Account existingAccount : userAccounts) {
            if (existingAccount.getUsername().equals(username)) {
                return new RegisterEvent(Feedback.FAILURE, "Username already exists.");
            }
        }

        userAccounts.add(newAccount);
        return new RegisterEvent(Feedback.SUCCESS, "Registration successful!");
    }

    /**
     * Handles the event of deleting an account.
     *
     * @param vv The user's VirtualView.
     * @return The account deletion event.
     */
    protected synchronized DeleteAccountEvent deleteAccount(VirtualView vv){
        if (!virtualViewAccounts.containsKey(vv))
            return new DeleteAccountEvent(Feedback.FAILURE, "You must log in first.");

        Account account = virtualViewAccounts.get(vv);
        logout(vv);
        userAccounts.remove(account);
        return new DeleteAccountEvent(Feedback.SUCCESS, "Account deleted successfully!");
    }

    /**
     * Handles the event of creating a new lobby.
     *
     * @param vv The user's VirtualView.
     * @param gl The user's GameListener.
     * @param lobbyID The ID of the lobby to create.
     * @param nRequiredPlayers The number of players required to start the game.
     * @return The lobby creation event and the GameController of the new lobby.
     */
    protected synchronized Pair<CreateLobbyEvent, GameController>
                           createLobby(VirtualView vv, GameListener gl, String lobbyID, int nRequiredPlayers){
        if (!virtualViewAccounts.containsKey(vv))
            return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "You must log in first."), null);

        if (lobbyID == null || lobbyID.isEmpty() || lobbyID.length() > JsonConfig.getInstance().getMaxLobbyIdLength())
            return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "The provided lobby ID is not allowed."), null);

        if (nRequiredPlayers < 2 || nRequiredPlayers > 4)
            return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "The number of players should be between 2 and 4."), null);

        Account account = virtualViewAccounts.get(vv);
        for (GameController gc : gameControllers) {
            if (gc.getIdentifier().equals(lobbyID))
                return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "The lobby ID you entered is already in use."), null);
            else if (gc.isPlayerOnline(account.getUsername())) {
                if(gc.isGameStarted())
                    return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "Your account is already online in a game."), null);
                else
                    return new Pair<>(new CreateLobbyEvent(Feedback.FAILURE, "Your account is already online in a lobby."), null);
            }
        }

        GameController newGC = new GameController(vv, virtualViewAccounts.get(vv), gl, lobbyID, nRequiredPlayers);
        return new Pair<>(new CreateLobbyEvent(Feedback.SUCCESS, newGC.getReadyLobbyPlayers(), lobbyID, "New " + lobbyID + " lobby has been created!"), newGC);
    }

    /**
     * Handles the event of joining an existing lobby.
     *
     * @param vv The user's VirtualView.
     * @param gl The user's GameListener.
     * @param lobbyID The ID of the lobby to join.
     * @return The lobby joining event and the GameController of the lobby.
     */
    protected synchronized Pair<JoinLobbyEvent, GameController>
                           joinLobby(VirtualView vv, GameListener gl, String lobbyID){
        GameController found = null;

        if (!virtualViewAccounts.containsKey(vv))
            return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "You must log in first."), null);

        if (lobbyID == null || lobbyID.isEmpty())
            return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "The provided lobby ID is not valid."), null);

        Account account = virtualViewAccounts.get(vv);
        for (GameController gc : gameControllers) {
            if (gc.isPlayerOnline(account.getUsername())) {
                if(gc.isGameStarted())
                    return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "Your account is already online in a game."), null);
                else
                    return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "Your account is already online in a lobby."), null);
            }
            if (gc.getIdentifier().equals(lobbyID)) {
                if (!gc.isGameStarted()) {
                    if (gc.getRequiredPlayers() > gc.getPlayers().size())
                        found = gc;
                    else
                        return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "The lobby is already full."), null);
                } else
                    return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "The lobby has already started a game."), null);
            }
        }

        if (found != null){
            found.addLobbyPlayer(vv, virtualViewAccounts.get(vv), gl);
            return new Pair<>(new JoinLobbyEvent(Feedback.SUCCESS, found.getReadyLobbyPlayers(), lobbyID, "'" + lobbyID + "' lobby joined!"), found);
        }

        return new Pair<>(new JoinLobbyEvent(Feedback.FAILURE, "The lobby does not exist."), null);
    }

    /**
     * Handles the event of a player attempting to reconnect to an existing game.
     * A player can only reconnect if they were present at the start of the game.
     *
     * @param vv The VirtualView of the reconnecting player.
     * @param gl The GameListener of the reconnecting player.
     * @param gameID The ID of the game to which the player is attempting to reconnect.
     * @return A pair containing the reconnection event and the GameController of the game.
     */
    protected synchronized Pair<ReconnectToGameEvent, GameController>
                           reconnectToGame(VirtualView vv, GameListener gl, String gameID){
        GameController found = null;

        if (!virtualViewAccounts.containsKey(vv))
            return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "You must log in first."), null);

        if (gameID == null || gameID.isEmpty())
            return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "The provided game ID is not allowed."), null);

        Account account = virtualViewAccounts.get(vv);
        String username = account.getUsername();
        for (GameController gc : gameControllers){
            if (gc.isPlayerOnline(account.getUsername())) {
                if(gc.isGameStarted())
                    return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "Your account is already online in a game."), null);
                else
                    return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "Your account is already online in a lobby."), null);
            }
            if (gc.isGameStarted() && gc.getIdentifier().equals(gameID) && gc.getPlayers().contains(username))
                found = gc;
        }

        if (found != null){
            if (found.reconnectPlayer(vv, account, gl))
                return new Pair<>(new ReconnectToGameEvent(Feedback.SUCCESS, gameID, "ok"), found);
            else
                return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "The game has ended."), null);
        }

        return new Pair<>(new ReconnectToGameEvent(Feedback.FAILURE, gameID, "The game does not exist, or you do not have the permission to enter it."), null);
    }

    /**
     * Retrieves the list of available lobbies.
     *
     * @return The event with the list of available lobbies.
     */
    protected synchronized AvailableLobbiesEvent getLobbiesAvailable(VirtualView vv){
        if (!virtualViewAccounts.containsKey(vv))
            return new AvailableLobbiesEvent(Feedback.FAILURE, new ArrayList<>(), "You must log in first.");

        List<LobbyState> collect = new ArrayList<>();

        for (GameController gc : gameControllers)
            if (!gc.isGameStarted() && (gc.getRequiredPlayers() > gc.getPlayers().size()))
                collect.add(new LobbyState(gc.getIdentifier(), gc.nOnlinePlayers(), gc.getRequiredPlayers()));

        if (collect.isEmpty())
            return new AvailableLobbiesEvent(Feedback.SUCCESS, collect, "There are no available lobbies.");
        return new AvailableLobbiesEvent(Feedback.SUCCESS, collect, "Choose one of the available lobbies:");
    }

    /**
     * Retrieves the list of offline games available for the user. These games are ones from which the user was
     * disconnected due to network issues, not ones that the user voluntarily quit.
     *
     * @param vv The user's VirtualView.
     * @return The event with the list of offline games available.
     */
    protected synchronized GetMyOfflineGamesEvent getMyOfflineGamesAvailable(VirtualView vv){
        if(!virtualViewAccounts.containsKey(vv))
            return new GetMyOfflineGamesEvent(Feedback.FAILURE, new ArrayList<>(), "You must log in first.");

        List<LobbyState> collect = new ArrayList<>();
        String username = virtualViewAccounts.get(vv).getUsername();

        for (GameController gc : gameControllers)
            if (gc.isGameStarted() && gc.getPlayers().contains(username))
                collect.add(new LobbyState(gc.getIdentifier(), gc.nOnlinePlayers(), gc.getRequiredPlayers()));

        if (collect.isEmpty())
            return new GetMyOfflineGamesEvent(Feedback.SUCCESS, collect, "You do not have any available games.");
        return new GetMyOfflineGamesEvent(Feedback.SUCCESS, collect, "Choose one of the available games:");
    }

    /**
     * Handles the event of a client disconnecting.
     *
     * @param vv The user's VirtualView.
     */
    protected synchronized void clientDisconnected(VirtualView vv){
        if (!virtualViewAccounts.containsKey(vv))
            return;

        String username = virtualViewAccounts.get(vv).getUsername();

        for (GameController gc : gameControllers)
            if (gc.getPlayers().contains(username)){
                if (!gc.isGameStarted())
                    gc.quitLobby(vv);
                else
                    gc.disconnectFromGame(vv);

                virtualViewAccounts.remove(vv);
                return;
            }

        virtualViewAccounts.remove(vv);
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

    /**
     * Checks if the format of an input is invalid.
     *
     * @param input The input to check.
     * @return true if the format is invalid, false otherwise.
     */
    private boolean isFormatInvalid(String input) {
        return input == null || input.isEmpty() || input.contains(" ") || input.length() > 16; //FIXME config file
    }

    /**
     * @return the instance of the Controller class.
     */
    public synchronized static Controller getInstance(){
        if(controller == null)
            controller = new Controller();
        return controller;
    }

    /**
     * @return A map of VirtualView and Account pairs representing the users currently logged in.
     */
    protected synchronized Map<VirtualView, Account> getVirtualViewAccounts() {
        return this.virtualViewAccounts;
    }

    /**
     * @return A list of GameController instances representing the currently active games.
     */
    protected synchronized List<GameController> getGameControllers() {
        return this.gameControllers;
    }
}
