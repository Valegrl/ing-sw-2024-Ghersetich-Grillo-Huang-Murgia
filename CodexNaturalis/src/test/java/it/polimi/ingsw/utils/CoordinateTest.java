package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
    void testEquals() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        Coordinate c3 = new Coordinate(3, 4);
        Object o = new Object();

        assertEquals(c1, c1);
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(null, c1);
        assertNotEquals(c1, o);
    }

    @Test
    void hashCodeTest() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testSortCoordinates() {
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(3, 1);
        Coordinate c3 = new Coordinate(2, 3);
        Coordinate c4 = new Coordinate(-5, 1);

        List<Coordinate> coordinates = Arrays.asList(c1, c2, c3, c4);
        List<Coordinate> sortedCoordinates = Coordinate.sortCoordinates(coordinates);

        assertEquals(c4, sortedCoordinates.get(0));
        assertEquals(c2, sortedCoordinates.get(1));
        assertEquals(c1, sortedCoordinates.get(2));
        assertEquals(c3, sortedCoordinates.get(3));
    }

    @Test
    void testToString() {
        Coordinate c = new Coordinate(1, 2);
        assertEquals("(1, 2)", c.toString());
    }
}