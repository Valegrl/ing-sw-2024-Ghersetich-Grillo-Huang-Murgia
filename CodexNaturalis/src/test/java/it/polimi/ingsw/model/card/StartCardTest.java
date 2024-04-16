package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartCardTest {
    private StartCard startCard;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = Mockito.mock(Item.class);
        item2 = Mockito.mock(Item.class);
        List<Item> backPermanentResources = Arrays.asList(item1, item2);
        startCard = new StartCard("1", backPermanentResources);
    }

    @Test
    void flipCard() {
        assertFalse(startCard.isFlipped());
        startCard.flipCard();
        assertTrue(startCard.isFlipped());
    }

    @Test
    void getBackPermanentResources() {
        List<Item> resources = startCard.getBackPermanentResources();
        assertEquals(2, resources.size());
        assertTrue(resources.contains(item1));
        assertTrue(resources.contains(item2));
    }

    @Test
    void getFrontCorners() {
        Item[] corners = startCard.getFrontCorners();
        assertEquals(4, corners.length);
    }

    @Test
    void setFrontCorner() {
        Item item3 = Mockito.mock(Item.class);
        startCard.setFrontCorner(item3, 0);
        assertEquals(item3, startCard.getFrontCorners()[0]);
    }

    @Test
    void getBackCorners() {
        Item[] corners = startCard.getBackCorners();
        assertEquals(4, corners.length);
    }

    @Test
    void setBackCorner() {
        Item item3 = Mockito.mock(Item.class);
        startCard.setBackCorner(item3, 0);
        assertEquals(item3, startCard.getBackCorners()[0]);
    }

    @Test
    void setFrontCornerOutOfBounds() {
        Item item3 = Mockito.mock(Item.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> startCard.setFrontCorner(item3, 5));
    }

    @Test
    void setBackCornerOutOfBounds() {
        Item item3 = Mockito.mock(Item.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> startCard.setBackCorner(item3, 5));
    }
}