package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    private GoldCard goldCard;
    private Evaluator evaluator;
    private Item permanentResource;
    private Item[] corners;
    private Map<Item, Integer> constraint;
    private Map<Item, Integer> requiredItems;

    @BeforeEach
    void setUp() {
        evaluator = Mockito.mock(Evaluator.class);
        permanentResource = Mockito.mock(Item.class);
        corners = new Item[]{Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class)};
        constraint = new HashMap<>();
        constraint.put(Mockito.mock(Item.class), 1);
        requiredItems = new HashMap<>();
        requiredItems.put(Mockito.mock(Item.class), 1);
        goldCard = new GoldCard("1", evaluator, 10, permanentResource, corners, constraint, requiredItems);
    }

    @Test
    void testConstructor() {
        assertEquals("1", goldCard.getId());
        assertEquals(evaluator, goldCard.getEvaluator());
        assertEquals(10, goldCard.getPoints());
        assertEquals(permanentResource, goldCard.getPermanentResource());
        assertArrayEquals(corners, goldCard.getCorners());
        assertEquals(CardType.GOLD, goldCard.getCardType());
        assertEquals(constraint, goldCard.getConstraint());
        assertEquals(requiredItems, goldCard.getRequiredItems());
    }

    @Test
    void testGetConstraint() {
        assertEquals(constraint, goldCard.getConstraint());
    }

    @Test
    void testGetRequiredItems() {
        assertEquals(requiredItems, goldCard.getRequiredItems());
    }
}