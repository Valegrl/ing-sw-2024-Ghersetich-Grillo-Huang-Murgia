package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

import java.util.Map;

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
	 * Sets each corner to the Item EMPTY.
	 */
	public void flipCard() {
		for(int i=0;i<4;i++){
			corners[i]=Item.EMPTY;
		}
	}

	/**
	 * Constructs a new Playable
	 * @param id A unique String associated with each card.
	 * @param evaluator The card's specific {@link Evaluator evaluator}.
	 * @param points The amount of points associated with each card.
	 * @param permanentResource The list of Items in each corner of the back of a Start card.
	 */
	public PlayableCard(String id, Evaluator evaluator, int points, Item permanentResource) {
		super(id, evaluator, points);
		this.permanentResource = permanentResource;
		this.corners = new Item[4];
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

	/**
	 * Retrieves the number of occurrences of each resource to be able to score points.
	 */
	public Map<Item, Integer> getConstraint() {
		return null;
	}
}
