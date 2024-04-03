package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

/**
 * A class to represent a specific type of card, a Playable card.
 */
public abstract class PlayableCard extends EvaluableCard {
	/**
	 * The fixed resource of a Playable card, valid through the entire game.
	 */
	private final Item permanentResource;

	/**
	 * The Items on each corner of a Start card.
	 */
	private Item[] corners;

	/**
	 * Shows if the card has a constraint to get points.
	 */
	private final boolean hasConstraint;

	/**
	 * Sets each corner to the Item EMPTY.
	 */
	public void flipCard() {
		for(int i=0;i<4;i++){
			corners[i]=Item.EMPTY;
		}
	}

	/**
	 * Constructs a new Playable
	 * @param id A unique integer associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 * @param permanentResource The list of Items in each corner of the back of a Start card.
	 * @param hasConstraint The showing side of a Start card.
	 */
	public PlayableCard(int id, Evaluator evaluator, int points, Item permanentResource, boolean hasConstraint) {
		super(id, evaluator, points);
		this.permanentResource = permanentResource;
		this.corners = new Item[4];
		this.hasConstraint = hasConstraint;
	}

	/**
	 * Retrieves the permanent resources of a Start card.
	 * @return {@link PlayableCard#permanentResource}.
	 */
	public Item getPermanentResource() {
		return permanentResource;
	}

	/**
	 * Retrieves items on the corners of each card.
	 * @return {@link PlayableCard#corners}.
	 */
	public Item[] getCorners() {
		return corners;
	}

	/**
	 * Sets an Item on each corner of a Playable card.
	 * @param  item The Item required for a corner
	 * @param  i Iterates through the four corners of a card.
	 */
	public void setCorner(Item item, int i) {
		this.corners[i] = item;
	}

	public boolean getHasConstraint() {
		return hasConstraint;
	}
}
