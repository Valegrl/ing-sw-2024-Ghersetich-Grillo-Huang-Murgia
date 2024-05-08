package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * This class represents an immutable version of an EvaluableCard.
 * It extends the ImmCard class and adds additional properties and methods related to the evaluation of the card.
 */
public class ImmEvaluableCard extends ImmCard {
    /**
     * The evaluator is used to evaluate the card.
     */
    private final Evaluator evaluator;

    /**
     * The points represent the score value of the card.
     */
    private final int points;

    /**
     * Constructs an immutable representation of an evaluable card.
     * This constructor takes an EvaluableCard object as an argument and extracts its properties to create an
     * ImmEvaluableCard object.
     *
     * @param evaluableCard the evaluable card to represent
     */
    public ImmEvaluableCard(EvaluableCard evaluableCard) {
        super(evaluableCard);
        this.evaluator = evaluableCard.getEvaluator();
        this.points = evaluableCard.getPoints();
    }

    /**
     * This method allows access to the evaluator of the card, which is used to evaluate the card.
     *
     * @return the evaluator of the card
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * This method allows access to the points of the card, which represent the score value of the card.
     *
     * @return the points of the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * This method allows access to the required pattern of the card, which is an array of pairs, each pair consists
     * of a Coordinate and an Item.
     * It represents the pattern that needs to be matched on the PlayArea for the card to be evaluated.
     *
     * @return a copy of the required pattern of the card
     */
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return null;
    }

    /**
     * This method allows access to the map of required items of the card, where the keys are Items and the values are
     * the quantity of each item required.
     *
     * @return a copy of the map of required items of the card
     */
    public Map<Item, Integer> getRequiredItems() {
        return null;
    }
}
