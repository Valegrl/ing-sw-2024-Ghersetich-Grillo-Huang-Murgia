package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.player.PlayArea;

/**
 * A class that evaluates the points a player receives from a card by verifying a specific condition.
 */
public abstract class Evaluator {

    /**
     * It calculates the points that a player receives from the selected card in his PlayArea.
     *
     * @param playArea Where points are calculated.
     * @return Calculated points.
     */
    public abstract int calculatePoints(PlayArea playArea);

}
