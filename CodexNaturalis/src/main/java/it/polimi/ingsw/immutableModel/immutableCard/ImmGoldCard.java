package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Map;

/**
 * This class represents an immutable version of a GoldCard.
 * It extends the ImmPlayableCard class and adds additional properties and methods related to the gold card.
 * The class is final, so it can't be extended.
 */
public final class ImmGoldCard extends ImmPlayableCard {
    /**
     * The constraint is a map where the keys are Items and the values are the quantity of each item required as
     * a constraint.
     */
    private final Map<Item, Integer> constraint;

    /**
     * The requiredItems is a map where the keys are Items and the values are the quantity of each item required.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of a gold card.
     * This constructor takes a GoldCard object as an argument and extracts its properties to create an
     * ImmGoldCard object.
     *
     * @param goldCard the gold card to represent
     */
    public ImmGoldCard(GoldCard goldCard) {
        super(goldCard);
        this.constraint = Map.copyOf(goldCard.getConstraint());
        this.requiredItems = Map.copyOf(goldCard.getRequiredItems());
    }

    @Override
    public Map<Item, Integer> getConstraint() {
        return Map.copyOf(constraint);
    }
    @Override
    public Map<Item, Integer> getRequiredItems() {
        return Map.copyOf(requiredItems);
    }
}
