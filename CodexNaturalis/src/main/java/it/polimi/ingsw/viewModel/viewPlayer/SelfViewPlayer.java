package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

/**
 * Immutable representation of the self-{@link Player}.
 */
public class SelfViewPlayer extends AbstractViewPlayer implements Serializable {
    /**
     * The self-play area of the player.
     */
    private SelfViewPlayArea playArea;

    /**
     * The secret objective card of the player.
     */
    private ImmObjectiveCard secretObjective;

    /**
     * Constructs an immutable representation of the given {@link Player}.
     *
     * @param player The player to represent.
     */
    public SelfViewPlayer(Player player) {
        super(player);
        this.playArea = new SelfViewPlayArea(player.getPlayArea());
    }

    /**
     * Retrieves the self-play area of the player.
     * @return {@link SelfViewPlayer#playArea}.
     */
    public SelfViewPlayArea getPlayArea() {
        return playArea;
    }

    /**
     * Retrieves the secret objective card of the player.
     * @return {@link SelfViewPlayer#secretObjective}.
     */
    public ImmObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * Sets the self-play area of the player.
     * @param playArea The play area to set.
     */
    public void setPlayArea(PlayArea playArea) {
        this.playArea = new SelfViewPlayArea(playArea);
    }

    /**
     * Sets the secret objective card of the player.
     * @param secretObjective The secret objective card to set.
     */
    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = new ImmObjectiveCard(secretObjective);
    }
}
