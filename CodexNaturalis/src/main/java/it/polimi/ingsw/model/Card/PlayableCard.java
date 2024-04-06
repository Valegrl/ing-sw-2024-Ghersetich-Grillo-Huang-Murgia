package it.polimi.ingsw.model.Card;

import it.polimi.ingsw.model.Evaluator.Evaluator;

import java.util.Map;

/**
 * A class to represent a specific type of card, a Playable card.
 * These Cards can be played in a {@link it.polimi.ingsw.model.Player.Player Player}'s {@link it.polimi.ingsw.model.Player.PlayArea PlayArea}.
 */
public abstract class PlayableCard extends EvaluableCard {
    /**
     * The permanent resource of a Playable card.
     * It is visible through the entire game if the card is placed on its back side.
     */
    private final Item permanentResource;

    /**
     * The {@link Item Items} contained on each corner of a card.
     */
    private final Item[] corners;

    /**
     * The type of PlayableCard.
     * It can be {@link CardType#GOLD gold} or {@link CardType#RESOURCE resource}.
     */
    private final CardType type;

    /**
     * Constructs a new PlayableCard.
     *
     * @param id                A unique String associated with each card.
     * @param evaluator         The card's specific {@link Evaluator evaluator}.
     * @param points            The number of points associated with each card.
     * @param permanentResource The permanent resource of a PlayableCard.
     * @param corners           The items contained on each corner of the card.
     * @param type              The type of this PlayableCard.
     */
    public PlayableCard(String id, Evaluator evaluator, int points, Item permanentResource, Item[] corners, CardType type) {
        super(id, evaluator, points);
        this.permanentResource = permanentResource;
        this.corners = corners;
        this.type = type;
    }

    /**
     * Flips the card by setting each corner as {@link Item#EMPTY empty}.
     */
    public void flipCard() {
        for (CornerIndex c : CornerIndex.values()) {
            corners[c.getIndex()] = Item.EMPTY;
        }
    }

    /**
     * Retrieves the permanent resource of a Start card.
     *
     * @return {@link PlayableCard#permanentResource}.
     */
    public Item getPermanentResource() {
        return permanentResource;
    }

    /**
     * Retrieves the array of {@link Item}s contained on each corner of this card.
     *
     * @return {@link PlayableCard#corners}.
     */
    public Item[] getCorners() {
        return corners;
    }

    /**
     * Sets the {@link Item} at the specified index in the corners array.
     *
     * @param item The {@link Item} to set.
     * @param i    The index at which to set the item.
     * @throws ArrayIndexOutOfBoundsException if the index is out of range (i < 0 || i >= backCorners.length).
     */
    public void setCorner(Item item, int i) {
        if (i < 0 || i >= corners.length) {
            throw new ArrayIndexOutOfBoundsException("Index is out of range: " + i);
        }
        this.corners[i] = item;
    }

    /**
     * Retrieves the constraint associated with this PlayableCard.
     */
    public Map<Item, Integer> getConstraint() {
        return null;
    }

    /**
     * Retrieves this PlayableCard's type.
     */
    public CardType getCardType() {
        return type;
    }
}
