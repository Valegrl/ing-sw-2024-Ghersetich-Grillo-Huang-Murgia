package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

public abstract class PlayableCard extends EvaluableCard {

	private final Item permanentResource;

	private Item[] corners;

	private final boolean hasConstraint;



	public void flipCard() {
		for(int i=0;i<4;i++){
			corners[i]=Item.EMPTY;
		}
	}

	public PlayableCard(int id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, boolean hasConstraint) {
		super(id, evaluator, points);
		this.permanentResource = permanentResource;
		this.corners = new Item[4];
		this.hasConstraint = hasConstraint;
	}

	public Item getPermanentResource() {
		return permanentResource;
	}

	public Item[] getCorners() {
		return corners;
	}

	public void setCorner(Item item, int i) {
		this.corners[i] = item;
	}

	public boolean getHasConstraint() {
		return hasConstraint;
	}


}
