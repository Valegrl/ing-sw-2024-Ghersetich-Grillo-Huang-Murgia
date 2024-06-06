package it.polimi.ingsw.model.card;

import java.io.Serializable;
import static it.polimi.ingsw.utils.AnsiCodes.*;

/**
 * An enumeration that represents the game Items.
 * They can be found in a {@link Card card} corner.
 */
public enum Item implements Serializable {
    /**
     * The plant kingdom resource.
     */
    PLANT("plant"),

    /**
     * The animal kingdom resource.
     */
    ANIMAL("animal"),

    /**
     * The fungi kingdom resource.
     */
    FUNGI("fungi"),

    /**
     * The insect kingdom resource.
     */
    INSECT("insect"),

    /**
     * The quill object.
     */
    QUILL("quill"),

    /**
     * The inkwell object.
     */
    INKWELL("inkwell"),

    /**
     * The manuscript object.
     */
    MANUSCRIPT("manuscript"),

    /**
     * The empty corner.
     */
    EMPTY("empty"),

    /**
     * The hidden corner.
     */
    HIDDEN("hidden"),

    /**
     * The corner covered by another card.
     */
    COVERED("covered");

    /**
     * The type of item that can be found in a {@link Card card} corner.
     */
    private final String type;

    /**
     * Constructs a new Item with the given type.
     *
     * @param type The type of the Item.
     */
    Item(String type) {
        this.type = type;
    }

    /**
     * Retrieves the Item enum constant corresponding to the given string representation of the item.
     *
     * @param type The string representation of the item.
     * @return The Item enum constant corresponding to the provided string, or null if no match is found.
     */
    public static Item getItemFromString(String type) {
        return switch (type) {
            case "plant" -> Item.PLANT;
            case "animal" -> Item.ANIMAL;
            case "fungi" -> Item.FUNGI;
            case "insect" -> Item.INSECT;
            case "quill" -> Item.QUILL;
            case "inkwell" -> Item.INKWELL;
            case "manuscript" -> Item.MANUSCRIPT;
            case "empty" -> Item.EMPTY;
            case "hidden" -> Item.HIDDEN;
            case "covered" -> Item.COVERED;
            default -> null;
        };
    }

    /**
     * Converts a String from the default color to a custom color based on the Item.
     * Sets the color to default after printing the colored string.
     * @param item   The item mapped to the color.
     * @param string The string to color.
     * @return The colored string.
     */
    public static String itemToColor(Item item, String string) {
        return switch (item) {

            case Item.PLANT -> (GREEN + string + RESET);

            case Item.ANIMAL -> (CYAN + string + RESET);

            case Item.FUNGI -> (RED + string + RESET);

            case Item.INSECT -> (PURPLE + string + RESET);

            case Item.QUILL, Item.INKWELL, Item.MANUSCRIPT -> (GOLD + string + RESET);

            case Item.EMPTY -> (BEIGE + string + RESET);

            case Item.HIDDEN, Item.COVERED -> string;
            case null -> string;
        };
    }

    /**
     * Prints the Item to a custom color.
     * Sets the color to default after printing the colored Item.
     * @param item The item mapped to the color.
     * @return The colored Item string representation.
     */
    public static String itemToColor(Item item) {
        return switch (item) {

            case Item.PLANT -> (GREEN + Item.PLANT + RESET);

            case Item.ANIMAL -> (CYAN + Item.ANIMAL + RESET);

            case Item.FUNGI -> (RED + Item.FUNGI + RESET);

            case Item.INSECT -> (PURPLE + Item.INSECT + RESET);

            case Item.QUILL -> (GOLD + Item.QUILL + RESET);

            case Item.INKWELL -> (GOLD + Item.INKWELL + RESET);

            case Item.MANUSCRIPT -> (GOLD + Item.MANUSCRIPT + RESET);

            case Item.EMPTY -> (BEIGE + Item.EMPTY + RESET);

            case Item.HIDDEN -> Item.HIDDEN.toString();
            case Item.COVERED -> Item.COVERED.toString();
            case null -> "empty";
        };
    }

    /**
     * Retrieves the type of this Item, in a String representation.
     *
     * @return The type of Item.
     */
    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.getType();
    }
}
