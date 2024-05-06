package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.player.PlayArea;

import java.io.Serializable;

/**
 * A class that evaluates a basic card, without sample to check.
 */
public class BasicEvaluator implements Evaluator, Serializable {

    @Override
    public int calculatePoints(PlayArea playArea) {
        return playArea.getSelectedCard().value().getPoints();
    }
}
