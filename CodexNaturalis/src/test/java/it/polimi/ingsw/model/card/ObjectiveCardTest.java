package it.polimi.ingsw.model.card;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class ObjectiveCardTest {
    private ObjectiveCard objectiveCard;
    private Evaluator evaluator;
    private Pair<Coordinate, Item>[] requiredPattern;
    private Map<Item, Integer> requiredItems;

    @BeforeEach
    void setUp() {
        evaluator = Mockito.mock(Evaluator.class);
        requiredPattern = new Pair[]{new Pair<>(new Coordinate(0, 0), Mockito.mock(Item.class))};
        requiredItems = new HashMap<>();
        requiredItems.put(Mockito.mock(Item.class), 1);
        objectiveCard = new ObjectiveCard("1", evaluator, 10, requiredPattern, requiredItems);
    }

    @Test
    void testConstructor() {
        assertEquals("1", objectiveCard.getId());
        assertEquals(evaluator, objectiveCard.getEvaluator());
        assertEquals(10, objectiveCard.getPoints());
        assertArrayEquals(requiredPattern, objectiveCard.getRequiredPattern());
        assertEquals(requiredItems, objectiveCard.getRequiredItems());
    }

    @Test
    void testGetRequiredPattern() {
        assertArrayEquals(requiredPattern, objectiveCard.getRequiredPattern());
    }

    @Test
    void testGetRequiredItems() {
        assertEquals(requiredItems, objectiveCard.getRequiredItems());
    }
}