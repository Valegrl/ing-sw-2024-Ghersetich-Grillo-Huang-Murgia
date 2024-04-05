package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * A class to represent an Evaluable card, i.e., an Objective, a Gold or a Resource card.
 */
public abstract class EvaluableCard extends Card {
    /**
     * The card's specific {@link Evaluator evaluator}.
     */
    private final Evaluator evaluator;

    /**
     * The amount of points associated with each and every card.
     */
    private final int points;

    /**
     * Constructs a new Evaluable card.
     *
     * @param id        A unique String associated with each card.
     * @param evaluator The card's specific {@link Evaluator evaluator}.
     * @param points    The number of points associated with each card.
     */
    public EvaluableCard(String id, Evaluator evaluator, int points) {
        super(id);
        this.evaluator = evaluator;
        this.points = points;
    }

    /**
     * Retrieves the evaluator belonging to the specific type of card.
     *
     * @return The card's specific {@link Evaluator evaluator}.
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Retrieves the points associated with an Evaluable card.
     *
     * @return The number of points associated with each and every Evaluable card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Retrieves the array of pairs of coordinates and items required to score points.
     */
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return null;
    }

    /**
     * Retrieves the number of Items of each Resource required to score points.
     */
    public Map<Item, Integer> getRequiredItems() {
        return null;
    }


}

