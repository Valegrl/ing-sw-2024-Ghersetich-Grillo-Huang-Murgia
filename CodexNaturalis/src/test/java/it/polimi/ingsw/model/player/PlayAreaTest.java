package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayAreaTest {
    private PlayArea playArea;
    private StartCard startCard;

    @BeforeEach
    void setUp() {
        startCard = Mockito.mock(StartCard.class);
        Mockito.when(startCard.getId()).thenReturn("SC01");
        Mockito.when(startCard.getBackPermanentResources()).thenReturn(Collections.singletonList(Item.INSECT));
        Mockito.when(startCard.getFrontCorners()).thenReturn(new Item[]{Item.FUNGI, Item.PLANT, Item.ANIMAL, Item.INSECT});
        Mockito.when(startCard.getBackCorners()).thenReturn(new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.INSECT});

        List<PlayableCard> hand = new ArrayList<>();
        playArea = new PlayArea(hand, startCard);
    }

    @Test
    void testPlayAreaConstructor() {
        List<PlayableCard> hand = new ArrayList<>();
        assertEquals(hand, playArea.getHand());
        assertEquals(startCard, playArea.getStartCard());
        assertTrue(playArea.getPlayedCards().isEmpty());
        assertTrue(playArea.getUncoveredItems().values().stream().allMatch(v -> v == 0));
        assertNull(playArea.getSelectedCard().getKey());
        assertNull(playArea.getSelectedCard().getValue());
    }

    @Test
    void testFlipStartCard() {
        Mockito.when(startCard.isFlipped()).thenReturn(true);
        Mockito.when(startCard.getBackPermanentResources()).thenReturn(List.of(new Item[]{Item.QUILL}));
        Mockito.when(startCard.getBackCorners()).thenReturn(new Item[]{Item.INKWELL, Item.MANUSCRIPT, Item.EMPTY, Item.HIDDEN});

        playArea.flipStartCard(true);
        playArea.flipStartCard(false);

        Mockito.verify(startCard).flipCard();
        assertEquals(1, playArea.getUncoveredItems().get(Item.QUILL));
        assertEquals(1, playArea.getUncoveredItems().get(Item.INKWELL));
        assertEquals(1, playArea.getUncoveredItems().get(Item.MANUSCRIPT));
    }

    @Test
    void testSetSelectedCard() {
        EvaluableCard evaluableCard = Mockito.mock(EvaluableCard.class);
        Coordinate coordinate = new Coordinate(1, 1);

        playArea.setSelectedCard(coordinate, evaluableCard);

        assertEquals(coordinate, playArea.getSelectedCard().getKey());
        assertEquals(evaluableCard, playArea.getSelectedCard().getValue());
    }
    @Test
    void testCheckConstraintSatisfied() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Map<Item, Integer> constraint = new HashMap<>() {{
            put(Item.PLANT, 1);
        }};
        Mockito.when(playableCard.getConstraint()).thenReturn(constraint);
        Mockito.when(playableCard.getCardType()).thenReturn(CardType.GOLD);

        playArea.getUncoveredItems().put(Item.PLANT, 2);

        assertTrue(playArea.checkConstraint(playableCard));
    }

    @Test
    void testCheckConstraintNotSatisfied() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Map<Item, Integer> constraint = new HashMap<>() {{
            put(Item.PLANT, 3);
        }};
        Mockito.when(playableCard.getConstraint()).thenReturn(constraint);
        Mockito.when(playableCard.getCardType()).thenReturn(CardType.GOLD);

        playArea.getUncoveredItems().put(Item.PLANT, 2);

        assertFalse(playArea.checkConstraint(playableCard));
    }

    @Test
    void testCheckConstraintNonConstraintCard() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Mockito.when(playableCard.getCardType()).thenReturn(CardType.RESOURCE);

        assertThrows(RuntimeException.class, () -> playArea.checkConstraint(playableCard));
    }

    @Test
    void testGetCardByPos() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Coordinate coordinate = new Coordinate(1, 1);
        playArea.getPlayedCards().put(coordinate, playableCard);

        assertEquals(playableCard, playArea.getCardByPos(coordinate));
        assertEquals(startCard, playArea.getCardByPos(new Coordinate(0, 0)));
        assertNull(playArea.getCardByPos(new Coordinate(2, 2)));
    }

    @Test
    void testAddToHand() {
        PlayableCard playableCard1 = Mockito.mock(PlayableCard.class);
        PlayableCard playableCard2 = Mockito.mock(PlayableCard.class);
        PlayableCard playableCard3 = Mockito.mock(PlayableCard.class);
        PlayableCard playableCard4 = Mockito.mock(PlayableCard.class);

        playArea.addToHand(playableCard1);
        playArea.addToHand(playableCard2);
        playArea.addToHand(playableCard3);

        assertTrue(playArea.getHand().contains(playableCard1));
        assertTrue(playArea.getHand().contains(playableCard2));
        assertTrue(playArea.getHand().contains(playableCard3));

        Exception exception = assertThrows(RuntimeException.class, () -> playArea.addToHand(playableCard4));
        assertEquals("You can't add more cards to your hand.", exception.getMessage());
    }

    @Test
    void testPlaceCard() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Coordinate coordinate = new Coordinate(1, 1);

        Mockito.when(playableCard.getCorners()).thenReturn(new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT});
        Mockito.when(playableCard.getPermanentResource()).thenReturn(Item.ANIMAL);

        playArea.addToHand(playableCard);
        playArea.placeCard(playableCard, coordinate, false);

        assertEquals(playableCard, playArea.getCardByPos(coordinate));
        assertEquals(0, playArea.getUncoveredItems().get(Item.PLANT));
        assertEquals(1, playArea.getUncoveredItems().get(Item.ANIMAL));
        assertEquals(1, playArea.getUncoveredItems().get(Item.FUNGI));
        assertEquals(1, playArea.getUncoveredItems().get(Item.INSECT));
        assertFalse(playArea.getHand().contains(playableCard));
    }

    @Test
    void testPlaceCardEmulation(){

        List<Item> backPermanentResources = Arrays.asList(Item.PLANT, Item.PLANT);
        Item[] frontCorners = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        Item[] backCorners = new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.INSECT};
        boolean flipped = false;
        StartCard startCard1 = new StartCard("SCtest", backPermanentResources, frontCorners, backCorners, flipped);

        Item[] corners1 = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        ResourceCard resourceCard1 = new ResourceCard("RCtest1", null, 5, Item.PLANT, corners1);
        Item[] corners2 = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        ResourceCard resourceCard2 = new ResourceCard("RCtest2", null, 5, Item.PLANT, corners2);

        Item[] cornersGold = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INKWELL};
        Map<Item, Integer> constraint = new HashMap<>();
        constraint.put(Item.PLANT, 2);
        GoldCard goldCard1 = new GoldCard("GCtest1", null, 5, Item.PLANT, cornersGold, constraint, null);

        List<PlayableCard> handTest = new ArrayList<>();
        PlayArea playArea1 = new PlayArea(handTest, startCard1);
        playArea1.flipStartCard(true);

        playArea1.addToHand(resourceCard1);
        playArea1.addToHand(resourceCard2);
        playArea1.addToHand(goldCard1);

        playArea1.placeCard(resourceCard1, new Coordinate(-1,1), false);
        playArea1.placeCard(resourceCard2, new Coordinate(1,1), true);
        playArea1.placeCard(goldCard1, new Coordinate(0,2), false);

        Map<Item, Integer> amount = playArea1.getUncoveredItems();
        List<Coordinate> freePos = playArea1.getAvailablePos();

        Map<Item, Integer> expectedAmount = new HashMap<>(){{
            put(Item.PLANT, 5);
            put(Item.ANIMAL, 1);
            put(Item.FUNGI, 2);
            put(Item.INSECT, 2);
            put(Item.QUILL, 0);
            put(Item.INKWELL, 1);
            put(Item.MANUSCRIPT, 0);
        }};

        assertEquals(expectedAmount, amount);

        List<Coordinate> expectedFreePos = Arrays.asList(
                new Coordinate(-1, 3),
                new Coordinate(1, 3),
                new Coordinate(2, 2),
                new Coordinate(2, 0),
                new Coordinate(1, -1),
                new Coordinate(-1, -1),
                new Coordinate(-2, 0),
                new Coordinate(-2, 2)
        );

        Set<Coordinate> set1 = new HashSet<>(freePos);
        Set<Coordinate> set2 = new HashSet<>(expectedFreePos);

        assertTrue(set1.containsAll(set2) && set2.containsAll(set1));
    }

    @Test
    void testPlaceCardMissingFromHand() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Coordinate coordinate = new Coordinate(1, 1);

        Exception exception = assertThrows(RuntimeException.class, () -> playArea.placeCard(playableCard, coordinate, false));
        assertEquals("Your hand doesn't contain such card", exception.getMessage());
    }

    @Test
    void testPlaceCardNoCoveredCards() {
        PlayableCard playableCard = Mockito.mock(PlayableCard.class);
        Coordinate coordinate = new Coordinate(2, 2);

        playArea.addToHand(playableCard);

        Exception exception = assertThrows(RuntimeException.class, () -> playArea.placeCard(playableCard, coordinate, false));
        assertEquals("The card you placed didn't cover any other cards", exception.getMessage());
    }
}