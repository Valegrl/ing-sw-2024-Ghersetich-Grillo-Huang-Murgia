package it.polimi.ingsw.model.player;

import it.polimi.ingsw.utils.AnsiCodes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testFromStringValidColor() {
        assertEquals(Token.RED, Token.fromString("red"));
        assertEquals(Token.BLUE, Token.fromString("blue"));
        assertEquals(Token.GREEN, Token.fromString("green"));
        assertEquals(Token.YELLOW, Token.fromString("yellow"));
    }

    @Test
    void testFromStringInvalidColor() {
        assertNull(Token.fromString("invalidColor"));
    }

    @Test
    void testGetColor() {
        assertEquals("red", Token.RED.getColor());
        assertEquals("blue", Token.BLUE.getColor());
        assertEquals("green", Token.GREEN.getColor());
        assertEquals("yellow", Token.YELLOW.getColor());
    }

    @Test
    void testToString() {
        assertEquals(AnsiCodes.RED_TOKEN + "red" + AnsiCodes.RESET, Token.RED.toString());
        assertEquals(AnsiCodes.BLUE_TOKEN + "blue" + AnsiCodes.RESET, Token.BLUE.toString());
        assertEquals(AnsiCodes.GREEN_TOKEN + "green" + AnsiCodes.RESET, Token.GREEN.toString());
        assertEquals(AnsiCodes.YELLOW_TOKEN + "yellow" + AnsiCodes.RESET, Token.YELLOW.toString());
    }
}