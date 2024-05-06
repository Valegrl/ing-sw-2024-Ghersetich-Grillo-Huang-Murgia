package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * An enumeration that represents the type of {@link PlayableCard}.
 */
public enum CardType implements Serializable {
    /**
     * The gold card type.
     */
    GOLD("gold"),

    /**
     * The resource card type.
     */
    RESOURCE("resource");

    /**
     * The card's type.
     */
    private final String type;

    /**
     * Constructs a new CardType with the given type.
     *
     * @param type The type of the {@link PlayableCard}.
     */
    CardType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the type associated to this CardType, in a String representation.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.getType();
    }
}
