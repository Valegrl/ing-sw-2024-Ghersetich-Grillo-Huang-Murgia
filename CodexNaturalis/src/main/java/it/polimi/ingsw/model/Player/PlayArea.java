package it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import it.polimi.ingsw.model.Card.*;
import it.polimi.ingsw.utils.Coordinate;

import it.polimi.ingsw.model.exceptions.NonConstraintCardException;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.MissingCardFromHandException;

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
     * The map of the cards played in this PlayArea and their respective coordinates.
     */
    private final Map<Coordinate, Card> playedCards;

    /**
     * The map of the items a player possesses, items covered by other cards aren't included.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The card selected by the player for placing.
     */
    private EvaluableCard selectedCard;

    public PlayArea(List<PlayableCard> hand, StartCard c){

        this.hand = hand;

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

        Coordinate coordinate = new Coordinate(0, 0);
        playedCards.put(coordinate, c);
    }

    /*Getters should maybe give a copy? It'd be safer*/
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

    /**
     * Flips the start card if signaled by the given parameter.
     * Updates uncovered items based on the visible corners of the chosen side.
     * @param flipped flipped Indicates whether the start card should be flipped (true) or not (false).
     */
    public void flipStartCard(boolean flipped){

        StartCard sc;
        sc = (StartCard) this.playedCards.get(new Coordinate(0, 0));
        if (flipped)
            sc.flipCard();

        Item[] corners;
        int currItems;
        if (flipped){
            for (Item item : sc.getBackPermanentResources())
                this.uncoveredItems.put(item, 1);
            corners = sc.getBackCorners();
        } else
            corners = sc.getCorners();

        for (Item item : corners) {
            if (item != Item.EMPTY && item != Item.HIDDEN) {
                currItems = this.uncoveredItems.get(item);
                currItems++;
                this.uncoveredItems.put(item, currItems);
            }
        }
    }

    /**
     * Selects the given {@link EvaluableCard}.
     * @param c The card to be selected.
     */
    public void selectCard(EvaluableCard c) {
        this.selectedCard = c;
    }

    /**
     * Checks if the given {@link PlayableCard}'s constraint is satisfied.
     *
     * @param c The card on which the constraint will be checked.
     * @return true if the player can play the given card, false if the player can't play it.
     * @throws NonConstraintCardException If the given {@link PlayableCard} does not have a constraint to check.
     */
    public boolean checkConstraint(PlayableCard c) throws NonConstraintCardException{
        if(c.getCardType() == CardType.RESOURCE)
            throw new NonConstraintCardException();
        else {
            GoldCard goldCard = (GoldCard) c;
            int currCheck;
            for(Item item : goldCard.getConstraint().keySet()){
                currCheck = this.uncoveredItems.get(item);
                if(currCheck < goldCard.getConstraint().get(item))
                    return false;
            }
            return true;
        }
    }

    public void placeCard(PlayableCard c, Coordinate pos, boolean flipped) throws MissingCardFromHandException{
        if((pos.getX() == 1 || pos.getX() == -1) && (pos.getY() == 1 || pos.getY() == -1)){
            StartCard sc = (StartCard) this.playedCards.get(new Coordinate(0,0));
            if(sc.isFlipped()){
                //TODO implement the method
            }
        }
        removeFromHand(c);


    }

    private void removeFromHand(PlayableCard c) throws MissingCardFromHandException{
        if(!this.hand.contains(c))
            throw new MissingCardFromHandException();
        else
            this.hand.remove(c);
    }

    private List<Coordinate> newlyCoveredCards(Coordinate pos) {
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
        else {
            this.hand.add(c);
        }
    }

}
