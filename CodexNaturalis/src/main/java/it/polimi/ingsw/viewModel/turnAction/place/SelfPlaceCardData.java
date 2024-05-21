package it.polimi.ingsw.viewModel.turnAction.place;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;

/**
 * The SelfPlaceCardData class represents the data associated with the event of the current player placing a card.
 */
public class SelfPlaceCardData extends PlaceCardData {

    /**
     * The play area of the player.
     */
    private final SelfViewPlayArea playArea;

    /**
     * Constructs a new SelfPlaceCardData with the given game model and username.
     *
     * @param model The game model.
     * @param username The username of the player.
     */
    public SelfPlaceCardData(Game model, String username) {
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
