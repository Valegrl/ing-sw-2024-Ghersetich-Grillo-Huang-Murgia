package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.player.PlayArea;

/**
 * A class that evaluates a basic card, without sample to check.
 */
public class BasicEvaluator extends Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) {
        return playArea.getSelectedCard().getValue().getPoints();
    }
}
