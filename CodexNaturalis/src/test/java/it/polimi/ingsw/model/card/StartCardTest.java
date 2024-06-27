package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartCardTest {
    private StartCard card;

    @BeforeEach
    void setUp() {
        List<Item> backPermanentResources = Arrays.asList(Item.PLANT, Item.ANIMAL);

        Item[] frontCorners = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        Item[] backCorners = new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.INSECT};
        boolean flipped = false;

        card = new StartCard("1", backPermanentResources, frontCorners, backCorners, flipped);
    }

    @Test
    void testFlipCard() {
        assertFalse(card.isFlipped());
        card.flipCard();
        assertTrue(card.isFlipped());
    }

    @Test
    void testGetBackPermanentResources() {
        List<Item> resources = Arrays.asList(Item.PLANT, Item.ANIMAL);
        assertEquals(resources, card.getBackPermanentResources());
    }

    @Test
    void testGetFrontCorners() {
        Item[] frontCorners = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        assertArrayEquals(frontCorners, card.getFrontCorners());
    }

    @Test
    void testSetFrontCornerValidIndex() {
        card.setFrontCorner(Item.FUNGI, 1);
        assertEquals(Item.FUNGI, card.getFrontCorners()[1]);
    }

    @Test
    void testSetFrontCornerInvalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setFrontCorner(Item.FUNGI, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setFrontCorner(Item.FUNGI, card.getFrontCorners().length));
    }

    @Test
    void testGetBackCorners() {
        Item[] backCorners = new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.INSECT};
        assertArrayEquals(backCorners, card.getBackCorners());
    }

    @Test
    void testSetBackCornerValidIndex() {
        card.setBackCorner(Item.FUNGI, 1);
        assertEquals(Item.FUNGI, card.getBackCorners()[1]);
    }

    @Test
    void testSetBackCornerInvalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setBackCorner(Item.FUNGI, -1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setBackCorner(Item.FUNGI, card.getBackCorners().length));
    }

    @Test
    void setFrontCornerOutOfBounds() {
        Item item3 = Mockito.mock(Item.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setFrontCorner(item3, 5));
    }

    @Test
    void setBackCornerOutOfBounds() {
        Item item3 = Mockito.mock(Item.class);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> card.setBackCorner(item3, 5));
    }
}