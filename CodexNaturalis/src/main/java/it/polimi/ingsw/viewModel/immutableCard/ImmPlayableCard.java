package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Map;

/**
 * This class represents an immutable version of a {@link PlayableCard}.
 */
public class ImmPlayableCard extends ImmEvaluableCard implements CardToString {
    /**
     * The card's permanent resource.
     */
    private final Item permanentResource;

    /**
     * The card's corners' resources.
     */
    private final Item[] corners;

    /**
     * The card's type.
     */
    private final CardType type;

    /**
     * Constructs an immutable representation of the given {@link PlayableCard}.
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
     * Retrieves the card's permanent resource.
     * @return {@link ImmPlayableCard#permanentResource}.
     */
    public Item getPermanentResource() {
        return permanentResource;
    }

    /**
     * Retrieves the card's corners' resources.
     * @return {@link ImmPlayableCard#corners}.
     */
    public Item[] getCorners() {
        return corners.clone();
    }

    /**
     * Retrieves the card's type.
     * @return {@link ImmPlayableCard#type}.
     */
    public CardType getType() {
        return type;
    }

    /**
     * Retrieves the card's required items.
     * @return The required items to assign points.
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

    /**
     * Returns a string representing the card's details in a simplified manner during a Command Line Interface(TUI) game.
     * @param indent The number of spaces to indent the string.
     * @return A string representing the card's details.
     */
    public String printSimpleCard(int indent) {
        StringBuilder sb = new StringBuilder();
        //sb.append(" ".repeat(indent)); TODO add flipped attribute to model's PlayableCard
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
