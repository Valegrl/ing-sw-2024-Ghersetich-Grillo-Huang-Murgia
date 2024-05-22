package it.polimi.ingsw.model.player;

import java.io.Serializable;

/**
 * An enumeration that represents the game tokens.
 * They are chosen by each {@link Player player}.
 */
public enum Token implements Serializable {
    /**
     * The red color.
     */
    RED("red"),

    /**
     * The blue color.
     */
    BLUE("blue"),

    /**
     * The green color.
     */
    GREEN("green"),

    /**
     * The yellow color.
     */
    YELLOW("yellow");

    /**
     * The color of a token.
     */
    private final String color;

    /**
     * Constructs a new Token with the given color.
     * @param color The color of the token.
     */
    private Token(String color) {
        this.color = color;
    }

    public static Token fromString(String color) {
        for (Token t : Token.values()) {
            if (t.color.equals(color)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Retrieves the name of the color associated with this Token.
     * @return The name of the color.
     */
    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color;
    }
}
