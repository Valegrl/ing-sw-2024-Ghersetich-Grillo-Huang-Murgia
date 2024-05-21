package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmGoldCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmResourceCard;

/**
 * This interface provides a method to convert a PlayableCard into an ImmutableCard.
 */
public interface CardConverter {

    /**
     * Converts a PlayableCard into an ImmutableCard based on the card's type.
     *
     * @param card The PlayableCard to be converted.
     * @return The converted ImmutableCard.
     * @throws RuntimeException if the card type is not GOLD or RESOURCE.
     */
    default ImmPlayableCard convertToImmCardType(PlayableCard card) {
        if (card != null) {
            if (card.getCardType().equals(CardType.GOLD)) {
                return new ImmGoldCard((GoldCard) card);
            } else if (card.getCardType().equals(CardType.RESOURCE)) {
                return new ImmResourceCard((ResourceCard) card);
            } else {
                throw new RuntimeException("Error with card type.");
            }
        } else
            return null;
    }
}
