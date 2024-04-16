package it.polimi.ingsw.model.card;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerIndexTest {

    @Test
    void getIndex() {
        assertEquals(0, CornerIndex.TL.getIndex());
        assertEquals(1, CornerIndex.TR.getIndex());
        assertEquals(2, CornerIndex.BR.getIndex());
        assertEquals(3, CornerIndex.BL.getIndex());
    }

    @Test
    void values() {
        CornerIndex[] expectedValues = {CornerIndex.TL, CornerIndex.TR, CornerIndex.BR, CornerIndex.BL};
        assertArrayEquals(expectedValues, CornerIndex.values());
    }

    @Test
    void valueOf() {
        assertEquals(CornerIndex.TL, CornerIndex.valueOf("TL"));
        assertEquals(CornerIndex.TR, CornerIndex.valueOf("TR"));
        assertEquals(CornerIndex.BR, CornerIndex.valueOf("BR"));
        assertEquals(CornerIndex.BL, CornerIndex.valueOf("BL"));
    }
}