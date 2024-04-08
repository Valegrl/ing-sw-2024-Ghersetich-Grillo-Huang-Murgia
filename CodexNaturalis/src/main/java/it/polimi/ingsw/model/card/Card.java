package it.polimi.ingsw.model.card;

import java.util.Objects;

/**
 * A class to represent all possible cards in the game.
 */
public abstract class Card {
    /**
     * A String-format unique identifier that distinguishes each and every card in the game.
     */
    private final String id;

    /**
     * Constructs a new card with the given identifier.
     *
     * @param id A unique String associated with each card.
     */
    public Card(String id) {
        this.id = id;
    }

    /**
     * Retrieves the identifier of a card.
     *
     * @return {@link Card#id}.
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Card that = (Card) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

