package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImmObjectiveCardTest {
    @Test
    void testPrintCardUpwardsDiagonalPattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.FUNGI),
                new Pair<>(new Coordinate(1, 1), Item.FUNGI),
                new Pair<>(new Coordinate(2, 2), Item.FUNGI)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC01");
        when(objectiveCard.getPoints()).thenReturn(2);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC01\n" +
                "  Points: 2\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.FUNGI,"        |---|") +
                "\n" +
                Item.itemToColor(Item.FUNGI,"     |---|") +
                "\n" +
                Item.itemToColor(Item.FUNGI,"  |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardDownwardsDiagonalPattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.PLANT),
                new Pair<>(new Coordinate(-1, -1), Item.PLANT),
                new Pair<>(new Coordinate(-2, -2), Item.PLANT)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC02");
        when(objectiveCard.getPoints()).thenReturn(2);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC02\n" +
                "  Points: 2\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.PLANT,"  |---|") +
                "\n" +
                Item.itemToColor(Item.PLANT,"     |---|") +
                "\n" +
                Item.itemToColor(Item.PLANT,"        |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardOC05Pattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.PLANT),
                new Pair<>(new Coordinate(-1, 1), Item.FUNGI),
                new Pair<>(new Coordinate(-1, 2), Item.FUNGI)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC05");
        when(objectiveCard.getPoints()).thenReturn(3);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC05\n" +
                "  Points: 3\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.FUNGI,"  |---|") +
                "\n" +
                Item.itemToColor(Item.FUNGI,"  |---|") +
                "\n" +
                Item.itemToColor(Item.PLANT,"     |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardOC06Pattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.INSECT),
                new Pair<>(new Coordinate(1, 1), Item.PLANT),
                new Pair<>(new Coordinate(1, 2), Item.PLANT)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC06");
        when(objectiveCard.getPoints()).thenReturn(3);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC06\n" +
                "  Points: 3\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.PLANT,"     |---|") +
                "\n" +
                Item.itemToColor(Item.PLANT,"     |---|") +
                "\n" +
                Item.itemToColor(Item.INSECT,"  |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardOC07Pattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.FUNGI),
                new Pair<>(new Coordinate(-1, -1), Item.ANIMAL),
                new Pair<>(new Coordinate(-1, -2), Item.ANIMAL)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC07");
        when(objectiveCard.getPoints()).thenReturn(3);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC07\n" +
                "  Points: 3\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.FUNGI  ,"     |---|") +
                "\n" +
                Item.itemToColor(Item.ANIMAL ,"  |---|") +
                "\n" +
                Item.itemToColor(Item.ANIMAL,"  |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardOC08Pattern() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.ANIMAL),
                new Pair<>(new Coordinate(1, -1), Item.INSECT),
                new Pair<>(new Coordinate(1, -2), Item.INSECT)
        };
        Map<Item, Integer> requiredItems = new HashMap<>();

        when(objectiveCard.getId()).thenReturn("OC08");
        when(objectiveCard.getPoints()).thenReturn(3);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC08\n" +
                "  Points: 3\n" +
                "  Pattern: \n" +
                Item.itemToColor(Item.ANIMAL  ,"  |---|") +
                "\n" +
                Item.itemToColor(Item.INSECT ,"     |---|") +
                "\n" +
                Item.itemToColor(Item.INSECT ,"     |---|") +
                "\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardSameRequiredItems() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{};
        Map<Item, Integer> requiredItems = new HashMap<>(){{
            put(Item.FUNGI, 3);
        }};

        when(objectiveCard.getId()).thenReturn("OC09");
        when(objectiveCard.getPoints()).thenReturn(2);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC09\n" +
                "  Points: 2\n" +
                "  Required Items: \n" +
                "    Item: #3 fungi\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }

    @Test
    void testPrintCardMultipleRequiredItems() {
        ObjectiveCard objectiveCard = Mockito.mock(ObjectiveCard.class);

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{};
        Map<Item, Integer> requiredItems = new HashMap<>(){{
            put(Item.QUILL, 1);
            put(Item.INKWELL, 1);
            put(Item.MANUSCRIPT, 1);
        }};

        when(objectiveCard.getId()).thenReturn("OC13");
        when(objectiveCard.getPoints()).thenReturn(3);
        when(objectiveCard.getRequiredItems()).thenReturn(requiredItems);
        when(objectiveCard.getRequiredPattern()).thenReturn(requiredPattern);

        ImmObjectiveCard card = new ImmObjectiveCard(objectiveCard);

        String expectedOutput = "ObjectiveCard: OC13\n" +
                "  Points: 3\n" +
                "  Required Items: \n" +
                "    Item: #1 inkwell\n" +
                "    Item: #1 manuscript\n" +
                "    Item: #1 quill\n";

        assertEquals(expectedOutput, card.printCard());

        verify(objectiveCard, times(1)).getId();
        verify(objectiveCard, times(1)).getPoints();
        verify(objectiveCard, times(1)).getRequiredItems();
        verify(objectiveCard, times(1)).getRequiredPattern();
    }
}