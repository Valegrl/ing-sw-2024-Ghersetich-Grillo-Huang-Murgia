package it.polimi.ingsw.model.Evaluator;

import it.polimi.ingsw.model.Player.PlayArea;

/**
 * A class that evaluates a basic card, without sample to check.
 */
public class BasicEvaluator extends Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) {
        return playArea.getSelectedCard().getPoints();
    }
}
