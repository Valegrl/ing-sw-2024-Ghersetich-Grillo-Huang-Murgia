package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Token;

import java.io.Serializable;

/**
 * Immutable representation of a player.
 */
public abstract class AbstractViewPlayer implements Serializable {
    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The colored {@link Token} of the player.
     */
    private Token token;

    /**
     * Boolean that indicates if the player is online.
     */
    private final boolean online;

    /**
     * Constructs an immutable representation of a {@link Player}.
     *
     * @param player The player to represent.
     */
    public AbstractViewPlayer(Player player) {
        this.username = player.getUsername();
        this.token = player.getToken();
        this.online = player.isOnline();
    }

    abstract public String playAreaToString();

    /**
     * Retrieves the username of the player.
     * @return {@link AbstractViewPlayer#username}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the colored {@link Token} of the player.
     * @return {@link AbstractViewPlayer#token}.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Retrieves the boolean that indicates if the player is online.
     * @return {@link AbstractViewPlayer#online}.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets the player's {@link Token}.
     * @param token The {@link Token} to set.
     */
    public void setToken(Token token) {
        this.token = token;
    }
}
