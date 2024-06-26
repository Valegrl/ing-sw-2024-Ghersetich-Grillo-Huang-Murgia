package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.evaluator.ItemEvaluator;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


public class SelfViewPlayAreaTest {
    private PlayArea playArea;

    @BeforeEach
    void setUp() {
        JsonConfig.loadConfig();
        List<Item> backPermanentResources = Arrays.asList(Item.PLANT, Item.PLANT);
        Item[] frontCorners = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        Item[] backCorners = new Item[]{Item.EMPTY, Item.PLANT, Item.EMPTY, Item.INSECT};
        boolean flipped = false;
        StartCard startCard1 = new StartCard("SCtest", backPermanentResources, frontCorners, backCorners, flipped);

        Item[] corners1 = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        ResourceCard resourceCard1 = new ResourceCard("RCtest1", null, 5, Item.PLANT, corners1);
        Item[] corners2 = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INSECT};
        Map<Item, Integer> constraint = new HashMap<>();
        constraint.put(Item.PLANT, 2);
        constraint.put(Item.FUNGI, 1);
        Map<Item, Integer> requiredItems = new HashMap<>();
        requiredItems.put(Item.QUILL, 1);
        GoldCard goldCard2 = new GoldCard("GCtest2", new ItemEvaluator(), 1, Item.PLANT, corners2, constraint, requiredItems);

        Item[] cornersGold = new Item[]{Item.PLANT, Item.ANIMAL, Item.FUNGI, Item.INKWELL};
        constraint.put(Item.PLANT, 2);
        GoldCard goldCard1 = new GoldCard("GCtest1", null, 5, Item.PLANT, cornersGold, constraint, null);

        List<PlayableCard> handTest = new ArrayList<>();
        this.playArea = new PlayArea(handTest, startCard1);
        playArea.flipStartCard(true);

        playArea.addToHand(resourceCard1);
        playArea.addToHand(goldCard1);
        playArea.addToHand(goldCard2);

        playArea.placeCard(resourceCard1, new Coordinate(-1,1));
        playArea.addToHand(goldCard1);

        goldCard2.flipCard();
        playArea.placeCard(goldCard2, new Coordinate(1,1));
        playArea.addToHand(resourceCard1);

        playArea.placeCard(goldCard1, new Coordinate(0,2));
        playArea.addToHand(goldCard2);
    }

    @Test
    void printSelfPlayAreaTest() {
        SelfViewPlayArea selfViewPlayArea = new SelfViewPlayArea(playArea);
        System.out.println(selfViewPlayArea.printPlayArea());
    }
}