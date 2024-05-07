package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.StartCard;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImmStartCardTest {
    @Test
    void testPrintCardSingleBackResource() {
        StartCard startCard = Mockito.mock(StartCard.class);

        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.ANIMAL);

        when(startCard.getId()).thenReturn("SC01");
        when(startCard.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(startCard.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        when(startCard.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.ANIMAL});

        ImmStartCard card = new ImmStartCard(startCard);

        String expectedOutput = "StartCard: " + "SC01" + "\n" +
                "  Front Corners: \n" +
                "    TL: " + Item.itemToColor(Item.FUNGI) + "  TR: " + Item.itemToColor(Item.PLANT) + "\n" +
                "    BL: " + Item.itemToColor(Item.INSECT) + "  BR: " + Item.itemToColor(Item.ANIMAL) + "\n";

        assertEquals(expectedOutput, card.printCard());
        System.out.println(card.printCard());

        verify(startCard, times(1)).getId();
        verify(startCard, times(1)).getBackPermanentResources();
        verify(startCard, times(1)).getFrontCorners();
        verify(startCard, times(1)).getBackCorners();
    }

    @Test
    void testPrintCardMultipleBackResource() {
        StartCard startCard = Mockito.mock(StartCard.class);

        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.ANIMAL);
        backPermanentResources.add(Item.PLANT);
        backPermanentResources.add(Item.FUNGI);

        when(startCard.getId()).thenReturn("SC01");
        when(startCard.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(startCard.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        when(startCard.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.ANIMAL});

        ImmStartCard card = new ImmStartCard(startCard);

        String expectedOutput = "StartCard: " + "SC01" + "\n" +
                "  Front Corners: \n" +
                "    TL: " + Item.itemToColor(Item.FUNGI) + "  TR: " + Item.itemToColor(Item.PLANT) + "\n" +
                "    BL: " + Item.itemToColor(Item.INSECT) + "  BR: " + Item.itemToColor(Item.ANIMAL) + "\n";

        assertEquals(expectedOutput, card.printCard());
        System.out.println(card.printCard());

        verify(startCard, times(1)).getId();
        verify(startCard, times(1)).getBackPermanentResources();
        verify(startCard, times(1)).getFrontCorners();
        verify(startCard, times(1)).getBackCorners();
    }

    @Test
    void testPrintCardBackSingleBackResource() {
        StartCard startCard = Mockito.mock(StartCard.class);

        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.ANIMAL);

        startCard.flipCard();

        when(startCard.getId()).thenReturn("SC01");
        when(startCard.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(startCard.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        when(startCard.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.ANIMAL});

        ImmStartCard card = new ImmStartCard(startCard);

        String expectedOutput = "StartCard: " + "SC01" + "\n" +
                "  Back Permanent Resources: \n" +
                "    " + Item.itemToColor(Item.ANIMAL) + "\n" +
                "  Back Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.PLANT) + "\n" +
                "    BL: " + Item.itemToColor(Item.ANIMAL) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n";

        assertEquals(expectedOutput, card.printCardBack());
        System.out.println(card.printCardBack());

        verify(startCard, times(1)).getId();
        verify(startCard, times(1)).getBackPermanentResources();
        verify(startCard, times(1)).getFrontCorners();
        verify(startCard, times(1)).getBackCorners();
    }

    @Test
    void testPrintCardBackMultipleBackResource() {
        StartCard startCard = Mockito.mock(StartCard.class);

        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.ANIMAL);
        backPermanentResources.add(Item.PLANT);
        backPermanentResources.add(Item.FUNGI);

        startCard.flipCard();

        when(startCard.getId()).thenReturn("SC06");
        when(startCard.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(startCard.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.ANIMAL, Item.INSECT, Item.PLANT});
        when(startCard.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.EMPTY, Item.HIDDEN, Item.HIDDEN});

        ImmStartCard card = new ImmStartCard(startCard);

        String expectedOutput = "StartCard: " + "SC06" + "\n" +
                "  Back Permanent Resources: \n" +
                "    " + Item.itemToColor(Item.ANIMAL) + ", " +
                Item.itemToColor(Item.FUNGI) + ", " +
                Item.itemToColor(Item.PLANT) + "\n" +
                "  Back Corners: \n" +
                "    TL: " + Item.itemToColor(Item.EMPTY) + "  TR: " + Item.itemToColor(Item.EMPTY) + "\n" +
                "    BL: " + Item.itemToColor(Item.HIDDEN) + "  BR: " + Item.itemToColor(Item.HIDDEN) + "\n";

        assertEquals(expectedOutput, card.printCardBack());
        System.out.println(card.printCardBack());

        verify(startCard, times(1)).getId();
        verify(startCard, times(1)).getBackPermanentResources();
        verify(startCard, times(1)).getFrontCorners();
        verify(startCard, times(1)).getBackCorners();
    }
}
