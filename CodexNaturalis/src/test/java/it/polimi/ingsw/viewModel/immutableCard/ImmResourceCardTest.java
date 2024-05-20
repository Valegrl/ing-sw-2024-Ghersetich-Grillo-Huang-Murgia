package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.Item;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImmResourceCardTest {
    @Test
    public void testPrintCardNoPoints() {
        ResourceCard resourceCard = Mockito.mock(ResourceCard.class);

        when(resourceCard.getId()).thenReturn("RC16");
        when(resourceCard.getPermanentResource()).thenReturn(Item.PLANT);
        when(resourceCard.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.INKWELL, Item.HIDDEN});

        ImmResourceCard card = new ImmResourceCard(resourceCard);

        String expectedOutput = "ResourceCard: " + Item.itemToColor(Item.PLANT, "RC16") + "\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.FUNGI) + "  TR: " + Item.itemToColor(Item.PLANT) + "\n" +
                "    BL: " + Item.itemToColor(Item.HIDDEN) + "  BR: " + Item.itemToColor(Item.INKWELL)+ "\n";

        assertEquals(expectedOutput, card.printCard(0));
        System.out.println(card.printCard(0));

        verify(resourceCard, times(1)).getId();
        verify(resourceCard, times(1)).getPermanentResource();
        verify(resourceCard, times(1)).getCorners();
    }

    @Test
    public void testPrintCardBackNoPoints() {
        ResourceCard resourceCard = Mockito.mock(ResourceCard.class);

        when(resourceCard.getId()).thenReturn("RC16");
        when(resourceCard.getPermanentResource()).thenReturn(Item.PLANT);
        when(resourceCard.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.INKWELL, Item.HIDDEN});

        resourceCard.flipCard();

        ImmResourceCard card = new ImmResourceCard(resourceCard);

        String expectedOutput = "ResourceCard: " + Item.itemToColor(Item.PLANT, "RC16") + "\n" +
                "  Corners: \n" +
                "    TL: empty  TR: empty\n" +
                "    BL: empty  BR: empty\n";

        assertEquals(expectedOutput, card.printCardBack());
        System.out.println(card.printCardBack());

        verify(resourceCard, times(1)).getId();
        verify(resourceCard, times(1)).getPermanentResource();
        verify(resourceCard, times(1)).getCorners();
    }

    @Test
    public void testPrintCardWithPoints() {
        ResourceCard resourceCard = Mockito.mock(ResourceCard.class);

        when(resourceCard.getId()).thenReturn("RC38");
        when(resourceCard.getPoints()).thenReturn(1);
        when(resourceCard.getPermanentResource()).thenReturn(Item.INSECT);
        when(resourceCard.getCorners()).thenReturn(new Item[]{Item.INSECT, Item.HIDDEN, Item.EMPTY, Item.EMPTY});

        ImmResourceCard card = new ImmResourceCard(resourceCard);

        String expectedOutput = "ResourceCard: " + Item.itemToColor(Item.INSECT, "RC38") + "\n" +
                "  Points: 1\n" +
                "  Corners: \n" +
                "    TL: " + Item.itemToColor(Item.INSECT) + "  TR: " + Item.itemToColor(Item.HIDDEN) + "\n" +
                "    BL: " + Item.itemToColor(Item.EMPTY) + "  BR: " + Item.itemToColor(Item.EMPTY) + "\n";

        assertEquals(expectedOutput, card.printCard(0));
        System.out.println(card.printCard(0));

        verify(resourceCard, times(1)).getId();
        verify(resourceCard, times(1)).getPoints();
        verify(resourceCard, times(1)).getPermanentResource();
        verify(resourceCard, times(1)).getCorners();
    }

    @Test
    public void testPrintCardBackWithPoints() {
        ResourceCard resourceCard = Mockito.mock(ResourceCard.class);

        when(resourceCard.getId()).thenReturn("RC38");
        when(resourceCard.getPoints()).thenReturn(1);
        when(resourceCard.getPermanentResource()).thenReturn(Item.INSECT);
        when(resourceCard.getCorners()).thenReturn(new Item[]{Item.INSECT, Item.HIDDEN, Item.EMPTY, Item.EMPTY});

        resourceCard.flipCard();

        ImmResourceCard card = new ImmResourceCard(resourceCard);

        String expectedOutput = "ResourceCard: " + Item.itemToColor(Item.INSECT, "RC38") + "\n" +
                "  Corners: \n" +
                "    TL: empty  TR: empty\n" +
                "    BL: empty  BR: empty\n";

        assertEquals(expectedOutput, card.printCardBack());
        System.out.println(card.printCardBack());

        verify(resourceCard, times(1)).getId();
        verify(resourceCard, times(1)).getPoints();
        verify(resourceCard, times(1)).getPermanentResource();
        verify(resourceCard, times(1)).getCorners();
    }

}
