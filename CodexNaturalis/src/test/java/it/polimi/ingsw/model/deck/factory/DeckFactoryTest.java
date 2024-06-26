package it.polimi.ingsw.model.deck.factory;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.utils.JsonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DeckFactoryTest {
    private DeckFactory deckFactory;
    private ClassLoader classLoader;

    @BeforeEach
    void setUp() {
        JsonConfig.loadConfig();
        deckFactory = new DeckFactory();
        classLoader = Mockito.mock(ClassLoader.class);
    }
    @Test
    void testCreateDeck() {
        Deck<GoldCard> deck = deckFactory.createDeck(GoldCard.class);
        assertNotNull(deck);
        assertEquals(40, deck.getSize());
    }

    @Test
    void testCreateDeckWithNonExistentFile() {
        assertThrows(IllegalArgumentException.class, () -> deckFactory.createDeck(Card.class));
    }

    @Test
    void testCreateDeckWithNonExistentResource() {
        when(classLoader.getResource("NonExistentDeck.json")).thenReturn(null);
        deckFactory.classToFile.put(Card.class, "NonExistentDeck.json");

        assertThrows(IllegalArgumentException.class, () -> deckFactory.createDeck(Card.class));
    }

}