package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class SelfViewPlayer extends AbstractViewPlayer implements Serializable {

    private SelfViewPlayArea playArea;

    private ImmObjectiveCard secretObjective;

    /**
     * Constructs an immutable representation of a player.
     *
     * @param player the player to represent
     */
    public SelfViewPlayer(Player player) {
        super(player);
        this.playArea = new SelfViewPlayArea(player.getPlayArea());
    }

    public SelfViewPlayArea getPlayArea() {
        return playArea;
    }

    public ImmObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public void setPlayArea(PlayArea playArea) {
        this.playArea = new SelfViewPlayArea(playArea);
    }

    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = new ImmObjectiveCard(secretObjective);
    }
}
