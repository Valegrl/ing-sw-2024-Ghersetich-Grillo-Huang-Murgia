package it.polimi.ingsw.model.Deck;

import it.polimi.ingsw.model.Card.Card;

public abstract interface Deck {

	public abstract void newVisibleCard();

	public abstract Card drawVisible();

	public abstract void drawTop();

}
