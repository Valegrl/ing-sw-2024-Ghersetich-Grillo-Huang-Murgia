package it.polimi.ingsw.immutableModel;

import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Token;

/**
 * Immutable representation of the Player class.
 * This class provides a read-only view of the player's state, which can be safely shared across different threads.
 * All fields are final and initialized at construction time, and there are no setter methods.
 * The class also provides getter methods for all fields.
 *
 * @see it.polimi.ingsw.model.player.Player
 */
public final class ImmPlayer {
    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The token of the player.
     */
    private final Token token;

    /**
     * The play area of the player.
     */
    private final ImmPlayArea playArea;

    /**
     * The secret objective of the player.
     */
    private final ImmObjectiveCard secretObjective;

    /**
     * The online status of the player.
     */
    private final boolean online;

    /**
     * Constructs an immutable representation of a player.
     *
     * @param player the player to represent
     */
    public ImmPlayer(Player player) {
        this.username = player.getUsername();
        this.token = player.getToken();
        this.playArea = new ImmPlayArea(player.getPlayArea());
        this.secretObjective = new ImmObjectiveCard(player.getSecretObjective());
        this.online = player.isOnline();
    }

    /**
     * The username is a unique string that identifies the player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }


    /**
     * The token is an instance of the Token enum, representing the player's unique token.
     *
     * @return the token of the player
     */
    public Token getToken() {
        return token;
    }

    /**
     * The play area is represented as an instance of the ImmPlayArea class, which includes the player's hand, start
     * card, played cards, uncovered items, and selected card.
     *
     * @return the play area of the player
     */
    public ImmPlayArea getPlayArea() {
        return playArea;
    }

    /**
     * The secret objective is represented as an instance of the ImmObjectiveCard class, which includes the objective's
     * name, description, and point value.
     *
     * @return the secret objective of the player
     */
    public ImmObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * The online status is a boolean value that indicates whether the player is currently online (true) or offline (false).
     *
     * @return the online status of the player
     */
    public boolean isOnline() {
        return online;
    }
}
