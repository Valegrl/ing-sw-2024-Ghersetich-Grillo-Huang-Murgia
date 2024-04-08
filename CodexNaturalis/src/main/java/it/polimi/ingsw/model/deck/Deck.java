package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.Card;

import java.util.List;

/**
 * A class to represent a generic deck of cards.
 *
 * @param <T> The type of cards in the deck. It must extend the {@link Card} class.
 */
public class Deck<T extends Card> {
    /**
     * The list of {@link Card cards} in the deck.
     */
    protected final List<T> deck;

    /**
     * Constructs a new deck with the specified list of cards.
     *
     * @param deck The list of cards to initialize the deck with.
     */
    public Deck(List<T> deck) {
        this.deck = deck;
    }

    /**
     * Draws the top card from the deck, if present, represented by the first {@link Card} of the list.
     *
     * @return The top card from the deck, or null if the deck is empty.
     */
    public T drawTop() {
        T card = null;
        if (!deck.isEmpty())
            card = deck.removeFirst();
        return card;
    }

    /**
     * Retrieves the number of cards remained in the deck.
     *
     * @return The size of the deck.
     */
    public int getSize() {
        return deck.size();
    }

    @Override
    public String toString() {
        return "{\"Deck\":{"
                + "        \"deck\":" + deck
                + "}}";
    }

}