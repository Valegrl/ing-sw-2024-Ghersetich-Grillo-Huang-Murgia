package it.polimi.ingsw.viewModel.turnAction.place;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;

/**
 * The MyPlaceCardData class represents the data associated with the event of the current player placing a card.
 * It includes the game status, a flag indicating if the last circle was detected, the play area of the player,
 * and the scoreboard.
 */
public class MyPlaceCardData extends PlaceCardData {

    /**
     * The play area of the player.
     */
    private final SelfViewPlayArea playArea;

    /**
     * Constructs a new MyPlaceCardData with the given game model and username.
     * It initializes the game status, detected last circle flag, scoreboard, and play area of the player.
     *
     * @param model The game model.
     * @param username The username of the player.
     */
    public MyPlaceCardData(Game model, String username) {
        super(model);
        this.playArea = new SelfViewPlayArea(model.getPlayerFromUsername(username).getPlayArea());
    }

    /**
     * @return The play area of the player.
     */
    public SelfViewPlayArea getPlayArea() {
        return playArea;
    }
}
