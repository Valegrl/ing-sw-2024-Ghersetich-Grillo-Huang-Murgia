package it.polimi.ingsw.model.Evaluator;

import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Card.Item;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that evaluates a card that gives points based on a placement pattern of other cards.
 */
public class PatternEvaluator extends Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) {

        int counter = 0;
        boolean cond1, cond2, cond3, cond4;

        EvaluableCard card = playArea.getSelectedCard().getValue();
        Pair<Coordinate, Item>[] pattern = card.getRequiredPattern();
        Map<Coordinate, PlayableCard> playedCards = playArea.getPlayedCards();

        List<Coordinate> alreadyValued = new ArrayList<>();
        List<Coordinate> temp = new ArrayList<>();
        Coordinate start = new Coordinate(0,0);
        Coordinate pointer;

        for (Coordinate pos : playedCards.keySet()){
            boolean flag = false;
            temp.clear();
            for (int i = 0; i < pattern.length && !flag; i++) {
                pointer = pos.add(pattern[i].getKey());
                cond1 = !pointer.equals(start);
                cond2 = !alreadyValued.contains(pointer);
                cond3 = playedCards.containsKey(pointer);
                cond4 = pattern[i].getValue() == playedCards.get(pointer).getPermanentResource();
                if (cond1 && cond2 && cond3 && cond4) {
                    temp.add(pointer);
                } else {
                    flag = true;
                }
            }
            if (!flag) {
                counter++;
                alreadyValued.addAll(temp);
            }
        }

        return (card.getPoints()) * counter;
    }
}
