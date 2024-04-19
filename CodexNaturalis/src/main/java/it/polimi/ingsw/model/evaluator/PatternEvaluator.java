package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.EvaluableCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that evaluates a card that gives points based on a placement pattern of other cards.
 */
public class PatternEvaluator implements Evaluator {

    @Override
    public int calculatePoints(PlayArea playArea) {

        int counter = 0;
        boolean cond1, cond2, cond3;

        EvaluableCard card = playArea.getSelectedCard().getValue();
        Pair<Coordinate, Item>[] pattern = card.getRequiredPattern();
        Map<Coordinate, PlayableCard> playedCards = playArea.getPlayedCards();

        Set<Coordinate> keys = playedCards.keySet();
        List<Coordinate> cardCoordinates = Coordinate.sortCoordinates(new ArrayList<>(keys));

        List<Coordinate> alreadyValued = new ArrayList<>();
        List<Coordinate> temp = new ArrayList<>();
        Coordinate start = new Coordinate(0,0);
        Coordinate pointer;

        for (Coordinate pos : cardCoordinates){
            boolean flag = false;
            temp.clear();
            for (int i = 0; i < pattern.length && !flag; i++) {
                pointer = pos.sum(pattern[i].getKey());
                cond1 = !pointer.equals(start);
                cond2 = !alreadyValued.contains(pointer);
                cond3 = playedCards.containsKey(pointer);
                if (cond1 && cond2 && cond3) {
                    if(pattern[i].getValue() == playedCards.get(pointer).getPermanentResource())
                        temp.add(pointer);
                    else
                        flag = true;
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