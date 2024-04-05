package it.polimi.ingsw.model.Evaluator;

import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Player.PlayArea;
import it.polimi.ingsw.model.exceptions.NoCoveredCardsException;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

/**
 * A class that evaluates a card that gives points based on the corners it covers.
 */
public class CornerEvaluator extends Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) throws NoCoveredCardsException {

        int countCorners = 0;

        Pair<Coordinate, EvaluableCard> card = playArea.getSelectedCard();
        Coordinate[] corners = playArea.newlyCoveredCards(card.getKey());

        for (Coordinate c : corners){
            if(c!=null)
                countCorners++;
        }

        return (card.getValue().getPoints()) * countCorners;
    }
}
