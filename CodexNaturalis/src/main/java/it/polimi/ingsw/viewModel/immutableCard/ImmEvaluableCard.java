package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * This class represents an immutable version of an {@link EvaluableCard}.
 */
public class ImmEvaluableCard extends ImmCard {
    /**
     * The card's evaluator.
     */
    private final Evaluator evaluator;

    /**
     * The card's points.
     */
    private final int points;

    /**
     * Constructs an immutable representation of an evaluable card from the given {@link EvaluableCard}.
     *
     * @param evaluableCard The evaluable card to represent.
     */
    public ImmEvaluableCard(EvaluableCard evaluableCard) {
        super(evaluableCard);
        this.evaluator = evaluableCard.getEvaluator();
        this.points = evaluableCard.getPoints();
    }

    /**
     * Retrieves the card's evaluator.
     * @return {@link ImmEvaluableCard#evaluator}.
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Retrieves the card's points.
     * @return {@link ImmEvaluableCard#points}.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Retrieves the card's required pattern.
     * @return The required pattern to assign points.
     */
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return null;
    }

    /**
     * Retrieves the card's required items.
     * @return The required items to assign points.
     */
    public Map<Item, Integer> getRequiredItems() {
        return null;
    }
}
