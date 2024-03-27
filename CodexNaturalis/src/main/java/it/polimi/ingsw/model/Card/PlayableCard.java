package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

public abstract class PlayableCard extends EvaluableCard {

	private final Item permanentResource;

	private Item[] corners;

	private final CardType cardType;



	public void flipCard() {
		for(int i=0;i<4;i++){
			corners[i]=Item.EMPTY;
		}
	}

	public PlayableCard(int id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, CardType cardType) {
		super(id, evaluator, points);
		this.permanentResource = permanentResource;
		this.corners = new Item[4];
		this.cardType = cardType;
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

	public CardType getCardType() {
		return this.cardType;
	}


}
