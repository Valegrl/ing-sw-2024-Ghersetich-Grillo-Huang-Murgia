package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void sumCoordinates() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 4);
        Coordinate result = c1.sum(c2);

        assertEquals(4, result.getX());
        assertEquals(6, result.getY());
    }

    @Test
    void sumCoordinatesWithZero() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(0, 0);
        Coordinate result = c1.sum(c2);

        assertEquals(1, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void getX() {
        Coordinate c = new Coordinate(1, 2);
        assertEquals(1, c.getX());
    }

    @Test
    void getY() {
        Coordinate c = new Coordinate(1, 2);
        assertEquals(2, c.getY());
    }

    @Test
    void equalsSameObject() {
        Coordinate c1 = new Coordinate(1, 2);
        assertTrue(c1.equals(c1));
    }

    @Test
    void equalsDifferentObject() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        assertTrue(c1.equals(c2));
    }

    @Test
    void equalsDifferentObjectDifferentValues() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 4);
        assertFalse(c1.equals(c2));
    }

    @Test
    void equalsNull() {
        Coordinate c1 = new Coordinate(1, 2);
        assertFalse(c1.equals(null));
    }

    @Test
    void hashCodeTest() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}