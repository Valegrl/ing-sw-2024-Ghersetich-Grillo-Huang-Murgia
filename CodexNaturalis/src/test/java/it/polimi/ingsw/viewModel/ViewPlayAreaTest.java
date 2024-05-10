package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.viewModel.viewPlayer.ViewPlayArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ViewPlayAreaTest {
    private PlayArea playarea;

    @BeforeEach
    void setUp() {
        Coordinate coordinate1 = Mockito.mock(Coordinate.class);
        Coordinate coordinate2 = Mockito.mock(Coordinate.class);
        Coordinate coordinate3 = Mockito.mock(Coordinate.class);

        when(coordinate1.getX()).thenReturn(1);
        when(coordinate1.getY()).thenReturn(1);
        when(coordinate2.getX()).thenReturn(-1);
        when(coordinate2.getY()).thenReturn(-1);
        when(coordinate3.getX()).thenReturn(-1);
        when(coordinate3.getY()).thenReturn(1);


        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.INSECT);


        StartCard card0 = Mockito.mock(StartCard.class);

        when(card0.getId()).thenReturn("SC01");
        when(card0.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(card0.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        when(card0.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.ANIMAL});


        ResourceCard card1 = Mockito.mock(ResourceCard.class);

        when(card1.getId()).thenReturn("RC01");
        when(coordinate1.toString()).thenReturn("(1,1)");
        when(card1.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card1.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.EMPTY, Item.HIDDEN, Item.FUNGI});


        ResourceCard card2 = Mockito.mock(ResourceCard.class);

        when(card2.getId()).thenReturn("RC25");
        when(coordinate2.toString()).thenReturn("(-1,-1)");
        when(card2.getPermanentResource()).thenReturn(Item.INSECT);
        when(card2.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.INSECT, Item.ANIMAL, Item.INKWELL});


        Map<Item, Integer> constraint = new HashMap<>() {{
            put(Item.FUNGI, 2);
            put(Item.ANIMAL, 1);
        }};


        GoldCard card3 = Mockito.mock(GoldCard.class);

        when(card3.getId()).thenReturn("GC01");
        when(coordinate3.toString()).thenReturn("(-1,1)");
        when(card3.getPoints()).thenReturn(1);
        when(card3.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card3.getConstraint()).thenReturn(constraint);
        when(card3.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(card3.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.EMPTY, Item.QUILL, Item.EMPTY});


        ResourceCard card4 = Mockito.mock(ResourceCard.class);

        when(card4.getId()).thenReturn("RC01");
        when(card4.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card4.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.EMPTY, Item.HIDDEN, Item.FUNGI});


        ResourceCard card5 = Mockito.mock(ResourceCard.class);

        when(card5.getId()).thenReturn("RC25");
        when(card5.getPermanentResource()).thenReturn(Item.INSECT);
        when(card5.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.INSECT, Item.ANIMAL, Item.INKWELL});


        Map<Item, Integer> constraint1 = new HashMap<>() {{
            put(Item.FUNGI, 2);
            put(Item.ANIMAL, 1);
        }};


        GoldCard card6 = Mockito.mock(GoldCard.class);

        when(card6.getId()).thenReturn("GC01");
        when(card6.getPoints()).thenReturn(1);
        when(card6.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card6.getConstraint()).thenReturn(constraint1);
        when(card6.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(card6.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.EMPTY, Item.QUILL, Item.EMPTY});


        List<PlayableCard> hand = new ArrayList<>();
        hand.add(card4);
        hand.add(card5);
        hand.add(card6);


        Map<Coordinate, PlayableCard> playedCards = new HashMap<>();
        playedCards.put(coordinate1, card1);
        playedCards.put(coordinate2, card2);
        playedCards.put(coordinate3, card3);


        playarea = Mockito.mock(PlayArea.class);

        when(playarea.getHand()).thenReturn(hand);
        when(playarea.getStartCard()).thenReturn(card0);
        when(playarea.getPlayedCards()).thenReturn(playedCards);
    }

    @Test
    void printPlayedCardsTest() {
        ViewPlayArea viewPlayArea = new ViewPlayArea(playarea);
        System.out.println(viewPlayArea.printPlayedCards());
    }

    @Test
    void printHandTest() {
        ViewPlayArea viewPlayArea = new ViewPlayArea(playarea);
        String expectedOutput = "Player's Hand: \n" +
                "  " + Item.itemToColor(Item.FUNGI) + "\n" +
                "  " + Item.itemToColor(Item.INSECT) + "\n" +
                "  " + Item.itemToColor(Item.FUNGI) + "\n";

        assertEquals(expectedOutput, viewPlayArea.printHand());
        System.out.println(viewPlayArea.printHand());
    }
}