package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.player.PlayArea;

/**
 * An interface that evaluates the points a player receives from a card by verifying a specific condition.
 */
public interface Evaluator {

    /**
     * It calculates the points that a player receives from the selected card in his PlayArea.
     *
     * @param playArea Where points are calculated.
     * @return Calculated points.
     */
    int calculatePoints(PlayArea playArea);

}
