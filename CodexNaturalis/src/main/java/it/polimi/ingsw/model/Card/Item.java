package it.polimi.ingsw.model.Card;

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
     * The corner covered by another Card.
     */
    COVERED("covered");

    /**
     * The type of item that can be found in a {@link Card card} corner.
     */
    private final String type;

    /**
     * Constructs a new Item with the given type.
     * @param type The type of the Item.
     */
    private Item(String type) {
        this.type = type;
    }

    /**
     * TODO
     * @param type
     * @return
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
     * TODO
     * @return
     */
    public String get() {
        return this.type;
    }
}
