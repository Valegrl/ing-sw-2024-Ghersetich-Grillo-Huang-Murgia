package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

/**
 * This class represents an immutable version of a {@link ResourceCard}.
 */
public final class ImmResourceCard extends ImmPlayableCard implements CardToString {

    /**
     * Constructs an immutable representation of the given {@link ResourceCard}.
     *
     * @param resourceCard The resource card to represent.
     */
    public ImmResourceCard(ResourceCard resourceCard) {
        super(resourceCard);
    }

    @Override
    public String printCard(int indent) {
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

    @Override
    public String printCardBack() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResourceCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Corners: \n");
        sb.append("    TL: empty").append("  TR: empty\n").append("    BL: empty").append("  BR: empty\n");
        return sb.toString();
    }
}
