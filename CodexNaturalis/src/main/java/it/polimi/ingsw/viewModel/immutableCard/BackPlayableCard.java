package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.PlayableCard;

import java.io.Serializable;

/**
 * Represents the back of a playable card in the game. This class is used to hide the details of a card from opponents.
 * It only exposes the card type and the permanent resource of the card.
 */
public class BackPlayableCard implements Serializable {
    /**
     * The card's type.
     */
    private final CardType cardType;

    /**
     * The permanent resource ({@link Item}) of the card.
     */
    private final Item item;

    /**
     * Constructs a new BackPlayableCard with the specified card type and item.
     *
     * @param c The original PlayableCard from which to create the BackPlayableCard.
     */
    public BackPlayableCard(PlayableCard c) {
        this.cardType = c.getCardType();
        this.item = c.getPermanentResource();
    }

    /**
     * Retrieves the card's type.
     * @return {@link BackPlayableCard#cardType}.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Retrieves the card's permanent resource.
     * @return {@link BackPlayableCard#item}.
     */
    public Item getItem() {
        return item;
    }
}
