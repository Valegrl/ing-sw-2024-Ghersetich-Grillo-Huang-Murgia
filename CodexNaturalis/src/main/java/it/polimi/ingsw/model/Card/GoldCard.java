package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

import java.util.Map;

/**
 * A class to represent a specific type of card, the Gold card.
 */
public class GoldCard extends PlayableCard {
	/**
	 * The condition required to get points when a gold card is placed in the play area.
	 */
	private final Map<Item, Integer> constraint;

	/**
	 * The required items to place a card in the play area. Each type of item required is mapped to an integer, that represents how many items of said type are required.
	 */
	private final Map<Item, Integer> requiredItems;

	/**
	 * Constructs a new Evaluable card.
	 * @param id A unique integer associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 * @param permanentResource The fixed resource of a Playable card, valid through the entire game.
	 * @param hasConstraint The boolean showing if a Playable card has a constraint to get points or not.
	 * @param constraint The condition required to get points when placing a Gold card.
	 * @param requiredItems The number of each resource required to place a Gold card.
	 */
	public GoldCard(int id, Evaluator evaluator, int points, Item permanentResource, boolean hasConstraint, Map<Item, Integer> constraint, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points, permanentResource, hasConstraint);
		this.constraint = constraint;
		this.requiredItems = requiredItems;
	}

	/**
	 * Retrieves the condition required to get points when a gold card is placed in the play area.
	 * @return {@link GoldCard#constraint}.
	 */
	public Map<Item, Integer> getConstraint() {
		return constraint;
	}

	/**
	 * Retrieves the number of occurrences of each resource to be able to score points.
	 * @return {@link GoldCard#requiredItems}.
	 */
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}
}
