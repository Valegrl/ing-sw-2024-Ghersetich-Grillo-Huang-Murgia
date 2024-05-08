package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class ViewPlayer extends AbstractViewPlayer implements Serializable {
    /**
     * The play area of the player.
     */
    private ViewPlayArea playArea;

    /**
     * Constructs an immutable representation of a player.
     *
     * @param player the player to represent
     */
    public ViewPlayer(Player player) {
        super(player);
        this.playArea = new ViewPlayArea(player.getPlayArea());
    }

    /**
     * The play area is represented as an instance of the ImmPlayArea class, which includes the player's hand, start
     * card, played cards, uncovered items, and selected card.
     *
     * @return the play area of the player
     */
    public ViewPlayArea getPlayArea() {
        return playArea;
    }

    public void setPlayArea(PlayArea playArea) {
        this.playArea = new ViewPlayArea(playArea);
    }
}
