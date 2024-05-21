package it.polimi.ingsw.viewModel.turnAction.draw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;

import java.util.List;

/**
 * The SelfDrawCardData class represents the data associated with the event of the current player drawing a card.
 */
public class SelfDrawCardData extends DrawCardData {

    /**
     * The hand of the current player.
     */
    private final List<ImmPlayableCard> hand;

    /**
     * Constructs a new SelfDrawCardData with the given game model and username.
     *
     * @param model The game model.
     * @param username The username of the current player.
     */
    public SelfDrawCardData(Game model, String username) {
        super(model);
        this.hand = model.getPlayerFromUsername(username).getPlayArea()
                .getHand().stream()
                .map(this::convertToImmCardType)
                .toList();
    }

    /**
     * @return The hand of the current player.
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }
}
