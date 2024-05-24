package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.viewModel.viewPlayer.ViewPlayArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;

public class ViewPlayAreaTest {
    private PlayArea playArea;

    @BeforeEach
    void setUp() {
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
        this.playArea = new PlayArea(handTest, startCard1);
        playArea.flipStartCard(true);

        playArea.addToHand(resourceCard1);
        playArea.addToHand(resourceCard2);
        playArea.addToHand(goldCard1);

        playArea.placeCard(resourceCard1, new Coordinate(-1,1));
        playArea.addToHand(goldCard1);

        resourceCard2.flipCard();
        playArea.placeCard(resourceCard2, new Coordinate(1,1));
        playArea.addToHand(resourceCard1);

        playArea.placeCard(goldCard1, new Coordinate(0,2));
        playArea.addToHand(resourceCard2);
    }

    @Test
    void printPlayedCardsTest() {
        ViewPlayArea viewPlayArea = new ViewPlayArea(playArea);
        System.out.println(viewPlayArea.printPlayedCards());
    }

    @Test
    void printHandTest() {
        ViewPlayArea viewPlayArea = new ViewPlayArea(playArea);
        System.out.println(viewPlayArea.printHand());
    }
}