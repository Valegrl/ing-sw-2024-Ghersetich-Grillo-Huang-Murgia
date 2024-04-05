package it.polimi.ingsw.model.Evaluator;

import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * A class that evaluates a card that gives points based on the corners it covers.
 */
public class CornerEvaluator extends Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) {

        int countCorners = 0;

        Coordinate[] corners = {
                new Coordinate(-1,1),
                new Coordinate(1,1),
                new Coordinate(1,-1),
                new Coordinate(-1,-1)
        };
        Coordinate start = new Coordinate(0,0);
        Coordinate check;

        Pair<Coordinate, EvaluableCard> card = playArea.getSelectedCard();
        Map<Coordinate, PlayableCard> playedCards = playArea.getPlayedCards();


        for (Coordinate corner : corners) {
            check = card.getKey().add(corner);
            if (check.equals(start) || playedCards.containsKey(check))
                countCorners++;
        }

        return (card.getValue().getPoints()) * countCorners;
    }
}
