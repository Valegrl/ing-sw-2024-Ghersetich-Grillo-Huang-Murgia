package it.polimi.ingsw.model.card;

import it.polimi.ingsw.utils.AnsiCodes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

    class CardTypeTest {

        @Test
        void getType() {
            assertEquals("gold", CardType.GOLD.getType());
            assertEquals("resource", CardType.RESOURCE.getType());
        }

        @Test
        void testToString() {
            assertEquals("gold", CardType.GOLD.toString());
            assertEquals("resource", CardType.RESOURCE.toString());
        }

        @Test
        void values() {
            CardType[] expectedValues = {CardType.GOLD, CardType.RESOURCE};
            assertArrayEquals(expectedValues, CardType.values());
        }

        @Test
        void valueOf() {
            assertEquals(CardType.GOLD, CardType.valueOf("GOLD"));
            assertEquals(CardType.RESOURCE, CardType.valueOf("RESOURCE"));
        }

        @Test
        void testTypeToColor() {
            assertEquals(AnsiCodes.GOLD_BACKGROUND, CardType.GOLD.TypeToColor());
            assertEquals("", CardType.RESOURCE.TypeToColor());
        }
    }