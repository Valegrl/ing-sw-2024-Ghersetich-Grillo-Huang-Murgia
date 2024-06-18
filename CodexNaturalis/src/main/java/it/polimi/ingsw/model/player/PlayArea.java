package it.polimi.ingsw.model.player;

import java.util.*;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.Coordinate;

import it.polimi.ingsw.model.exceptions.NonConstraintCardException;
import it.polimi.ingsw.model.exceptions.FullHandException;
import it.polimi.ingsw.model.exceptions.MissingCardFromHandException;
import it.polimi.ingsw.model.exceptions.NoCoveredCardsException;
import it.polimi.ingsw.utils.Pair;

/**
 * A class to represent the {@link it.polimi.ingsw.model.player.Player player}'s playArea.
 * It contains played cards and methods for the game's execution.
 */
public class PlayArea {
//{@link it.polimi.ingsw.model.evaluator.evaluator }
    /**
     * The list of cards the player holds and can play.
     */
    private final List<PlayableCard> hand;

    /**
     * The StartCard chosen at the start.
     * Its Coordinate is (0,0).
     */
    /*Remember that we consider the coordinate (0,0) for this card.*/
    private final StartCard startCard;

    /**
     * The map of the played cards coordinates after the {@link StartCard} and their respective cards.
     */
    private final Map<Coordinate, PlayableCard> playedCards;

    /**
     * The map which represents the player's item inventory and their respective quantities.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The card and its Coordinate selected by the player for placing.
     */
    private Pair<Coordinate, EvaluableCard> selectedCard;

    /**
     * Constructs a new PlayArea with the given starting hand and start card chosen by the player.
     * @param hand The player's initial hand.
     * @param c The player's chosen start card.
     */
    public PlayArea(List<PlayableCard> hand, StartCard c){

        this.hand = hand;

        this.playedCards = new LinkedHashMap<>();

        this.uncoveredItems = new LinkedHashMap<>() {{
            put(Item.PLANT, 0);
            put(Item.ANIMAL, 0);
            put(Item.FUNGI, 0);
            put(Item.INSECT, 0);
            put(Item.QUILL, 0);
            put(Item.INKWELL, 0);
            put(Item.MANUSCRIPT, 0);
        }};

        this.selectedCard = new Pair<>(null, null);

        this.startCard = c;
    }

    /**
     * Flips the start card if signaled by the given parameter.
     * Updates uncovered items based on the visible corners of the chosen side.
     * @param flipped Indicates whether the start card should be flipped {@code true} or not {@code false}.
     */
    public void flipStartCard(boolean flipped){

        StartCard sc = startCard;
        if (flipped)
            sc.flipCard();

        Item[] corners;

        if (flipped){
            for (Item item : sc.getBackPermanentResources())
                uncoveredItems.put(item, uncoveredItems.get(item) + 1);
            corners = sc.getBackCorners();
        } else
            corners = sc.getFrontCorners();

        for (Item item : corners) {
            if (item != Item.EMPTY && item != Item.HIDDEN)
                uncoveredItems.put(item, uncoveredItems.get(item) + 1);

        }
    }

    /**
     * Selects the given {@link EvaluableCard} and its {@link Coordinate}.
     * @param c The card to be selected.
     * @param pos The coordinate of the selected card.
     */
    public void setSelectedCard(Coordinate pos, EvaluableCard c) {
        this.selectedCard = new Pair<>(pos, c);
    }

    /**
     * Checks if the given {@link PlayableCard}'s constraint is satisfied.
     *
     * @param c The card on which the constraint will be checked.
     * @return {@code true} if the player can play the given card, {@code false} if the player can't play it.
     * @throws NonConstraintCardException If the given {@link PlayableCard} does not have a constraint to check.
     */
    public boolean checkConstraint(PlayableCard c) {
        if(c.getCardType() == CardType.RESOURCE)
            throw new NonConstraintCardException();
        else {
            int currCheck;
            for(Item item : c.getConstraint().keySet()){
                currCheck = this.uncoveredItems.get(item);
                if(currCheck < c.getConstraint().get(item))
                    return false;
            }
            return true;
        }
    }

