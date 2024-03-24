package it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.Map;
import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.Item;

public class PlayArea {

	private List hand;

	private Map playedCards;

	private Map uncoveredItems;

	private EvaluableCard selectedCard;

	private PlayableCard[] playableCard;

	private Card[] card;

	private EvaluableCard evaluableCard;

	private Item[] item;

	public void selectCard(Card c) {

	}

	public boolean checkConstraint(PlayableCard c) {
		return false;
	}

	public void placeCard(PlayableCard c, int pos) {

	}

	private void removeFromHand(PlayableCard c) {

	}

	private List newlyCoveredCards() {
		return null;
	}

	public Card getCardByPos(int pos) {
		return null;
	}

	public List getAvailablePos() {
		return null;
	}

	public void addToHand(PlayableCard c) {

	}

}
