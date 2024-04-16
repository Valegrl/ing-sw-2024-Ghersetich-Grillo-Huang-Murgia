package it.polimi.ingsw.model.card;

import java.util.List;

/**
 * A class to represent a specific type of card, a Start card.
 */
public class StartCard extends Card {
    /**
     * The fixed resources of each start card,
     * valid through the entire game only if the card is flipped.
     */
    private final List<Item> backPermanentResources;

    /**
     * The {@link Item Items} contained on each corner of a Start card's front side.
     */
    private final Item[] frontCorners;

    /**
     * The {@link Item Items} contained on each corner of a Start card's back side.
     */
    private final Item[] backCorners;

    /**
     * Indicates the current showing side of the StartCard.
     * Flipped is false if the card is displayed with the front side,
     * true if the card is displayed with the back side.
     */
    private boolean flipped;

    /**
     * Constructs a new StartCard with the given parameters.
     *
     * @param id                     The unique identifier for the StartCard.
     * @param backPermanentResources The list of permanent resources on the back of the StartCard.
     * @param frontCorners           The array of Items on the front corners of the StartCard.
     * @param backCorners            The array of Items on the back corners of the StartCard.
     * @param flipped                The initial flipped state of the StartCard. If true, the StartCard is considered flipped.
     */
    public StartCard(String id, List<Item> backPermanentResources, Item[] frontCorners, Item[] backCorners, boolean flipped) {
        super(id);
        this.backPermanentResources = backPermanentResources;
        this.frontCorners = frontCorners;
        this.backCorners = backCorners;
        this.flipped = flipped;
    }

    /**
     * Changes the showing side of the StartCard in the {@link it.polimi.ingsw.model.player.PlayArea PlayArea}.
     */
    public void flipCard() {
        flipped = !flipped;
    }

    /**
     * Retrieves the showing side of the StartCard.
     * true if it's flipped,
     * false otherwise.
     *
     * @return The current showing side of the card, whether it's flipped or not.
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Retrieves the permanent resources of a Start card.
     *
     * @return {@link StartCard#backPermanentResources}.
     */
    public List<Item> getBackPermanentResources() {
        return backPermanentResources;
    }

    /**
     * Retrieves the array of {@link Item}s contained on each corner of a Start card's front side.
     *
     * @return {@link StartCard#frontCorners}.
     */
    public Item[] getFrontCorners() {
        return frontCorners;
    }

    /**
     * Sets the item at the specified index in the frontCorners array.
     *
     * @param item The item to set.
     * @param i    The index at which to set the item.
     * @throws ArrayIndexOutOfBoundsException if the index is out of range (i < 0 || i >= backCorners.length).
     */
    public void setFrontCorner(Item item, int i) {
        if (i < 0 || i >= frontCorners.length) {
            throw new ArrayIndexOutOfBoundsException("Index is out of range: " + i);
        }
        this.frontCorners[i] = item;
    }

    /**
     * Retrieves the array of {@link Item}s contained on each corner of a Start card's back side.
     *
     * @return {@link StartCard#backCorners}.
     */
    public Item[] getBackCorners() {
        return backCorners;
    }

    /**
     * Sets the item at the specified index in the back corners array.
     *
     * @param item The item to set.
     * @param i    The index at which to set the item.
     * @throws ArrayIndexOutOfBoundsException if the index is out of range (i < 0 || i >= backCorners.length).
     */
    public void setBackCorner(Item item, int i) {
        if (i < 0 || i >= backCorners.length) {
            throw new ArrayIndexOutOfBoundsException("Index is out of range: " + i);
        }
        this.backCorners[i] = item;
    }

}
