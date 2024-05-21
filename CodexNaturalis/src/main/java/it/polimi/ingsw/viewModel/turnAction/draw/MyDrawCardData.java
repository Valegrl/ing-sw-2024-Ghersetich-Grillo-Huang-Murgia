package it.polimi.ingsw.viewModel.turnAction.draw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmGoldCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmResourceCard;

import java.util.List;

/**
 * The MyDrawCardData class represents the data associated with the event of the current player drawing a card.
 * It includes the game status, a flag indicating if the last circle was detected, the top card of the resource deck,
 * the top card of the gold deck, the visible resource and gold cards, and the hand of the current player.
 */
public class MyDrawCardData extends DrawCardData {

    /**
     * The hand of the current player.
     */
    private final List<ImmPlayableCard> hand;

    /**
     * Constructs a new MyDrawCardData with the given game model and username.
     * It initializes the game status, detected last circle flag, top cards of the decks, visible cards, and the hand
     * of the current player.
     *
     * @param model The game model.
     * @param username The username of the current player.
     */
    public MyDrawCardData(Game model, String username) {
        super(model);
        this.hand = model.getPlayerFromUsername(username).getPlayArea()
                .getHand().stream()
                .map(card -> {
                    if (card.getCardType().equals(CardType.GOLD)) {
                        return new ImmGoldCard((GoldCard) card);
                    } else if (card.getCardType().equals(CardType.RESOURCE)) {
                        return new ImmResourceCard((ResourceCard) card);
                    } else {
                        return new ImmPlayableCard(card);
                    }
                })
                .toList();
    }

    /**
     * @return The hand of the current player.
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }
}
