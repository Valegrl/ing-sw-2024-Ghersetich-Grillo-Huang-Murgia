package it.polimi.ingsw.model.Card;

import java.util.List;

/**
 * A class to represent a specific type of card, a Start card.
 */
public class StartCard extends Card {
	/**
	 * The fixed resources of each start card, valid through the entire game.
	 */
	private final List<Item> backPermanentResources;

	/**
	 * The Items on each corner of a Start card.
	 */
	private Item[] corners;

	/**
	 * The Items on each corner of the back of a Start card.
	 */
	private Item[] backCorners;

	/**
	 * Shows if the showing side of the card was changed. Flipped is false if the showing side is the front of the start card, true if the showing side is the back of a Start card.
	 */
	private boolean flipped = false;

	/**
	 * Changes the showing side in which the Start card will be placed in the Play Area.
	 */
	public void flipCard() {
		flipped=!flipped;
	}

	/**
	 * Constructs a new Start Card.
	 * @param id A unique integer associated with each card.
	 * @param backPermanentResources The list of fixed resources of a Start card.
	 */
	public StartCard(String id, List<Item> backPermanentResources) {
		super(id);
		this.backPermanentResources = backPermanentResources;
		this.corners = new Item[4];
		this.backCorners = new Item[4];
	}

	/**
	 * Checks the showing side of a placed card.
	 * @return The current state of the card, whether it's flipped or not.
	 */
	public boolean isFlipped() {
		return flipped;
	}

	/**
	 * Retrieves the permanent resources of a Start card.
	 * @return {@link StartCard#backPermanentResources}.
	 */
	public List<Item> getBackPermanentResources() {
		return backPermanentResources;
	}

	/**
	 * Retrieves the list of Items in each corner of a Start card.
	 * @return {@link StartCard#corners}.
	 */
	public Item[] getCorners() {
		return corners;
	}

	public void setCorner(Item item, int i) {
		this.corners[i] = item;
	}
	/**
	 * Retrieves the list of Items in each corner of the back of a Start card.
	 * @return {@link StartCard#backCorners}.
	 */
	public Item[] getBackCorners() {
		return backCorners;
	}

	public void setBackCorner(Item item, int i) {
		this.backCorners[i] = item;
	}
}
