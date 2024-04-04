package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.List;
import java.util.Map;

/**
 * A class to represent a specific type of {@link Card}, an Objective card.
 * This type of card assigns points each time its requirement is accomplished.
 */
public class ObjectiveCard extends EvaluableCard {
	/**
	 * A list of {@link Pair Pairs} representing the pattern required to assign points.
	 */
	private final List<Pair<Coordinate, Item>> requiredPattern;

	/**
	 * A Map associating Items to the number of required visible resources of that type
	 * in the {@link it.polimi.ingsw.model.Player.Player Player}'s {@link it.polimi.ingsw.model.Player.PlayArea PlayArea} to assign points.
	 */
	private final Map<Item, Integer> requiredItems;

	/**
	 * Constructs a new Objective card.
	 *
	 * @param id A unique String associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The number of points associated with each card.
	 * @param requiredPattern The list of {@link Pair Pairs} representing the pattern required to assign points.
	 * @param requiredItems The Map of required Items to score the number of points associated with the card.
	 */
	public ObjectiveCard(String id, Evaluator evaluator, int points, List<Pair<Coordinate, Item>> requiredPattern, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points);
		this.requiredPattern = requiredPattern;
		this.requiredItems = requiredItems;
	}

	/**
	 * Retrieves the required pattern to score points.
	 * @return {@link ObjectiveCard#requiredPattern}.
	 */
	@Override
	public List<Pair<Coordinate, Item>> getRequiredPattern() {
		return requiredPattern;
	}

	/**
	 * Retrieves the map of required items to score points.
	 * @return {@link ObjectiveCard#requiredItems}.
	 */
	@Override
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}
}
