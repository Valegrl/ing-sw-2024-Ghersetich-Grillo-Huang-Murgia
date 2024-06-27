package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.utils.AnsiCodes.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void getType() {
        assertEquals("plant", Item.PLANT.getType());
        assertEquals("animal", Item.ANIMAL.getType());
        assertEquals("fungi", Item.FUNGI.getType());
        assertEquals("insect", Item.INSECT.getType());
        assertEquals("quill", Item.QUILL.getType());
        assertEquals("inkwell", Item.INKWELL.getType());
        assertEquals("manuscript", Item.MANUSCRIPT.getType());
        assertEquals("empty", Item.EMPTY.getType());
        assertEquals("hidden", Item.HIDDEN.getType());
        assertEquals("covered", Item.COVERED.getType());
    }

    @Test
    void testToString() {
        assertEquals("plant", Item.PLANT.toString());
        assertEquals("animal", Item.ANIMAL.toString());
        assertEquals("fungi", Item.FUNGI.toString());
        assertEquals("insect", Item.INSECT.toString());
        assertEquals("quill", Item.QUILL.toString());
        assertEquals("inkwell", Item.INKWELL.toString());
        assertEquals("manuscript", Item.MANUSCRIPT.toString());
        assertEquals("empty", Item.EMPTY.toString());
        assertEquals("hidden", Item.HIDDEN.toString());
        assertEquals("covered", Item.COVERED.toString());
    }

    @Test
    void getItemFromString() {
        assertEquals(Item.PLANT, Item.getItemFromString("plant"));
        assertEquals(Item.ANIMAL, Item.getItemFromString("animal"));
        assertEquals(Item.FUNGI, Item.getItemFromString("fungi"));
        assertEquals(Item.INSECT, Item.getItemFromString("insect"));
        assertEquals(Item.QUILL, Item.getItemFromString("quill"));
        assertEquals(Item.INKWELL, Item.getItemFromString("inkwell"));
        assertEquals(Item.MANUSCRIPT, Item.getItemFromString("manuscript"));
        assertEquals(Item.EMPTY, Item.getItemFromString("empty"));
        assertEquals(Item.HIDDEN, Item.getItemFromString("hidden"));
        assertEquals(Item.COVERED, Item.getItemFromString("covered"));
        assertNull(Item.getItemFromString("nonexistent"));
    }

    @Test
    void itemToColorWithItemAndString() {
        assertEquals(GREEN + "test" + RESET, Item.itemToColor(Item.PLANT, "test"));
        assertEquals(CYAN + "test" + RESET, Item.itemToColor(Item.ANIMAL, "test"));
        assertEquals(RED + "test" + RESET, Item.itemToColor(Item.FUNGI, "test"));
        assertEquals(PURPLE + "test" + RESET, Item.itemToColor(Item.INSECT, "test"));
        assertEquals(GOLD + "test" + RESET, Item.itemToColor(Item.QUILL, "test"));
        assertEquals(GOLD + "test" + RESET, Item.itemToColor(Item.INKWELL, "test"));
        assertEquals(GOLD + "test" + RESET, Item.itemToColor(Item.MANUSCRIPT, "test"));
        assertEquals(BEIGE + "test" + RESET, Item.itemToColor(Item.EMPTY, "test"));
        assertEquals("test", Item.itemToColor(Item.HIDDEN, "test"));
        assertEquals("test", Item.itemToColor(Item.COVERED, "test"));
    }

    @Test
    void itemToColorWithItem() {
        assertEquals(GREEN + Item.PLANT + RESET, Item.itemToColor(Item.PLANT));
        assertEquals(CYAN + Item.ANIMAL + RESET, Item.itemToColor(Item.ANIMAL));
        assertEquals(RED + Item.FUNGI + RESET, Item.itemToColor(Item.FUNGI));
        assertEquals(PURPLE + Item.INSECT + RESET, Item.itemToColor(Item.INSECT));
        assertEquals(GOLD + Item.QUILL + RESET, Item.itemToColor(Item.QUILL));
        assertEquals(GOLD + Item.INKWELL + RESET, Item.itemToColor(Item.INKWELL));
        assertEquals(GOLD + Item.MANUSCRIPT + RESET, Item.itemToColor(Item.MANUSCRIPT));
        assertEquals(BEIGE + Item.EMPTY + RESET, Item.itemToColor(Item.EMPTY));
        assertEquals(Item.HIDDEN.toString(), Item.itemToColor(Item.HIDDEN));
        assertEquals(Item.COVERED.toString(), Item.itemToColor(Item.COVERED));
    }
}