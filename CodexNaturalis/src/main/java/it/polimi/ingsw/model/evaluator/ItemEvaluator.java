package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.EvaluableCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.PlayArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that evaluates a card that gives points based on the visibility of some items in the PlayArea.
 */
public class ItemEvaluator implements Evaluator, Serializable {

    @Override
    public int calculatePoints(PlayArea playArea) {

        EvaluableCard card = playArea.getSelectedCard().value();
        Map<Item, Integer> availableItems = playArea.getUncoveredItems();
        Map<Item, Integer> itemsToFind = card.getRequiredItems();

        List<Integer> proportion = new ArrayList<>();

        for (Map.Entry<Item, Integer> entry : itemsToFind.entrySet())
            proportion.add(availableItems.get(entry.getKey()) / entry.getValue()); /* entry.getValue() always >=1*/

        return (proportion.stream().mapToInt(Integer::intValue).min().orElse(0)) * card.getPoints();
    }
}
