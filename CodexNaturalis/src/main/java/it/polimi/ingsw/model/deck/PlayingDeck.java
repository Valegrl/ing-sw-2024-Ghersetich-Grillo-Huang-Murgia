package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.exceptions.NullVisibleCardException;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a deck of {@link PlayableCard playable cards}, extending a generic deck.
 *
 * @param <U> The type of {@link PlayableCard playable cards} in the playing deck.
 *            It must extend the PlayableCard class.
 */
public class PlayingDeck<U extends PlayableCard> extends Deck<U> {
    /**
     * The array of visible cards on the table.
     */
    private final U[] visibleCards;

    /**
     * Constructs a new playing deck with the specified list of cards and array of visible cards.
     *
     * @param deck         The list of cards to initialize the playing deck with.
     * @param visibleCards The array of visible cards to initialize the playing deck with.
     */
    public PlayingDeck(List<U> deck, U[] visibleCards) {
        super(deck);
        this.visibleCards = visibleCards;
    }

    /**
     * Draws the visible card at the specified index from the playing deck.
     * Then draws a new card from the top of the deck, if possible, to replace the drawn visible card.
     *
     * @param chosenCard The index of the visible card to draw. Must be between 0 and {@link PlayingDeck#visibleCards}'s size - 1.
     * @return           The drawn visible card that can't be null.
     */
    public U drawVisible(int chosenCard) {
        if (chosenCard < 0 || chosenCard >= this.visibleCards.length) {
            throw new ArrayIndexOutOfBoundsException("Index is out of range: " + chosenCard);
        }
        U draw = this.visibleCards[chosenCard];
        if (draw == null) throw new NullVisibleCardException();

        this.visibleCards[chosenCard] = drawTop(); // null if empty deck
        return draw;
    }

    /**
     * Retrieves the {@link Item permanent resource} from the top {@link PlayableCard} of the playing deck.
     *
     * @return An {@link Item} representing the playing deck's top card permanent resource.
     */
    public Item getTopResource() {
        return this.deck.getFirst().getPermanentResource();
    }

    /**
     * Retrieves a copy of the array of visible cards in the playing deck.
     *
     * @return A copy of the array of visible cards.
     */
    public U[] getVisibleCards() {
        return Arrays.copyOf(visibleCards, visibleCards.length);
    }

    @Override
    public String toString() {
        return "{\"PlayingDeck\":{"
                + "        \"visibleCards\":" + Arrays.toString(visibleCards)
                + ",         \"deck\":" + deck
                + "}}";
    }

}