    /**
     * Removes a card from the player's hand.
     * Updates the item counts if the placement of the card is going to cover others cards corners.
     * The played card is then placed into the {@link PlayArea#playedCards} map.
     *
     * @param c The card which is going to be placed.
     * @param pos The coordinates of the card.
     * @throws MissingCardFromHandException If the played card is not present in the player's hand.
     * @throws NoCoveredCardsException If the card doesn't cover any other cards which is impossible.
     */
    public void placeCard(PlayableCard c, Coordinate pos){
        this.removeFromHand(c);
        Coordinate[] coveredCoordinates = newlyCoveredCards(pos);

        Coordinate startCoordinate = new Coordinate(0, 0);
        Coordinate check;
        StartCard sc = startCard;
        Item currItem;
        int j;
        PlayableCard currCard;

        //TODO up to review the two letters standard for corners
        //Corresponding corners of the cards covered by played card
        int[] arrayCorners = {2, 3, 0, 1};

        /*Updating the player's items inventory depending on the cards that have their corner covered*/
        for(CornerIndex i : CornerIndex.values()){
            j = i.getIndex();
            check = coveredCoordinates[j];
            if(check != null){
                if(check.equals(startCoordinate)){
                    if(!sc.isFlipped()){
                        currItem = sc.getFrontCorners()[arrayCorners[j]];
                        uncoveredItems.put(currItem, uncoveredItems.get(currItem) - 1);
                        sc.setFrontCorner(Item.COVERED, arrayCorners[j]);
                    }
                    else{
                        if(sc.getBackCorners()[arrayCorners[j]] != Item.EMPTY){
                            currItem = sc.getBackCorners()[arrayCorners[j]];
                            uncoveredItems.put(currItem, uncoveredItems.get(currItem) - 1);
                        }
                        sc.setBackCorner(Item.COVERED, arrayCorners[j]);
                    }
                }
                else{
                    currCard = playedCards.get(check);
                    if(currCard.getCorners()[arrayCorners[j]] != Item.EMPTY){
                        currItem = currCard.getCorners()[arrayCorners[j]];
                        uncoveredItems.put(currItem, uncoveredItems.get(currItem) - 1);
                    }
                    currCard.setCorner(Item.COVERED, arrayCorners[j]);
                }
            }
        }

        if(c.isFlipped()) {
            currItem = c.getPermanentResource();
            uncoveredItems.put(currItem, uncoveredItems.get(currItem) + 1);
            for(CornerIndex i : CornerIndex.values()) /* redundant; just for test */
                c.setCorner(Item.EMPTY, i.getIndex());
        } else {
            Item[] corners = c.getCorners();
            for(Item item : corners)
                if(item != Item.EMPTY && item != Item.HIDDEN)
                    uncoveredItems.put(item, uncoveredItems.get(item) + 1);
        }

        playedCards.put(pos, c);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param c The card to be removed.
     * @throws MissingCardFromHandException If the card is not present in the player's hand.
     */
    private void removeFromHand(PlayableCard c){
        if(!this.hand.contains(c))
            throw new MissingCardFromHandException();
        else
            this.hand.remove(c);
    }

    /**
     * Calculates which cards will have their corner covered by the played card.
     *
     * @param pos The card's coordinates.
     * @return A list of the covered cards coordinates.
     * @throws NoCoveredCardsException If the card doesn't cover any other cards which is impossible.
     */
    private Coordinate[] newlyCoveredCards(Coordinate pos) {
        Coordinate[] coveredCoordinates = {null, null, null, null};
        Coordinate[] coordinateArray = {
                new Coordinate(-1,1),
                new Coordinate(1,1),
                new Coordinate(1,-1),
                new Coordinate(-1,-1)
        };
        Coordinate startCoordinate = new Coordinate(0, 0);

        boolean flag = false;

        /*The way it's implemented I check the corners of the selected card starting from the TL and ending in BL*/
        for(int i = 0; i < coordinateArray.length; i++){
            Coordinate coordinateCheck = pos.sum(coordinateArray[i]);
            if(this.playedCards.containsKey(coordinateCheck) || coordinateCheck.equals(startCoordinate)){
                coveredCoordinates[i] = coordinateCheck;
                flag = true;
            }
        }

        if(!flag) throw new NoCoveredCardsException();

        return coveredCoordinates;
    }

    /*Exception if there is no card at the parameter coordinate ?*/
    /**
     * Retrieves the player's already played card if passed its matching coordinates.
     * @param pos The coordinates with which it retrieves the associated card.
     * @return The card that matches the given coordinates.
     */
    public Card getCardByPos(Coordinate pos) {
        if(pos.equals(new Coordinate(0, 0)))
            return this.startCard;
        else
            return this.playedCards.get(pos);
    }

    /**
     * Provides a listing of coordinates accessible to a player for card placement.
     * @return A list of coordinates.
     */
    public List<Coordinate> getAvailablePos() {
        //TODO code review
        /*To be used only AFTER the PlayArea has a StartCard*/
        Coordinate[] arrayCoordinate = {
                new Coordinate(-1,1),
                new Coordinate(1,1),
                new Coordinate(1,-1),
                new Coordinate(-1,-1)
        };

        Set<Coordinate> okPos = new HashSet<>();
        Set<Coordinate> notOkPos = new HashSet<>();
        Coordinate startCoordinate = new Coordinate(0, 0);
        StartCard sc = this.startCard;
        Item[] temp;
        int j;

        temp = !sc.isFlipped() ? sc.getFrontCorners() : sc.getBackCorners();
        notOkPos.add(startCoordinate);

        /*check startCard's corners*/
        for(CornerIndex i : CornerIndex.values()) {
            j = i.getIndex();
            if(temp[j] != Item.HIDDEN && temp[j] != Item.COVERED)
                okPos.add(startCoordinate.sum(arrayCoordinate[j]));
            else
                notOkPos.add(startCoordinate.sum(arrayCoordinate[j]));
        }

        for(Coordinate pos : this.playedCards.keySet()) {
            PlayableCard currCard = this.playedCards.get(pos);
            for(CornerIndex i : CornerIndex.values()) {
                j = i.getIndex();
                if(currCard.getCorners()[j] != Item.HIDDEN && currCard.getCorners()[j] != Item.COVERED)
                    okPos.add(pos.sum(arrayCoordinate[j]));
                else
                    notOkPos.add(pos.sum(arrayCoordinate[j]));
            }
        }

        okPos.removeAll(notOkPos);

        return new ArrayList<>(okPos);
    }

    /**
     * Adds a playable card to the player's hand.
     * @param c The card to be added.
     */
    public void addToHand(PlayableCard c) {
        if(this.hand.size() >= 3){
            throw new FullHandException();
        }
        else {
            this.hand.add(c);
        }
    }

    /**
     * Retrieves the player's current hand.
     * @return {@link PlayArea#hand}.
     */
    public List<PlayableCard> getHand(){
        return this.hand;
    }

    /**
     * Retrieves the player's already placed cards.
     * @return {@link PlayArea#playedCards}.
     */
    public Map<Coordinate, PlayableCard> getPlayedCards(){
        return this.playedCards;
    }

    /**
     * Retrieves the player's current item counts.
     * @return {@link PlayArea#uncoveredItems}.
     */
    public Map<Item, Integer> getUncoveredItems(){
        return this.uncoveredItems;
    }

    /**
     * Retrieves the player's current selected card.
     * @return {@link PlayArea#selectedCard}.
     */
    public Pair<Coordinate, EvaluableCard> getSelectedCard(){
        return new Pair<>(selectedCard.key(), selectedCard.value());
    }

    /**
     * Retrieves the player's start card.
     * @return {@link PlayArea#startCard}.
     */
    public StartCard getStartCard() {
        return this.startCard;
    }

    /**
     * Retrieves a card from the player's hand based on the provided card ID.
     *
     * @param playableCardID The ID of the card to be retrieved.
     * @return The card with the matching ID if found in the player's hand, null otherwise.
     */
    public PlayableCard getCardById(String playableCardID) {
        for (PlayableCard card : hand) {
            if (card.getId().equals(playableCardID)) {
                return card;
            }
        }
        return null;
    }
}

