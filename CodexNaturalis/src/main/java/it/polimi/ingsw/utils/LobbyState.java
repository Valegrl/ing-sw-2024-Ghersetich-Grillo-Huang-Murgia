package it.polimi.ingsw.utils;

/**
 * This class represents the state of a lobby in a game.
 */
public class LobbyState {

    /**
     * The unique identifier for a Lobby.
     */
    private final String id;

    /**
     * The number of online players in the lobby.
     */
    private final int onlinePlayers;

    /**
     * The number of required players for the lobby.
     */
    private final int requiredPlayers;

    /**
     * Constructor for the LobbyState class.
     * It initializes the id, onlinePlayers, and requiredPlayers with the provided values.
     *
     * @param id The unique identifier for a Lobby.
     * @param onlinePlayers The number of online players in the lobby.
     * @param requiredPlayers The number of required players for the lobby.
     */
    public LobbyState(String id, int onlinePlayers, int requiredPlayers) {
        this.id = id;
        this.onlinePlayers = onlinePlayers;
        this.requiredPlayers = requiredPlayers;
    }

    /**
     * @return The unique identifier for a Lobby.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The number of online players in the lobby.
     */
    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    /**
     * @return The number of required players for the lobby.
     */
    public int getRequiredPlayers() {
        return requiredPlayers;
    }
}
