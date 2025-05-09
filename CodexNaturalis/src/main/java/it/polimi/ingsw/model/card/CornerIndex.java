package it.polimi.ingsw.model.card;

/**
 * An enumeration that represents the index of a corner in the corners list of a {@link PlayableCard}.
 * Index 0 is the top-left corner and the other corners are indexed clockwise.
 */
public enum CornerIndex {
    /**
     * The top left corner.
     */
    TL(0),

    /**
     * The top right corner.
     */
    TR(1),

    /**
     * The bottom right corner.
     */
    BR(2),

    /**
     * The bottom left corner.
     */
    BL(3);


    /**
     * The corner index in the {@link PlayableCard} corners list.
     */
    private final int index;

    /**
     * Constructs a new CornerIndex with the given index.
     *
     * @param index The index of the corner.
     */
    CornerIndex(int index) {
        this.index = index;
    }

    /**
     * Retrieves the index associated with this CornerIndex.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }
}

