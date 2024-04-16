package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DeckTest {
    private Deck<Card> deck;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        card1 = Mockito.mock(Card.class);
        card2 = Mockito.mock(Card.class);

        when(card1.toString()).thenReturn("Card1");
        when(card2.toString()).thenReturn("Card2");

        LinkedList<Card> cards = new LinkedList<>();
        cards.add(card1);
        cards.add(card2);
        deck = new Deck<>(cards);
    }

    @Test
    void drawTop() {
        assertEquals(card1, deck.drawTop());
        assertEquals(1, deck.getSize());
        assertEquals(card2, deck.drawTop());
        assertEquals(0, deck.getSize());
        assertNull(deck.drawTop());
    }

    @Test
    void getSize() {
        assertEquals(2, deck.getSize());
        deck.drawTop();
        assertEquals(1, deck.getSize());
        deck.drawTop();
        assertEquals(0, deck.getSize());
    }

    @Test
    void seeTopCard() {
        assertEquals(card1, deck.seeTopCard());
        assertEquals(2, deck.getSize());
        deck.drawTop();
        assertEquals(card2, deck.seeTopCard());
        assertEquals(1, deck.getSize());
        deck.drawTop();
        assertNull(deck.seeTopCard());
    }

    @Test
    void testToString() {
        String expected = "{\"Deck\":{"
                + "        \"deck\":[" + card1.toString() + ", " + card2.toString() + "]}}";
        assertEquals(expected, deck.toString());
    }
}