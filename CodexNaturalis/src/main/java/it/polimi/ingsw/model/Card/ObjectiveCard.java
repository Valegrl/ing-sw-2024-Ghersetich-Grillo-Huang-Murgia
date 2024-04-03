package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.List;
import java.util.Map;

/**
 * A class to represent a specific type of card, an Objective card.
 */
public class ObjectiveCard extends EvaluableCard {
	/**
	 * The list of pairs of coordinates and items required to score points in a game.
	 */
	private final List<Pair<Coordinate, Item>> requiredPattern;

	/**
	 * The numbers of Items of each Resource required to score points in a game.
	 */
	private final Map<Item, Integer> requiredItems;

	/**
	 * Constructs a new Objective card.
	 * @param id A unique integer associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 * @param requiredPattern The list of pairs of coordinates and items required to score points in a game.
	 * @param requiredItems The numbers of Items of each Resource required to score points in a game.
	 */
	public ObjectiveCard(int id, Evaluator evaluator, int points, List<Pair<Coordinate, Item>> requiredPattern, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points);
		this.requiredPattern = requiredPattern;
		this.requiredItems = requiredItems;
	}

	/**
	 * Retrieves the list of pairs of coordinates and items required to score the Objective card points.
	 * @return {@link ObjectiveCard#requiredPattern}.
	 */
	public List<Pair<Coordinate, Item>> getRequiredPattern() {
		return requiredPattern;
	}

	/**
	 * Retrieves the number of Items of each Resource required to score points.
	 * @return {@link ObjectiveCard#requiredItems}.
	 */
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}
}
