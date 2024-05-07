package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.evaluator.CornerEvaluator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImmGoldCardTest {
    @Test
    public void testPrintCardNoConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.PLANT, 3);
        }};

        when(goldCard.getId()).thenReturn("GC17");
        when(goldCard.getPoints()).thenReturn(3);
        when(goldCard.getPermanentResource()).thenReturn(Item.PLANT);
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.EMPTY, Item.HIDDEN, Item.HIDDEN, Item.QUILL});

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.PLANT, "GC17") + "\n" +
                "  Points: 3\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.HIDDEN) + "\n" +
                "    BL: " + Item.itemToColor(Item.QUILL) + "  BR: " + Item.itemToColor(Item.HIDDEN) + "\n" +
                "  Constraint: \n" +
                "    Item: #3 plant";

        assertEquals(expectedOutput, card.printCard());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }

    @Test
    public void testPrintBackCardNoConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.PLANT, 3);
        }};

        when(goldCard.getId()).thenReturn("GC17");
        when(goldCard.getPoints()).thenReturn(3);
        when(goldCard.getPermanentResource()).thenReturn(Item.PLANT);
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.EMPTY, Item.HIDDEN, Item.HIDDEN, Item.QUILL});

        goldCard.flipCard();

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.PLANT, "GC17") + "\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n" ;

        assertEquals(expectedOutput, card.printCardBack());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }

    @Test
    public void testPrintCardWithConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> requiredItems = new HashMap<>(){{
            put(Item.MANUSCRIPT, 1);
        }};
        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.FUNGI, 2);
            put(Item.INSECT, 1);
        }};

        when(goldCard.getId()).thenReturn("GC03");
        when(goldCard.getPoints()).thenReturn(1);
        when(goldCard.getPermanentResource()).thenReturn(Item.FUNGI);
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(requiredItems);
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.MANUSCRIPT, Item.EMPTY, Item.HIDDEN, Item.EMPTY});

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.FUNGI, "GC03") + "\n" +
                "  Points: 1\n" +
                "  Required Items: \n" +
                "    Item: #1 manuscript\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.MANUSCRIPT) + "  TR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.HIDDEN) + "\n" +
                "  Constraint: \n" +
                "    Item: #2 fungi\n" +
                "    Item: #1 insect";

        assertEquals(expectedOutput, card.printCard());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }

    @Test
    public void testPrintBackCardWithConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> requiredItems = new HashMap<>(){{
            put(Item.MANUSCRIPT, 1);
        }};
        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.FUNGI, 2);
            put(Item.INSECT, 1);
        }};

        when(goldCard.getId()).thenReturn("GC03");
        when(goldCard.getPoints()).thenReturn(1);
        when(goldCard.getPermanentResource()).thenReturn(Item.FUNGI);
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(requiredItems);
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.MANUSCRIPT, Item.EMPTY, Item.HIDDEN, Item.EMPTY});

        goldCard.flipCard();

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.FUNGI, "GC03") + "\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n" ;

        assertEquals(expectedOutput, card.printCardBack());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }

    @Test
    public void testPrintCardWithCornerConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.FUNGI, 3);
            put(Item.INSECT, 1);
        }};

        when(goldCard.getId()).thenReturn("GC06");
        when(goldCard.getPoints()).thenReturn(2);
        when(goldCard.getPermanentResource()).thenReturn(Item.FUNGI);
        when(goldCard.getEvaluator()).thenReturn(new CornerEvaluator());
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.EMPTY, Item.HIDDEN, Item.EMPTY, Item.EMPTY});

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.FUNGI, "GC06") + "\n" +
                "  Points: 2\n" +
                "  2 points for each overlapping corner of this card \n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.HIDDEN) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "  Constraint: \n" +
                "    Item: #3 fungi\n" +
                "    Item: #1 insect";

        assertEquals(expectedOutput, card.printCard());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getEvaluator();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }

    @Test
    public void testPrintBackCardWithCornerConstraint() {
        GoldCard goldCard = Mockito.mock(GoldCard.class);

        Map<Item, Integer> constraint = new HashMap<>(){{
            put(Item.FUNGI, 3);
            put(Item.INSECT, 1);
        }};

        when(goldCard.getId()).thenReturn("GC06");
        when(goldCard.getPoints()).thenReturn(2);
        when(goldCard.getPermanentResource()).thenReturn(Item.FUNGI);
        when(goldCard.getEvaluator()).thenReturn(new CornerEvaluator());
        when(goldCard.getConstraint()).thenReturn(constraint);
        when(goldCard.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(goldCard.getCorners()).thenReturn(new Item[]{Item.EMPTY, Item.HIDDEN, Item.EMPTY, Item.EMPTY});

        goldCard.flipCard();

        ImmGoldCard card = new ImmGoldCard(goldCard);

        String expectedOutput = "GoldCard: " + Item.itemToColor(Item.FUNGI, "GC06") + "\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n" ;

        assertEquals(expectedOutput, card.printCardBack());

        verify(goldCard, times(1)).getId();
        verify(goldCard, times(1)).getPoints();
        verify(goldCard, times(1)).getPermanentResource();
        verify(goldCard, times(1)).getEvaluator();
        verify(goldCard, times(1)).getConstraint();
        verify(goldCard, times(1)).getRequiredItems();
        verify(goldCard, times(1)).getCorners();
    }
}
