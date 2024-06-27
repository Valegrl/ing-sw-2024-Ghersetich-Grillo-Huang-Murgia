package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.AnsiCodes;

/**
 * Immutable representation of a {@link Player}.
 */
public class ViewPlayer extends AbstractViewPlayer {
    /**
     * The player's play area.
     */
    private ViewPlayArea playArea;

    /**
     * Constructs an immutable representation of the given {@link Player}.
     *
     * @param player The player to represent.
     */
    public ViewPlayer(Player player) {
        super(player);
        this.playArea = new ViewPlayArea(player.getPlayArea());
    }

    /**
     * Returns a string representation of the play area of the player.
     * @return The string representation of the play area of the player.
     */
    @Override
    public String playAreaToString() {
        return AnsiCodes.BOLD + getToken().getColorCode() + getUsername() + AnsiCodes.RESET + "'s " + playArea.printPlayArea();
    }

    /**
     * Retrieves the play area of the player.
     * @return {@link ViewPlayer#playArea}.
     */
    public ViewPlayArea getPlayArea() {
        return playArea;
    }

    /**
     * Sets the play area of the player.
     * @param playArea the play area to set
     */
    public void setPlayArea(ViewPlayArea playArea) {
        this.playArea = playArea;
    }
}
