package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.player.Token;

/**
 * This class represents the setup of a player's token in the game.
 * It contains the player's username, token, and a boolean value indicating whether the player has been disconnected at least once.
 */
public class PlayerTokenSetup {

    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The token of the player.
     */
    private Token token;

    /**
     * This field represents whether the player has been disconnected at least once.
     * If true, the player has been disconnected. If false, the player has not been disconnected yet.
     */
    private boolean disconnectedAtLeastOnce;

    /**
     * Constructs a new PlayerTokenSetup with the specified username.
     *
     * @param username the username of the player
     */
    public PlayerTokenSetup(String username) {
        this.username = username;
        this.token = null;
        this.disconnectedAtLeastOnce = false;
    }

    /**
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the token of the player
     */
    public Token getToken() {
        return token;
    }

    /**
     * This method sets the token of the player.
     *
     * @param token the new token to set
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * This method checks if the player has been disconnected at least once.
     *
     * @return true if the player has been disconnected at least once, false otherwise
     */
    public boolean isDisconnectedAtLeastOnce() {
        return disconnectedAtLeastOnce;
    }

    /**
     * This method sets the disconnected status of the player.
     *
     * @param disconnected the new disconnected status to set. True indicates that the player has been disconnected at
     *                     least once, false indicates the player has not been disconnected yet.
     */
    public void setDisconnected(boolean disconnected) {
        this.disconnectedAtLeastOnce = disconnected;
    }
}
