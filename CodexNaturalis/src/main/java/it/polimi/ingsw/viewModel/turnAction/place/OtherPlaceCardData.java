package it.polimi.ingsw.viewModel.turnAction.place;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.viewPlayer.ViewPlayArea;

/**
 * The OtherPlaceCardData class represents the data associated with the event of another player placing a card.
 */
public class OtherPlaceCardData extends PlaceCardData {

    /**
     * The username of the opponent.
     */
    private final String opponent;

    /**
     * The play area of the opponent.
     */
    private final ViewPlayArea opponentPlayArea;

    /**
     * Constructs a new OtherPlaceCardData with the given game model and username.
     *
     * @param model The game model.
     * @param username The username of the opponent.
     */
    public OtherPlaceCardData(Game model, String username) {
        super(model);
        this.opponent = username;
        this.opponentPlayArea = new ViewPlayArea(model.getPlayerFromUsername(username).getPlayArea());
    }

    /**
     * @return The username of the opponent.
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * @return The play area of the opponent.
     */
    public ViewPlayArea getOpponentPlayArea() {
        return opponentPlayArea;
    }
}
