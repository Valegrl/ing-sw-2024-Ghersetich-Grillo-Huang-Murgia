package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.EvaluableCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemEvaluatorTest {

    @Test
    void calculatePoints() {
        PlayArea playArea = Mockito.mock(PlayArea.class);

        EvaluableCard evaluableCard = Mockito.mock(EvaluableCard.class);

        Pair<Coordinate, EvaluableCard> pair = new Pair<>(new Coordinate(2, 0), evaluableCard);

        Map<Item, Integer> uncoveredItems = new HashMap<>() {{
            put(Item.PLANT, 2);
            put(Item.ANIMAL, 0);
            put(Item.FUNGI, 0);
            put(Item.INSECT, 0);
            put(Item.QUILL, 0);
            put(Item.INKWELL, 1);
            put(Item.MANUSCRIPT, 0);
        }};

        Map<Item, Integer> requiredItems = new HashMap<>(){{
            put(Item.PLANT, 1);
            put(Item.INKWELL, 1);
        }};

        when(playArea.getSelectedCard()).thenReturn(pair);
        when(playArea.getUncoveredItems()).thenReturn(uncoveredItems);
        when(evaluableCard.getRequiredItems()).thenReturn(requiredItems);
        when(evaluableCard.getPoints()).thenReturn(5);

        ItemEvaluator itemEvaluator = new ItemEvaluator();

        int points = itemEvaluator.calculatePoints(playArea);

        assertEquals(5, points);

        verify(playArea, times(1)).getSelectedCard();
        verify(playArea, times(1)).getUncoveredItems();
        verify(evaluableCard, times(1)).getRequiredItems();
        verify(evaluableCard, times(1)).getPoints();
    }
}