package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

 class TestCard extends Card {
    public TestCard(String id) {
        super(id);
    }
}

class CardTest {

    @Test
    void testGetId() {
        Card card = new TestCard("1");
        assertEquals("1", card.getId());
    }

    @Test
    void testEquals() {
        Card card1 = new TestCard("1");
        Card card2 = new TestCard("1");
        Card card3 = new TestCard("2");
        Card card4 = Mockito.mock(Card.class);

        assertEquals(card1, card1); // reflexivity
        assertEquals(card1, card2); // symmetry
        assertEquals(card2, card1); // symmetry
        assertNotEquals(card1, card3); // inequality
        assertNotEquals(card1, null); // non-nullity
        assertNotEquals(card1, card4); // different class
    }

    @Test
    void testHashCode() {
        Card card1 = new TestCard("1");
        Card card2 = new TestCard("1");
        Card card3 = new TestCard("2");

        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }
}