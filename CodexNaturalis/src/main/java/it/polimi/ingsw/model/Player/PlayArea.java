package it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import it.polimi.ingsw.model.Card.EvaluableCard;
import it.polimi.ingsw.model.Card.PlayableCard;
import it.polimi.ingsw.model.Card.Card;
import it.polimi.ingsw.model.Card.Item;
import it.polimi.ingsw.utils.Coordinate;

import it.polimi.ingsw.model.exceptions.NonCostraintCardException;

public class PlayArea {

    /**
     *The list of cards the player can play
     */
    private final List<PlayableCard> hand;

    /**
     *The map of the cards the player played previously
     */
    private final Map<Coordinate, Card> playedCards;

    /**
     *The map of the items a player possess, items covered by other cards aren't included
     */
    private final Map<Item, Integer> uncoveredItems;

    private EvaluableCard selectedCard;

    public PlayArea(){

        /*It should have a parameter that builds the player's hand,
        do I build the cards in here or is it the game?'s responsability*/
        this.hand = new ArrayList<>();

        this.playedCards = new HashMap<>();

        this.uncoveredItems = new HashMap<>();
        uncoveredItems.put(Item.PLANT, 0);
        uncoveredItems.put(Item.ANIMAL, 0);
        uncoveredItems.put(Item.FUNGI, 0);
        uncoveredItems.put(Item.INSECT, 0);
        uncoveredItems.put(Item.QUILL, 0);
        uncoveredItems.put(Item.INKWELL, 0);
        uncoveredItems.put(Item.MANUSCRIPT, 0);

        this.selectedCard = null;
    }

    public List<PlayableCard> getHand(){
        return this.hand;
    }

    public Map<Coordinate, Card> getPlayedCards(){
        return this.playedCards;
    }

    public Map<Item, Integer> getUncoveredItems(){
        return this.uncoveredItems;
    }

    public void selectCard(Card c) {
        this.selectedCard = (EvaluableCard) c;
    }

    /**
     *
     * @param c the card that needs to be checked for constraints
     * @return true if the player can play it, false if the player can't play it
     */
    public boolean checkConstraint(PlayableCard c) throws NonCostraintCardException{
        if(c.getHasConstraint() == false){
            throw new NonCostraintCardException();
        }
        else {
            //TODO check if the player's uncovereditems can suffice for the constraint
            return false;
        }
    }

    public void placeCard(PlayableCard c, Coordinate pos, boolean flipped) {

    }

    private void removeFromHand(PlayableCard c) {
        Iterator<PlayableCard> iterator = getHand().iterator();
        PlayableCard iteratedCard = iterator.next();
        if(iteratedCard.equals(c)){
            iterator.remove();
        }
    }

    private List<Coordinate> newlyCoveredCards() {
        return null;
    }

    public Card getCardByPos(Coordinate pos) {
        return this.playedCards.get(pos);
    }

    public List<Coordinate> getAvailablePos() {
        return null;
    }

    public void addToHand(PlayableCard c) {
        hand.add(c);
        //TODO handle exceptions (ie hand is bigger or equal to 3 cards)
    }

}
