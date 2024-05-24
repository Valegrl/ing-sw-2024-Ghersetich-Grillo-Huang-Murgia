package it.polimi.ingsw.model.player;

import it.polimi.ingsw.utils.AnsiCodes;

import java.io.Serializable;

/**
 * An enumeration that represents the game tokens.
 * They are chosen by each {@link Player player}.
 */
public enum Token implements Serializable {
    /**
     * The red color.
     */
    RED("red", AnsiCodes.RED_TOKEN),

    /**
     * The blue color.
     */
    BLUE("blue", AnsiCodes.BLUE_TOKEN),

    /**
     * The green color.
     */
    GREEN("green", AnsiCodes.GREEN_TOKEN),

    /**
     * The yellow color.
     */
    YELLOW("yellow", AnsiCodes.YELLOW_TOKEN);

    /**
     * The color of a token.
     */
    private final String color;

    private final String colorCode;

    /**
     * Constructs a new Token with the given color.
     * @param color The color of the token.
     */
    private Token(String color, String colorCode) {
        this.color = color;
        this.colorCode = colorCode;
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
        return colorCode + color + AnsiCodes.RESET;
    }
}
