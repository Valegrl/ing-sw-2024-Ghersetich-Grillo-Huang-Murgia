package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.*;
import it.polimi.ingsw.model.deck.factory.DeckFactory;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.NonConstraintCardException;
import it.polimi.ingsw.utils.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO Code review
class PlayAreaTest {

    private PlayArea playArea;
    private List<PlayableCard> hand;
    private StartCard startCard;

    @BeforeEach
    void setUp() {
        Deck<ResourceCard> resourceDeck = new DeckFactory().createDeck(ResourceCard.class);
        this.hand = new ArrayList<>();
        hand.add(resourceDeck.drawTop());
        hand.add(resourceDeck.drawTop());
        hand.add(new DeckFactory().createDeck(GoldCard.class).drawTop());

        this.startCard = new DeckFactory().createDeck(StartCard.class).drawTop();
        this.playArea = new PlayArea(hand, startCard);
        assertNotEquals(0, playArea.getUncoveredItems().size());
        assertEquals(0, playArea.getPlayedCards().size());
        assertEquals(startCard, playArea.getStartCard());
        assertEquals(hand, playArea.getHand());
    }

    @Test
    void flipStartCardWithTrue() {
        playArea.flipStartCard(true);
        assertTrue(startCard.isFlipped());

        int counter = 0;

        /*Checks if it has the exact number of items on the permanent list and back corners of the startCard*/
        for(Item item1 : startCard.getBackPermanentResources()){
            counter++;
            for(Item item2 : startCard.getBackCorners()){
                if(item1.equals(item2)){
                    counter++;
                }
            }
            assertEquals(playArea.getUncoveredItems().get(item1), counter);
            counter = 0;
        }
    }

    @Test
    void flipStartCardWithFalse(){
        playArea.flipStartCard(false);
        assertFalse(startCard.isFlipped());

        /*Checks if it has the exact number of items on the front corners of the startCard*/
        for(Item item : startCard.getFrontCorners()){
            assertEquals(playArea.getUncoveredItems().get(item), 1);
        }
    }

    @Test
    void setSelectedCardFromHand() {
        int x = 0;
        int y = 0;

        for(EvaluableCard card : playArea.getHand()) {
            x++;
            y++;
            Coordinate pos = new Coordinate(x, y);
            playArea.setSelectedCard(pos, card);
            assertEquals(pos, playArea.getSelectedCard().getKey());
            assertEquals(card, playArea.getSelectedCard().getValue());
        }
    }

    @Test
    void checkConstraintResourceCard() {
        PlayableCard resourceCard = new DeckFactory().createDeck(ResourceCard.class).drawTop();
        try{
            playArea.checkConstraint(resourceCard);
        }
        catch(NonConstraintCardException e){
            System.out.println("Card has no constraint!");
        }
    }

    @Test
    void checkConstraintGoldCardFalse(){
        /*Requires initialization of the playArea uncoveredItems so that it will return false.*/
        for(Item item : Item.values()){
            if(item != Item.EMPTY && item != Item.COVERED && item != Item.HIDDEN) {
                playArea.getUncoveredItems().put(item, 0);
            }
        }

        PlayableCard goldCard = new DeckFactory().createDeck(GoldCard.class).drawTop();
        assertFalse(playArea.checkConstraint(goldCard));
    }

    @Test
    void checkConstraintGoldCardTrue(){
        /*Requires initialization of the playArea uncoveredItems so that it will return true.*/
        for(Item item : Item.values()){
            if(item != Item.EMPTY && item != Item.COVERED && item != Item.HIDDEN) {
                playArea.getUncoveredItems().put(item, 10);
            }
        }

        PlayableCard goldCard = new DeckFactory().createDeck(GoldCard.class).drawTop();
        assertFalse(playArea.checkConstraint(goldCard));
    }

    @Test
    void placeResourceCard(){

    }

    @Test
    void placeGoldCard(){

    }
    /*removeFromHand() is a private method*/

    @Test
    void getResourceCardByPos(){
        PlayableCard card = new DeckFactory().createDeck(ResourceCard.class).drawTop();
        Coordinate pos = new Coordinate(1, 1);
        playArea.getPlayedCards().put(pos, card);
        assertEquals(card, playArea.getCardByPos(pos));
    }

    @Test
    void getGoldCardByPos(){
        PlayableCard card = new DeckFactory().createDeck(GoldCard.class).drawTop();
        Coordinate pos = new Coordinate(1, 1);
        playArea.getPlayedCards().put(pos, card);
        assertEquals(card, playArea.getCardByPos(pos));
    }

    @Test
    void getStartCardByPos(){
        Coordinate pos = new Coordinate(0, 0);
        assertEquals(startCard, playArea.getCardByPos(pos));
    }

    @Test
    void addResourceCardToHand(){
        for(PlayableCard card: playArea.getHand()){
            playArea.getHand().remove(card);
        }
        assertTrue(playArea.getHand().isEmpty());

        PlayableCard card = new DeckFactory().createDeck(ResourceCard.class).drawTop();
        playArea.addToHand(card);
        assertTrue(playArea.getHand().contains(card));
    }

    @Test
    void addGoldCardToHand(){
        for(PlayableCard card: playArea.getHand()){
            playArea.getHand().remove(card);
        }
        assertTrue(playArea.getHand().isEmpty());

        PlayableCard card = new DeckFactory().createDeck(GoldCard.class).drawTop();
        playArea.addToHand(card);
        assertTrue(playArea.getHand().contains(card));
    }

    @Test
    void addToHandWhileFull(){
        PlayableCard card = new DeckFactory().createDeck(GoldCard.class).drawTop();
        try{
            playArea.addToHand(card);
        }
        catch(FullHandException e){
            System.out.println("Hand is full!");
        }
    }


    @Test
    void getHandReturnsHand() {
        assertEquals(hand, playArea.getHand());
    }

    @Test
    void getPlayedCardsReturnsPlayedCards() {
        int x = 0;
        int y = 0;
        for(PlayableCard card : playArea.getHand()){
            x++;
            y++;
            Coordinate pos = new Coordinate(x, y);
            playArea.getPlayedCards().put(pos, card);
        }
        assertTrue(playArea.getPlayedCards().values().containsAll(hand));
    }

    @Test
    void getUncoveredItemsShouldReturnCurrentUncoveredItems() {

    }

    @Test
    void getSelectedCardReturnsSelectedCard() {
        EvaluableCard card = new DeckFactory().createDeck(ResourceCard.class).drawTop();
        Coordinate pos = new Coordinate(1, 1);
        playArea.setSelectedCard(pos, card);

        assertEquals(pos, playArea.getSelectedCard().getKey());
        assertEquals(card, playArea.getSelectedCard().getValue());
    }

    @Test
    void getStartCardShouldReturnStartCard() {
        assertEquals(startCard, playArea.getStartCard());
    }
}