package it.polimi.ingsw.model.Card;

/**
 * An enumeration that represents the type of {@link PlayableCard}.
 */
public enum CardType {
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
}
