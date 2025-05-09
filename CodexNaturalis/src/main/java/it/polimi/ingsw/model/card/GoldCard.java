package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;

import java.util.Map;

/**
 * A class to represent a specific type of card, the Gold card.
 * This card can be placed only if its constraint is validated.
 * After placing the card, it can assign points based on its requirements.
 */
public class GoldCard extends PlayableCard {
    /**
     * The visible resources needed in the play area to be able to place the card.
     * Each value of the Map is > 0, because it only contains Items that are necessary for the constraint.
     */
    private final Map<Item, Integer> constraint;

    /**
     * The Items required to score points when a gold card is placed in the play area.
     * Each value of the Map is > 0, because it only contains Items that are necessary to achieve the "objective".
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs a new Gold card.
     *
     * @param id                A unique String associated with each card.
     * @param evaluator         The card's specific {@link Evaluator evaluator}.
     * @param points            The number of points associated with each card.
     * @param permanentResource The permanent resource of a Playable card.
     * @param corners           The items contained on each corner of the card.
     * @param constraint        The visible resources needed in the play area to be able to place the card.
     * @param requiredItems     The Items required to get points when placing a Gold card.
     */
    public GoldCard(String id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, Map<Item, Integer> constraint, Map<Item, Integer> requiredItems) {
        super(id, evaluator, points, permanentResource, corners, CardType.GOLD);
        this.constraint = constraint;
        this.requiredItems = requiredItems;
    }

    /**
     * Retrieves the Map representing this GoldCard's constraint.
     *
     * @return {@link GoldCard#constraint}.
     */
    @Override
    public Map<Item, Integer> getConstraint() {
        return constraint;
    }

    /**
     * Retrieves the Map representing this GoldCard's required {@link Item Items} to assign points.
     *
     * @return {@link GoldCard#requiredItems}.
     */
    @Override
    public Map<Item, Integer> getRequiredItems() {
        return requiredItems;
    }
}
