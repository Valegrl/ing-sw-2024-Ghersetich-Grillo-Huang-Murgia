package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * This class represents an immutable version of an ObjectiveCard.
 * It extends the ImmEvaluableCard class and adds additional properties and methods related to the objective card.
 * The class is final, so it can't be extended.
 */
public final class ImmObjectiveCard extends ImmEvaluableCard {
    /**
     * The requiredPattern is an array of pairs, each pair consists of a Coordinate and an Item.
     * It represents the pattern that needs to be matched on the board for the card to be evaluated.
     */
    private final Pair<Coordinate, Item>[] requiredPattern;

    /**
     * The requiredItems is a map where the keys are Items and the values are the quantity of each item required.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of an objective card.
     * This constructor takes an ObjectiveCard object as an argument and extracts its properties to create an
     * ImmObjectiveCard object.
     *
     * @param objectiveCard the objective card to represent
     */
    public ImmObjectiveCard(ObjectiveCard objectiveCard) {
        super(objectiveCard);
        this.requiredPattern = objectiveCard.getRequiredPattern().clone();
        this.requiredItems = Map.copyOf(objectiveCard.getRequiredItems());
    }

    @Override
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return requiredPattern.clone();
    }

    @Override
    public Map<Item, Integer> getRequiredItems() {
        return Map.copyOf(requiredItems);
    }
}
