package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

import java.util.Map;

/**
 * A class to represent a specific type of card, the Gold card.
 */
public class GoldCard extends PlayableCard {
	/**
	 * The required items to place a card in the play area. Each type of item required is mapped to an integer, that represents how many items of said type are required.
	 */
	private final Map<Item, Integer> constraint;

	/**
	 * The condition required to get points when a gold card is placed in the play area.
	 */
	private final Map<Item, Integer> requiredItems;

	/**
	 * Constructs a new Evaluable card.
	 * @param id A unique String associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 * @param permanentResource The fixed resource of a Playable card, valid through the entire game.
	 * @param constraint The number of each resource required to place a Gold card.
	 * @param requiredItems The condition required to get points when placing a Gold card.
	 */
	public GoldCard(String id, Evaluator evaluator, int points, Item permanentResource, Map<Item, Integer> constraint, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points, permanentResource, CardType.GOLD);
		this.constraint = constraint;
		this.requiredItems = requiredItems;
	}

	@Override
	public Map<Item, Integer> getConstraint() {
		return constraint;
	}

	@Override
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}
}
