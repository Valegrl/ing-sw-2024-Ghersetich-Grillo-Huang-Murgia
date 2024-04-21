package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents an immutable version of a ResourceCard.
 * It extends the ImmPlayableCard class and adds additional properties and methods related to the resource card.
 * The class is final, so it can't be extended.
 */
public final class ImmResourceCard extends ImmPlayableCard {

    /**
     * Constructs an immutable representation of a resource card.
     * This constructor takes a ResourceCard object as an argument and extracts its properties to create an
     * ImmResourceCard object.
     *
     * @param resourceCard the resource card to represent
     */
    public ImmResourceCard(ResourceCard resourceCard) {
        super(resourceCard);
    }
}
