package it.polimi.ingsw.model.card;

/**
 * An enumeration that represents the game Items.
 * They can be found in a {@link Card card} corner.
 */
public enum Item {
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
            /*
             * Green color for plants then resets color to default.
             * Then resets color to default.
             */
            case Item.PLANT -> ("\u001B[32m" + string + "\u001B[0m");

            /*
             * Cyan color for animals.
             * Then resets color to default.
             */
            case Item.ANIMAL -> ("\u001B[36m" + string + "\u001B[0m");

            /*
             * Red color for fungi.
             * Then resets color to default.
             */
            case Item.FUNGI -> ("\u001B[31m" + string + "\u001B[0m");

            /*
             * Purple color for insects.
             * Then resets color to default.
             */
            case Item.INSECT -> ("\u001B[35m" + string + "\u001B[0m");

            /*
             * Yellow color for quills.
             * Then resets color to default.
             */
            case Item.QUILL -> ("\u001B[33m" + string + "\u001B[0m");

            /*
             * Yellow color for inkwells.
             * Then resets color to default.
             */
            case Item.INKWELL -> ("\u001B[33m" + string + "\u001B[0m");

            /*
             * Yellow color for manuscripts.
             * Then resets color to default.
             */
            case Item.MANUSCRIPT -> ("\u001B[33m" + string + "\u001B[0m");
            case Item.EMPTY -> ("\u001B[32m" + string + "\u001B[0m");
            case Item.HIDDEN -> ("\u001B[32m" + string + "\u001B[0m");
            case Item.COVERED -> ("\u001B[32m" + string + "\u001B[0m");
            default -> null;
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
            /*
             * Green color for plants.
             * Then resets color to default.
             */
            case Item.PLANT -> ("\u001B[32m" + Item.PLANT + "\u001B[0m");

            /*
             * Cyan color for animals.
             * Then resets color to default.
             */
            case Item.ANIMAL -> ("\u001B[36m" + Item.ANIMAL + "\u001B[0m");

            /*
             * Red color for fungi.
             * Then resets color to default.
             */
            case Item.FUNGI -> ("\u001B[31m" + Item.FUNGI + "\u001B[0m");

            /*
             * Purple color for insects.
             * Then resets color to default.
             */
            case Item.INSECT -> ("\u001B[35m" + Item.INSECT + "\u001B[0m");

            /*
             * Yellow color for quills.
             * Then resets color to default.
             */
            case Item.QUILL -> ("\u001B[33m" + Item.QUILL + "\u001B[0m");

            /*
             * Yellow color for inkwells.
             * Then resets color to default.
             */
            case Item.INKWELL -> ("\u001B[33m" + Item.INKWELL + "\u001B[0m");

            /*
             * Yellow color for manuscripts.
             * Then resets color to default.
             */
            case Item.MANUSCRIPT -> ("\u001B[33m" + Item.MANUSCRIPT + "\u001B[0m");


            case Item.EMPTY -> ("\u001B[32m" + Item.EMPTY + "\u001B[0m");
            case Item.HIDDEN -> ("\u001B[32m" + Item.HIDDEN + "\u001B[0m");
            case Item.COVERED -> ("\u001B[32m" + Item.COVERED + "\u001B[0m");
            default -> null;
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
