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

    private List<Pair<Coordinate, Item>> requiredPattern;

    private Map<Item, Integer> requiredItems;

	public ObjectiveCard(int id, Evaluator evaluator, int points, List<Pair<Coordinate, Item>> requiredPattern, Map<Item, Integer> requiredItems) {
		super(id, evaluator, points);
		this.requiredPattern = requiredPattern;
		this.requiredItems = requiredItems;
	}

	public List<Pair<Coordinate, Item>> getRequiredPattern() {
		return requiredPattern;
	}
	public void setRequiredPattern(List<Pair<Coordinate, Item>> requiredPattern) {
		this.requiredPattern = requiredPattern;
	}
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}
	public void setRequiredItems(Map<Item, Integer> requiredItems) {
		this.requiredItems = requiredItems;
	}

}
