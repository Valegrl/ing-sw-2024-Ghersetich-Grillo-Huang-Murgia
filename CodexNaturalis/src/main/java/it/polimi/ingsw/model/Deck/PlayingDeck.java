package it.polimi.ingsw.model.Deck;

import it.polimi.ingsw.model.Card.Item;
import it.polimi.ingsw.model.Card.PlayableCard;

import java.util.Arrays;
import java.util.List;

public class PlayingDeck<U extends PlayableCard> extends Deck<U> {
    private final U[] visibleCards;

    public PlayingDeck(List<U> deck, U[] visibleCards) {
        //TODO correct implementation
        super(deck);
        this.visibleCards = visibleCards;
    }

    public void newVisibleCard(){
        //TODO implementation
    }

    public U drawVisible(int chosenCard){
        //TODO correct implementation
        U draw = visibleCards[chosenCard];
        visibleCards[chosenCard] = null;

        return draw;
    }

    public Item getTopResource(){
        //TODO review implementation
        return deck.getLast().getPermanentResource();
    }

    public U[] getVisibleCards() {
        return Arrays.copyOf(visibleCards, visibleCards.length);
    }

}