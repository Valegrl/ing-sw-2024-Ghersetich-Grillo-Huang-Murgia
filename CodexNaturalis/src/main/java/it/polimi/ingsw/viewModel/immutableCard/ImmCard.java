package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents an immutable version of a Card.
 * It is used to provide a read-only view of the card data.
 */
public class ImmCard implements Serializable {
    /**
     * The unique identifier of the card.
     */
    private final String id;

    /**
     * Constructs an immutable representation of a card from a model's {@link Card}.
     *
     * @param card The card to represent.
     */
    public ImmCard(Card card) {
        this.id = card.getId();
    }

    /**
     * Retrieves the card's unique identifier.
     * @return {@link ImmCard#id}.
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
