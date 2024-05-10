package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;


public class SelfViewPlayAreaTest {
    private PlayArea playarea;

    @BeforeEach
    void setUp() {
        //initializing the coordinates
        Coordinate coordinate1 = Mockito.mock(Coordinate.class);
        Coordinate coordinate2 = Mockito.mock(Coordinate.class);
        Coordinate coordinate3 = Mockito.mock(Coordinate.class);
        Coordinate selectedCardCoordinate = Mockito.mock(Coordinate.class);
        when(selectedCardCoordinate.getX()).thenReturn(0);
        when(selectedCardCoordinate.getY()).thenReturn(0);
        when(coordinate1.getX()).thenReturn(1);
        when(coordinate1.getY()).thenReturn(1);
        when(coordinate2.getX()).thenReturn(-1);
        when(coordinate2.getY()).thenReturn(-1);
        when(coordinate3.getX()).thenReturn(-1);
        when(coordinate3.getY()).thenReturn(1);

        //initializing the start card
        List<Item> backPermanentResources = new ArrayList<>();
        backPermanentResources.add(Item.INSECT);

        StartCard card0 = Mockito.mock(StartCard.class);
        when(card0.getId()).thenReturn("SC01");
        when(card0.getBackPermanentResources()).thenReturn(backPermanentResources);
        when(card0.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        when(card0.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.ANIMAL});

        //initializing a PlayableCard that acts as a playedCard
        PlayableCard card1 = Mockito.mock(PlayableCard.class);
        when(card1.getId()).thenReturn("RC01");
        when(coordinate1.toString()).thenReturn("(1,1)");
        when(card1.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card1.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.EMPTY, Item.HIDDEN, Item.FUNGI});

        //initializing a PlayableCard that acts as a playedCard
        PlayableCard card2 = Mockito.mock(PlayableCard.class);
        when(card2.getId()).thenReturn("RC25");
        when(coordinate2.toString()).thenReturn("(-1,-1)");
        when(card2.getPermanentResource()).thenReturn(Item.INSECT);
        when(card2.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.INSECT, Item.ANIMAL, Item.INKWELL});

        //initializing a PlayableCard that acts as a playedCard
        Map<Item, Integer> constraint = new HashMap<>() {{
            put(Item.FUNGI, 2);
            put(Item.ANIMAL, 1);
        }};
        PlayableCard card3 = Mockito.mock(PlayableCard.class);
        when(card3.getId()).thenReturn("GC01");
        when(coordinate3.toString()).thenReturn("(-1,1)");
        when(card3.getPoints()).thenReturn(1);
        when(card3.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card3.getConstraint()).thenReturn(constraint);
        when(card3.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(card3.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.EMPTY, Item.QUILL, Item.EMPTY});

        //initializing a PlayableCard that acts as a hand card
        PlayableCard card4 = Mockito.mock(PlayableCard.class);
        when(card4.getId()).thenReturn("RC01");
        when(card4.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card4.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.EMPTY, Item.HIDDEN, Item.FUNGI});

        //initializing a PlayableCard that acts as a hand card
        PlayableCard card5 = Mockito.mock(PlayableCard.class);
        when(card5.getId()).thenReturn("RC25");
        when(card5.getPermanentResource()).thenReturn(Item.INSECT);
        when(card5.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.INSECT, Item.ANIMAL, Item.INKWELL});

        //initializing a PlayableCard that acts as a hand card
        Map<Item, Integer> constraint1 = new HashMap<>() {{
            put(Item.FUNGI, 2);
            put(Item.ANIMAL, 1);
        }};
        PlayableCard card6 = Mockito.mock(PlayableCard.class);
        when(card6.getId()).thenReturn("GC01");
        when(card6.getPoints()).thenReturn(1);
        when(card6.getPermanentResource()).thenReturn(Item.FUNGI);
        when(card6.getConstraint()).thenReturn(constraint1);
        when(card6.getRequiredItems()).thenReturn(Collections.emptyMap());
        when(card6.getCorners()).thenReturn(new Item[]{Item.HIDDEN, Item.EMPTY, Item.QUILL, Item.EMPTY});


        //initializing the hand
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(card4);
        hand.add(card5);
        hand.add(card6);

        //initializing the played cards
        Map<Coordinate, PlayableCard> playedCards = new HashMap<>();
        playedCards.put(coordinate1, card1);
        playedCards.put(coordinate2, card2);
        playedCards.put(coordinate3, card3);

        //initializing the uncovered items
        Map<Item, Integer> uncoveredItems = new HashMap<>();
        uncoveredItems.put(Item.FUNGI, 2);
        uncoveredItems.put(Item.ANIMAL, 1);

        //initializing the selected card
        PlayableCard selectedCard = Mockito.mock(PlayableCard.class);
        when(selectedCard.getId()).thenReturn("RC01");
        when(selectedCard.getPermanentResource()).thenReturn(Item.FUNGI);
        when(selectedCard.getCorners()).thenReturn(new Item[]{Item.FUNGI, Item.EMPTY, Item.HIDDEN, Item.FUNGI});

        Pair<Coordinate, EvaluableCard> selectedCardPair = new Pair<>(selectedCardCoordinate, selectedCard);


        //initializing the play area
        playarea = Mockito.mock(PlayArea.class);
        when(playarea.getHand()).thenReturn(hand);
        when(playarea.getStartCard()).thenReturn(card0);
        when(playarea.getPlayedCards()).thenReturn(playedCards);
        when(playarea.getUncoveredItems()).thenReturn(uncoveredItems);
        when(playarea.getSelectedCard()).thenReturn(selectedCardPair);
    }

    @Test
    void printPlayedCardsTest() {
        SelfViewPlayArea selfViewPlayArea = new SelfViewPlayArea(playarea);
        System.out.println(selfViewPlayArea.printPlayedCards());
    }

    @Test
    void printHandTest() {
        SelfViewPlayArea selfViewPlayArea = new SelfViewPlayArea(playarea);
        System.out.println(selfViewPlayArea.printHand());
    }
}