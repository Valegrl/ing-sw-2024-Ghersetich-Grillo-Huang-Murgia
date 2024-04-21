package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Collections;
import java.util.List;

/**
 * This class represents an immutable version of a StartCard.
 * It extends the ImmCard class and adds additional properties and methods related to the start card.
 */
public final class ImmStartCard extends ImmCard {
    /**
     * The backPermanentResources is a list of Items representing the resources that the card provides permanently
     * on its back side.
     */
    private final List<Item> backPermanentResources;

    /**
     * The frontCorners is an array of Items representing the resources located at the corners of the card's front side.
     */
    private final Item[] frontCorners;

    /**
     * The backCorners is an array of Items representing the resources located at the corners of the card's back side.
     */
    private final Item[] backCorners;

    /**
     * The flipped is a boolean indicating whether the card is flipped or not.
     */
    private final boolean flipped;

    /**
     * Constructs an immutable representation of a start card.
     * This constructor takes a StartCard object as an argument and extracts its properties to create an ImmStartCard
     * object.
     *
     * @param startCard the start card to represent
     */
    public ImmStartCard(StartCard startCard) {
        super(startCard);
        this.backPermanentResources = Collections.unmodifiableList(startCard.getBackPermanentResources());
        this.frontCorners = startCard.getFrontCorners().clone();
        this.backCorners = startCard.getBackCorners().clone();
        this.flipped = startCard.isFlipped();
    }

    /**
     * This method allows access to the list of back permanent resources of the card, which represents the resources
     * that the card provides permanently on its back side.
     *
     * @return the back permanent resources of the card
     */
    public List<Item> getBackPermanentResources() {
        return backPermanentResources;
    }

    /**
     * This method allows access to the array of front corner resources of the card, which represents the resources
     * located at the corners of the card's front side.
     *
     * @return a copy of the array of front corner resources of the card
     */
    public Item[] getFrontCorners() {
        return frontCorners.clone();
    }

    /**
     * This method allows access to the array of back corner resources of the card, which represents the resources
     * located at the corners of the card's back side.
     *
     * @return a copy of the array of back corner resources of the card
     */
    public Item[] getBackCorners() {
        return backCorners.clone();
    }

    /**
     * This method allows access to the flipped status of the card, which indicates whether the card is flipped or not.
     *
     * @return the flipped status of the card
     */
    public boolean isFlipped() {
        return flipped;
    }
}
