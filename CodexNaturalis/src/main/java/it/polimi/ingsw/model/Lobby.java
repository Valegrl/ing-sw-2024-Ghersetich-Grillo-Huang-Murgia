package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.InsufficientPlayersException;
import it.polimi.ingsw.model.exceptions.NonUniqueUsernameException;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a Lobby for a multiplayer game.
 */
public class Lobby {
    /**
     * The Lobby's identifier.
     */
    private final int id;

    /**
     * The list of Players' usernames that joined the Lobby.
     */
    private final List<String> joinedPlayers;

    /**
     * The minimum number of players required for this Lobby's game to start.
     */
    private final int requiredPlayers;

    /**
     * Constructs a new Lobby with the given id, first player's username and the number of required players.
     * @param id The identifier of the lobby.
     * @param user The first player's username.
     * @param requiredPlayers The number of required players to start this Lobby's game.
     */
    public Lobby(int id, String user, int requiredPlayers) {
        if (user == null)
            throw new NullPointerException("User name cannot be null");
        if (requiredPlayers < 2 || requiredPlayers > 4)
            throw new IllegalArgumentException("Number of required players must be between 2 and 4");
        this.id = id;
        this.joinedPlayers = new ArrayList<>();
        this.joinedPlayers.add(user);
        this.requiredPlayers = requiredPlayers;
    }

    /**
     * Adds a player's username to the list of {@link Lobby#joinedPlayers} in the lobby.
     * If the lobby is already full, or if the username is not unique in the lobby,
     * corresponding exceptions will be thrown.
     *
     * @param user The username of the player to be added.
     * @return true if the player's username was successfully added to the list of joined players, false otherwise.
     * @throws NonUniqueUsernameException If the username is not unique in the lobby.
     * @throws FullLobbyException         If the lobby is already full.
     */
    public boolean addPlayer(String user) throws NonUniqueUsernameException, FullLobbyException {
        if (user == null)
            throw new NullPointerException("User name cannot be null");
        if (requiredPlayers == joinedPlayers.size())
            throw new FullLobbyException();
        if (joinedPlayers.contains(user))
            throw new NonUniqueUsernameException();
        return this.joinedPlayers.add(user);
    }

    /**
     * Removes a player's username from the list of {@link Lobby#joinedPlayers} in the lobby.
     * Returns true if the player's username was present in the joined players list
     * and successfully removed, otherwise returns false.
     *
     * @param user The username of the player to be removed.
     * @return true if the player's username was present and successfully removed, otherwise false.
     */
    public boolean removePlayer(String user) {
        if (user == null)
            throw new NullPointerException("User name cannot be null");
        return joinedPlayers.remove(user);
    }

    /**
     * Initiates the {@link Game} within the lobby and returns a reference to the newly created game.
     *
     * @return A reference to the newly created {@link Game}.
     * @throws InsufficientPlayersException If the lobby does not have enough players to start the game.
     */
    public Game startGame() throws InsufficientPlayersException {
        if (requiredPlayers > joinedPlayers.size())
            throw new InsufficientPlayersException();
        return new Game(this.id, joinedPlayers);
    }

    /**
     * Retrieves the list of joined players in the Lobby.
     * @return {@link Lobby#joinedPlayers}.
     */
    public List<String> getJoinedPlayers() {
        return new ArrayList<>(joinedPlayers);
    }

    /**
     * Retrieves the number of required players to start this Lobby's game.
     * @return {@link Lobby#requiredPlayers}.
     */
    public int getRequiredPlayers() {
        return requiredPlayers;
    }

}
