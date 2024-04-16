package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvaluableCardTest {
    private EvaluableCard evaluableCard;
    private Evaluator evaluator;
    private Pair<Coordinate, Item>[] requiredPattern;
    private Map<Item, Integer> requiredItems;

    @BeforeEach
    void setUp() {
        evaluator = Mockito.mock(Evaluator.class);
        requiredPattern = new Pair[]{new Pair<>(new Coordinate(0, 0), Mockito.mock(Item.class))};
        requiredItems = new HashMap<>();
        requiredItems.put(Mockito.mock(Item.class), 1);
        evaluableCard = Mockito.mock(EvaluableCard.class, Mockito.withSettings().useConstructor("1", evaluator, 10).defaultAnswer(Mockito.CALLS_REAL_METHODS));
        Mockito.when(evaluableCard.getRequiredPattern()).thenReturn(requiredPattern);
        Mockito.when(evaluableCard.getRequiredItems()).thenReturn(requiredItems);
    }

    @Test
    void testConstructor() {
        assertEquals("1", evaluableCard.getId());
        assertEquals(evaluator, evaluableCard.getEvaluator());
        assertEquals(10, evaluableCard.getPoints());
    }

    @Test
    void testGetEvaluator() {
        assertEquals(evaluator, evaluableCard.getEvaluator());
    }

    @Test
    void testGetPoints() {
        assertEquals(10, evaluableCard.getPoints());
    }

    @Test
    void testGetRequiredPattern() {
        assertArrayEquals(requiredPattern, evaluableCard.getRequiredPattern());
    }

    @Test
    void testGetRequiredItems() {
        assertEquals(requiredItems, evaluableCard.getRequiredItems());
    }
}