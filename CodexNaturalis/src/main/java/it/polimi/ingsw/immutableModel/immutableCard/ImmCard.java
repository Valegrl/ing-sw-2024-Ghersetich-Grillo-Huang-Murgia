package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Objects;

/**
 * This class represents an immutable version of a Card.
 * It is used to provide a read-only view of the card data.
 * The class is final, so it can't be extended.
 */
public class ImmCard {
    /**
     * The unique identifier of the card.
     */
    private final String id;

    /**
     * Constructs an immutable representation of a card.
     *
     * @param card the card to represent
     */
    public ImmCard(Card card) {
        this.id = card.getId();
    }

    /**
     * This method allows access to the ID of the card, which is a unique string that identifies the card.
     *
     * @return the unique identifier of the card
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
        ImmCard that = (ImmCard) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
