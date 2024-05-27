package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.evaluator.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {
    private PlayableCard playableCard;
    private Evaluator evaluator;
    private Item permanentResource;
    private Item[] corners;

    @BeforeEach
    void setUp() {
        evaluator = Mockito.mock(Evaluator.class);
        permanentResource = Mockito.mock(Item.class);
        corners = new Item[]{Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class)};
        playableCard = Mockito.mock(PlayableCard.class, Mockito.withSettings().useConstructor("1",
                evaluator, 10, permanentResource, corners, CardType.GOLD).defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    @Test
    void testConstructor() {
        assertEquals("1", playableCard.getId());
        assertEquals(evaluator, playableCard.getEvaluator());
        assertEquals(10, playableCard.getPoints());
        assertEquals(permanentResource, playableCard.getPermanentResource());
        assertArrayEquals(corners, playableCard.getCorners());
        assertEquals(CardType.GOLD, playableCard.getCardType());
    }

    @Test
    void testGetPermanentResource() {
        assertEquals(permanentResource, playableCard.getPermanentResource());
    }

    @Test
    void testGetCorners() {
        assertArrayEquals(corners, playableCard.getCorners());
    }

    @Test
    void testGetCardType() {
        assertEquals(CardType.GOLD, playableCard.getCardType());
    }

    @Test
    void testFlipCard() {
        playableCard.flipCard();
        for (Item corner : playableCard.getCorners()) {
            assertEquals(Item.EMPTY, corner);
        }
    }

    @Test
    void testSetCorner() {
        Item newItem = Mockito.mock(Item.class);
        playableCard.setCorner(newItem, 0);
        assertEquals(newItem, playableCard.getCorners()[0]);
    }

    @Test
    void testSetCornerOutOfBounds() {
        Item newItem = Mockito.mock(Item.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> playableCard.setCorner(newItem, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> playableCard.setCorner(newItem, 4));
    }

    @Test
    void testGetConstraint() {
        assertNull(playableCard.getConstraint());
    }

    @Test
    void testIsFlipped() {
        Evaluator evaluator = Mockito.mock(Evaluator.class);
        Item permanentResource = Mockito.mock(Item.class);
        Item[] corners = new Item[]{Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class), Mockito.mock(Item.class)};
        PlayableCard card = Mockito.mock(PlayableCard.class, Mockito.withSettings().useConstructor("1",
                evaluator, 10, permanentResource, corners, CardType.GOLD).defaultAnswer(Mockito.CALLS_REAL_METHODS));

        assertFalse(card.isFlipped());
        card.flipCard();
        assertTrue(card.isFlipped());
    }
}