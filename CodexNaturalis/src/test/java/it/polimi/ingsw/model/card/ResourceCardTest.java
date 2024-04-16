package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    private ResourceCard resourceCard;
    private Evaluator evaluator;
    private Item permanentResource;
    private Item[] corners;

    @BeforeEach
    void setUp() {
        evaluator = Mockito.mock(Evaluator.class);
        permanentResource = Mockito.mock(Item.class);
        corners = new Item[]{Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class)};
        resourceCard = new ResourceCard("1", evaluator, 10, permanentResource, corners);
    }

    @Test
    void testConstructor() {
        assertEquals("1", resourceCard.getId());
        assertEquals(evaluator, resourceCard.getEvaluator());
        assertEquals(10, resourceCard.getPoints());
        assertEquals(permanentResource, resourceCard.getPermanentResource());
        assertArrayEquals(corners, resourceCard.getCorners());
        assertEquals(CardType.RESOURCE, resourceCard.getCardType());
    }

    @Test
    void testGetPermanentResource() {
        assertEquals(permanentResource, resourceCard.getPermanentResource());
    }

    @Test
    void testGetCorners() {
        assertArrayEquals(corners, resourceCard.getCorners());
    }

    @Test
    void testGetCardType() {
        assertEquals(CardType.RESOURCE, resourceCard.getCardType());
    }
}