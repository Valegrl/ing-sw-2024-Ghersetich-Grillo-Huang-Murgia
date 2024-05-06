package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the state of a lobby in a game.
 */
public class LobbyState implements Serializable {

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

    @Override
    public String toString() {
        return id + "' " + onlinePlayers + "/" + requiredPlayers + " players.";
    }

    /**
     * Checks if this LobbyState is equal to another object.
     * An object is equal to this LobbyState if it is also a LobbyState and both have the same id, onlinePlayers, and requiredPlayers.
     *
     * @param o The object to compare this LobbyState to.
     * @return true if the other object is equal to this LobbyState, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyState that = (LobbyState) o;
        return onlinePlayers == that.onlinePlayers &&
                requiredPlayers == that.requiredPlayers &&
                Objects.equals(id, that.id);
    }

    /**
     * Generates a hash code for this LobbyState.
     * The hash code is generated based on the id, onlinePlayers, and requiredPlayers of this LobbyState.
     *
     * @return The generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, onlinePlayers, requiredPlayers);
    }
}
