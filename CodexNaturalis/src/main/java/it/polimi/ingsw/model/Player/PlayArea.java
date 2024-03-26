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
import it.polimi.ingsw.model.Card.StartCard;

import it.polimi.ingsw.model.exceptions.NonConstraintCardException;
import it.polimi.ingsw.model.exceptions.FullHandException;

/**
 * A class to represent the {@link it.polimi.ingsw.model.Player.Player Player}'s playArea.
 * It contains played cards and methods for the game's execution.
 */
public class PlayArea {
//{@link it.polimi.ingsw.model.Evaluator.Evaluator }
    /**
     * The list of cards the player holds and can play.
     */
    private final List<PlayableCard> hand;

    /**
     * The map of the cards the player played previously and their respective coordinates.
     */
    private final Map<Coordinate, Card> playedCards;

    /**
     * The map of the items a player possess, items covered by other cards aren't included.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The card selected by the player for placing.
     */
    private EvaluableCard selectedCard;

    public PlayArea(List<PlayableCard> hand, StartCard c){

        //TODO It should have a parameter that builds the initial hand from game instead
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

    public EvaluableCard getSelectedCard(){
        return this.selectedCard;
    }

    public void flipStartCard(boolean flipped){
    /*riceve un boolean che se è true chiama StartCard.flipCard() e aggiorna di conseguenza gli item del player*/
    }

    public void selectCard(Card c) {
        this.selectedCard = (EvaluableCard) c;
    }

    /**
     *
     * @param c the card that needs to be checked for constraints
     * @return true if the player can play it, false if the player can't play it
     */
    public boolean checkConstraint(PlayableCard c) throws NonConstraintCardException{
        if(c.getHasConstraint() == false){
            throw new NonConstraintCardException();
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

    //TODO requires parameter to know what card has been just placed
    private List<Coordinate> newlyCoveredCards() {
        return null;
    }

    public Card getCardByPos(Coordinate pos) {
        return this.playedCards.get(pos);
    }

    public List<Coordinate> getAvailablePos() {
        return null;
    }

    public void addToHand(PlayableCard c) throws FullHandException {
        if(this.hand.size() >= 3){
            throw new FullHandException();
        }
        else
            this.hand.add(c);
    }

}
