package it.polimi.ingsw.model.Evaluator;

import it.polimi.ingsw.model.Player.PlayArea;
import it.polimi.ingsw.model.exceptions.NoCoveredCardsException;

/**
 * A class that evaluates the points a player receives from a card.
 */
public abstract class Evaluator {

    /**
     * It calculates the points that a player receives from the selected card in his PlayArea.
     *
     * @param playArea Where points are calculated.
     * @return Calculated points.
     */
    public abstract int calculatePoints(PlayArea playArea) throws NoCoveredCardsException;

}
