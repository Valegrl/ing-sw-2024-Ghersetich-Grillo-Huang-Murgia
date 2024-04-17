package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void getKey() {
        Pair<String, Integer> pair = new Pair<>("key", 1);
        assertEquals("key", pair.getKey());
    }

    @Test
    void setKey() {
        Pair<String, Integer> pair = new Pair<>("key", 1);
        pair.setKey("newKey");
        assertEquals("newKey", pair.getKey());
    }

    @Test
    void getValue() {
        Pair<String, Integer> pair = new Pair<>("key", 1);
        assertEquals(1, pair.getValue());
    }

    @Test
    void setValue() {
        Pair<String, Integer> pair = new Pair<>("key", 1);
        pair.setValue(2);
        assertEquals(2, pair.getValue());
    }

    @Test
    void testEquals() {
        Pair<String, Integer> pair1 = new Pair<>("key", 1);
        Pair<String, Integer> pair2 = new Pair<>("key", 1);
        Pair<String, Integer> pair3 = new Pair<>("key", 2);
        Pair<String, Integer> pair4 = new Pair<>("key2", 1);
        Object obj = new Object();

        assertEquals(pair1, pair1); // same object
        assertEquals(pair1, pair2); // same key and value
        assertNotEquals(pair1, pair3); // different value
        assertNotEquals(pair1, pair4); // different key
        assertNotEquals(pair1, null); // compared with null
        assertNotEquals(pair1, obj); // compared with different type of object
    }

    @Test
    void testHashCode() {
        Pair<String, Integer> pair1 = new Pair<>("key", 1);
        Pair<String, Integer> pair2 = new Pair<>("key", 1);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }
}