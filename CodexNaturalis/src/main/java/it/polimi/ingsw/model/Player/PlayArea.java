package it.polimi.ingsw.model.Player;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import it.polimi.ingsw.model.Card.*;
import it.polimi.ingsw.model.Evaluator.CornerEvaluator;
import it.polimi.ingsw.utils.Coordinate;

import it.polimi.ingsw.model.exceptions.NonConstraintCardException;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.MissingCardFromHandException;
import it.polimi.ingsw.model.exceptions.NoCoveredCardsException;
import it.polimi.ingsw.utils.Pair;

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
     * The StartCard chosen to start.
     * Its Coordinate is (0,0).
     */
    //TODO check for new implementation, remember that we consider the coordinate (0,0) for this card.
    private final StartCard startCard;

    /**
     * The map of the PlayableCards played after the {@link PlayArea#startCard} in this PlayArea and their respective coordinates.
     */
    private final Map<Coordinate, PlayableCard> playedCards; //TODO check for new implementation

    /**
     * The map of the items a player possesses, items covered by other cards aren't included.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The card and its Coordinate selected by the player for placing.
     */
    private Pair<Coordinate, EvaluableCard> selectedCard; //TODO check for new implementation

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

        this.startCard = c;
    }

    /*Getters should maybe give a copy? It'd be safer*/
    public List<PlayableCard> getHand(){
        return this.hand;
    }

    public Map<Coordinate, PlayableCard> getPlayedCards(){
        return this.playedCards;
    }

    public Map<Item, Integer> getUncoveredItems(){
        return this.uncoveredItems;
    }

    public Pair<Coordinate, EvaluableCard> getSelectedCard(){
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
        }
        else
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
    public void selectCard(Coordinate pos, EvaluableCard c) {
        this.selectedCard.setKey(pos);
        this.selectedCard.setValue(c);
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

    public void placeCard(PlayableCard c, Coordinate pos, boolean flipped) throws MissingCardFromHandException,
            NoCoveredCardsException{
        this.removeFromHand(c);
        Coordinate[] coveredCoordinates = this.newlyCoveredCards(pos);

        Coordinate startCoordinate = new Coordinate(0, 0);
        StartCard sc = (StartCard) this.playedCards.get(startCoordinate);
        Item currItem;
        int currValue;
        PlayableCard currCard;

        //TODO up to review the two letters standard for corners
        int i;
        //Corresponding corners of the cards covered by selected card
        int[] arrayCorners = {2, 3, 0, 1};

        for(i = 0; i < coveredCoordinates.length; i++){
            if(coveredCoordinates[i] != null){
                if(coveredCoordinates[i].equals(startCoordinate)){
                    if(!sc.isFlipped()){
                        currItem = sc.getCorners()[arrayCorners[i]];
                        currValue = this.uncoveredItems.get(currItem);
                        currValue--;
                        this.uncoveredItems.put(currItem, currValue);
                        //TODO corner setter for startCard's array
                    }
                    else{
                        if(sc.getBackCorners()[arrayCorners[i]] != Item.EMPTY){
                            currItem = sc.getBackCorners()[arrayCorners[i]];
                            currValue = this.uncoveredItems.get(currItem);
                            currValue--;
                            this.uncoveredItems.put(currItem, currValue);
                        }
                        //TODO corner setter for startCard's back array
                    }
                }
                else{
                    currCard = (PlayableCard) this.playedCards.get(coveredCoordinates[i]);
                    if(currCard.getCorners()[arrayCorners[i]] != Item.EMPTY) {
                        currItem = currCard.getCorners()[arrayCorners[i]];
                        currValue = this.uncoveredItems.get(currItem);
                        currValue--;
                        this.uncoveredItems.put(currItem, currValue);
                    }
                    currCard.setCorner(Item.HIDDEN, arrayCorners[i]);
                }
            }
        }

        if(flipped){
            currItem = c.getPermanentResource();
            currValue = this.uncoveredItems.get(currItem);
            currValue++;
            this.uncoveredItems.put(currItem, currValue);
            c.flipCard();
        }
        else{
            Item[] corners = c.getCorners();
            for(Item item : corners){
                if(item != Item.EMPTY && item != Item.HIDDEN){
                    currValue = this.uncoveredItems.get(item);
                    currValue++;
                    this.uncoveredItems.put(item, currValue);
                }
            }
        }

        this.playedCards.put(new Coordinate(pos.getX(), pos.getY()), c);
    }

    private void removeFromHand(PlayableCard c) throws MissingCardFromHandException{
        if(!this.hand.contains(c))
            throw new MissingCardFromHandException();
        else
            this.hand.remove(c);
    }

    public Coordinate[] newlyCoveredCards(Coordinate pos) throws NoCoveredCardsException{
        Coordinate[] coveredCoordinates = {null, null, null, null};
        int[] arrayX = {-1, 1, 1, -1};
        int[] arrayY = {1, 1, -1, -1};
        boolean flag = false;

        /*The way it's implemented I check the corners of the selected card starting from the TL and ending in BL*/
        for(int i = 0; i < arrayX.length; i++){
            Coordinate coordinateCheck = new Coordinate(pos.getX() + arrayX[i], pos.getY() + arrayY[i]);
            if(this.playedCards.containsKey(coordinateCheck)) {
                coveredCoordinates[i] = coordinateCheck;
                flag = true;
            }
        }
        if(!flag)
            throw new NoCoveredCardsException();
        else
            return coveredCoordinates;
    }

    public Card getCardByPos(Coordinate pos) {
        return this.playedCards.get(pos);
    }

    public List<Coordinate> getAvailablePos() {
        /*Don't use method newlyCoveredCards, it would be wrong logically!*/
        int[] arrayX = {-1, 1, 1, -1};
        int[] arrayY = {1, 1, -1, -1};
        int i;
        List<Coordinate> availablePos = new ArrayList<>();
        Coordinate startCoordinate = new Coordinate(0, 0);
        StartCard sc = (StartCard) this.playedCards.get(startCoordinate);

        for(Coordinate pos : this.playedCards.keySet()){
            if(pos.equals(startCoordinate)){
                if(!sc.isFlipped()){
                    for(i = 0; i < sc.getCorners().length; i++){
                        if(sc.getCorners()[i] != Item.HIDDEN){
                            availablePos.add(new Coordinate(arrayX[i], arrayY[i]));
                        }
                    }
                }
                else{
                    for(i = 0; i < sc.getBackCorners().length; i++){
                        if(sc.getBackCorners()[i] != Item.HIDDEN){
                            availablePos.add(new Coordinate(arrayX[i], arrayY[i]));
                        }
                    }
                }
            }
            //If current Card is not the starting Card I have to keep in mind that current Card corners might
            // be covering other cards' corners
            else{
                PlayableCard currCard = (PlayableCard) this.playedCards.get(pos);
                for(i = 0; i < currCard.getCorners().length; i++){
                    if(!this.playedCards.containsKey(new Coordinate(pos.getX() + arrayX[i], pos.getY() + arrayY[i]))
                    && currCard.getCorners()[i] != Item.HIDDEN){
                        availablePos.add(new Coordinate(pos.getX() + arrayX[i], pos.getY() + arrayY[i]));
                    }
                }
            }
        }
        return availablePos;
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

