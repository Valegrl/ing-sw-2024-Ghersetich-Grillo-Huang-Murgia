package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Map;

/**
 * This class represents an immutable version of a PlayableCard.
 * It extends the ImmEvaluableCard class and adds additional properties and methods related to the playability
 * of the card.
 */
public class ImmPlayableCard extends ImmEvaluableCard implements CardToString {
    /**
     * The permanentResource represents the resource that the card provides permanently.
     */
    private final Item permanentResource;

    /**
     * The corners is an array of Items representing the resources located at the corners of the card.
     */
    private final Item[] corners;

    /**
     * The type represents the type of the card.
     */
    private final CardType type;

    /**
     * Constructs an immutable representation of a playable card.
     * This constructor takes a PlayableCard object as an argument and extracts its properties to create an
     * ImmPlayableCard object.
     *
     * @param playableCard the playable card to represent
     */
    public ImmPlayableCard(PlayableCard playableCard) {
        super(playableCard);
        this.permanentResource = playableCard.getPermanentResource();
        this.corners = playableCard.getCorners().clone();
        this.type = playableCard.getCardType();
    }

    /**
     * This method allows access to the permanent resource of the card, which represents the resource that the
     * card provides permanently.
     *
     * @return the permanent resource of the card
     */
    public Item getPermanentResource() {
        return permanentResource;
    }

    /**
     * This method allows access to the array of corner resources of the card, which represents the resources
     * located at the corners of the card.
     *
     * @return a copy of the array of corner resources of the card
     */
    public Item[] getCorners() {
        return corners.clone();
    }

    /**
     * This method allows access to the type of the card.
     *
     * @return the type of the card
     */
    public CardType getType() {
        return type;
    }

    /**
     * This method is overridden from the superclass and returns null in this class.
     * It is meant to be overridden in subclasses where a constraint is applicable.
     *
     * @return null
     */
    public Map<Item, Integer> getConstraint() {
        return null;
    }

    public String printCard() {
        return null;
    }

    public String printCardBack() {
        return null;
    }

    public String printSimpleCard(int indent) {
        StringBuilder sb = new StringBuilder();
        //sb.append(" ".repeat(indent));
        // if(!flipped) sb.append("Showing face: front\n");
        // else sb.append("Showing face: back\n");
        sb.append(" ".repeat(indent))
          .append("Corners: \n")
          .append(" ".repeat(indent + 2))
          .append("TL: ").append(Item.itemToColor(corners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(corners[CornerIndex.TR.getIndex()])).append("\n")
          .append(" ".repeat(indent + 2))
          .append("BL: ").append(Item.itemToColor(corners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(corners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }
}
