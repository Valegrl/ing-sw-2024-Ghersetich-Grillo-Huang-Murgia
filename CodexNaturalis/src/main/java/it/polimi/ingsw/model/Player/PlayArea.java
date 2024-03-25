package it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.Item;
import it.polimi.ingsw.utils.Coordinate;

public class PlayArea {

    private List<PlayableCard> hand;

    private Map<Coordinate, Card> playedCards;

    private Map<Item, Integer> uncoveredItems;

    private EvaluableCard selectedCard;

    public void selectCard(Card c) {

    }

    public boolean checkConstraint(PlayableCard c) {
        return false;
    }

    public void placeCard(PlayableCard c, int pos) {

    }

    private void removeFromHand(PlayableCard c) {

    }

    private List<Coordinate> newlyCoveredCards() {
        return null;
    }

    public Card getCardByPos(int pos) {
        return null;
    }

    public List<Coordinate> getAvailablePos() {
        return null;
    }

    public void addToHand(PlayableCard c) {

    }

}
