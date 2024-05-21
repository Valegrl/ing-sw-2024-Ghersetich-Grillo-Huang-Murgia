package it.polimi.ingsw.viewModel.turnAction.draw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.viewModel.immutableCard.BackPlayableCard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The OtherDrawCardData class represents the data associated with the event of another player drawing a card.
 * It includes the game status, a flag indicating if the last circle was detected, the top card of the resource deck,
 * the top card of the gold deck, the visible resource and gold cards, and the hand of the other player.
 */
public class OtherDrawCardData extends DrawCardData{

    /**
     * The username of the opponent.
     */
    private final String opponent;

    /**
     * The hand of the opponent.
     */
    private final List<BackPlayableCard> hand;

    /**
     * Constructs a new OtherDrawCardData with the given game model and username.
     * It initializes the game status, detected last circle flag, top cards of the decks, visible cards, and the hand
     * of the other player.
     *
     * @param model The game model.
     * @param username The username of the other player.
     */
    public OtherDrawCardData(Game model, String username) {
        super(model);
        this.opponent = username;
        this.hand = model.getPlayerFromUsername(username).getPlayArea()
                .getHand().stream()
                .map(BackPlayableCard::new)
                .collect(Collectors.toList());
    }

    /**
     * @return The username of the opponent.
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * @return The hand of the opponent.
     */
    public List<BackPlayableCard> getHand() {
        return hand;
    }
}
