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
	private Map<Item, Integer> constraint;

	/**
	 * The required items to place a card in the play area. Each type of item required is mapped to an integer, that represents how many items of said type are required.
	 */
	private Map<Item, Integer> requiredItems;

	public GoldCard(int id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, boolean hasConstraint, Map<Item, Integer> constraint, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points, permanentResource, corners, hasConstraint);
		this.constraint = constraint;
		this.requiredItems = requiredItems;
	}

	public Map<Item, Integer> getConstraint() {
		return constraint;
	}

	public void setConstraint(Map<Item, Integer> constraint) {
		this.constraint = constraint;
	}

	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}

	public void setRequiredItems(Map<Item, Integer> requiredItems) {
		this.requiredItems = requiredItems;
	}

}
