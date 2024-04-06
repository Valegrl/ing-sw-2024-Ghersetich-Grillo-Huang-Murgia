package it.polimi.ingsw.model.Deck;

import it.polimi.ingsw.model.Card.Card;
import java.util.List;

class Deck<T extends Card> {

    protected final List<T> deck;


    public Deck(List<T> deck) {
        //TODO correct implementation
        this.deck = deck;
    }

    public T drawTop(){
        //TODO correct implementation
        return deck.removeLast();
    }

    public int getSize(){
        return deck.size();
    }

}