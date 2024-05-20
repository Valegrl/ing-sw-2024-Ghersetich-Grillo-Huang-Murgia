package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.PlayableCard;

import java.io.Serializable;

/**
 * Represents the back of a playable card in the game. This class is used to hide the details of a card from opponents.
 * It only exposes the card type and the permanent resource (item) of the card.
 */
public class BackPlayableCard implements Serializable {
    /**
     * The type of the card.
     */
    private final CardType cardType;

    /**
     * The permanent resource (item) of the card.
     */
    private final Item item;

    /**
     * Constructs a new BackPlayableCard with the specified card type and item.
     *
     * @param c the original PlayableCard from which to create the BackPlayableCard
     */
    public BackPlayableCard(PlayableCard c) {
        this.cardType = c.getCardType();
        this.item = c.getPermanentResource();
    }

    /**
     * @return The CardType enum value representing the type of this card.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * @return The Item object representing the permanent resource of this card.
     */
    public Item getItem() {
        return item;
    }
}
