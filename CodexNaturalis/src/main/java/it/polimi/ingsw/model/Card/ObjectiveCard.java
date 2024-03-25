package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.List;
import java.util.Map;

public class ObjectiveCard extends EvaluableCard {

    private List<Pair<Coordinate, Item>> requiredPattern;

    private Map<Item, Integer> requiredItems;

}
