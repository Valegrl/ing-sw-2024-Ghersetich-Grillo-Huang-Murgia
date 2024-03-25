package it.polimi.ingsw.model.Card;

import java.util.List;

/**
 * A class to represent a specific type of card, a Start card.
 */
public class StartCard extends Card {

	private final List<Item> backPermanentResources;

	private Item[] corners;

	private Item[] backCorners;

	private boolean flipped;

	public void flipCard() {
		flipped=!flipped;
	}


	public StartCard(int id, List<Item> backPermanentResources, Item[] corners, Item[] backCorners, boolean flipped) {
		super(id);
		this.backPermanentResources = backPermanentResources;
		this.corners = corners;
		this.backCorners = backCorners;
		this.flipped = flipped;
	}

	public List<Item> getBackPermanentResources() {
		return backPermanentResources;
	}

	public Item[] getCorners() {
		return corners;
	}

	public void setCorners(Item[] corners) {
		this.corners = corners;
	}

	public Item[] getBackCorners() {
		return backCorners;
	}

	public void setBackCorners(Item[] backCorners) {
		this.backCorners = backCorners;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

}
