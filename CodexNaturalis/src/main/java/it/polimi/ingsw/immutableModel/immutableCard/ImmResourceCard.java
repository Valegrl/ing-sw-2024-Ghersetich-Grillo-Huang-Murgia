package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents an immutable version of a ResourceCard.
 * It extends the ImmPlayableCard class and adds additional properties and methods related to the resource card.
 * The class is final, so it can't be extended.
 */
public final class ImmResourceCard extends ImmPlayableCard implements ViewCard {

    /**
     * Constructs an immutable representation of a resource card.
     * This constructor takes a ResourceCard object as an argument and extracts its properties to create an
     * ImmResourceCard object.
     *
     * @param resourceCard the resource card to represent
     */
    public ImmResourceCard(ResourceCard resourceCard) {
        super(resourceCard);
    }

    /**
     * Prints the front of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResourceCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        if (this.getPoints() != 0) {
            sb.append("  Points: ").append(this.getPoints()).append("\n");
        }
        Item[] corners = this.getCorners();
        sb.append("  Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(corners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(corners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(corners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(corners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }

    /**
     * Prints the back of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCardBack() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResourceCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Corners: \n");
        sb.append("    TL: empty").append("  TR: empty\n").append("    BL: empty").append("  BR: empty\n");
        return sb.toString();
    }
}
