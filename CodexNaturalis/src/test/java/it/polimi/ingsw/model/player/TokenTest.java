package it.polimi.ingsw.model.player;

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
        assertEquals("red", Token.RED.toString());
        assertEquals("blue", Token.BLUE.toString());
        assertEquals("green", Token.GREEN.toString());
        assertEquals("yellow", Token.YELLOW.toString());
    }
}