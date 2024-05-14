package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents an immutable version of a StartCard.
 * It extends the ImmCard class and adds additional properties and methods related to the start card.
 */
public final class ImmStartCard extends ImmCard implements CardToString {
    /**
     * The backPermanentResources is a list of Items representing the resources that the card provides permanently
     * on its back side.
     */
    private final List<Item> backPermanentResources;

    /**
     * The frontCorners is an array of Items representing the resources located at the corners of the card's front side.
     */
    private final Item[] frontCorners;

    /**
     * The backCorners is an array of Items representing the resources located at the corners of the card's back side.
     */
    private final Item[] backCorners;

    /**
     * The flipped is a boolean indicating whether the card is flipped or not.
     */
    private final boolean flipped;

    /**
     * Constructs an immutable representation of a start card.
     * This constructor takes a StartCard object as an argument and extracts its properties to create an ImmStartCard
     * object.
     *
     * @param startCard the start card to represent
     */
    public ImmStartCard(StartCard startCard) {
        super(startCard);
        this.backPermanentResources = Collections.unmodifiableList(startCard.getBackPermanentResources());
        this.frontCorners = startCard.getFrontCorners().clone();
        this.backCorners = startCard.getBackCorners().clone();
        this.flipped = startCard.isFlipped();
    }

    /**
     * This method allows access to the list of back permanent resources of the card, which represents the resources
     * that the card provides permanently on its back side.
     *
     * @return the back permanent resources of the card
     */
    public List<Item> getBackPermanentResources() {
        return backPermanentResources;
    }

    /**
     * This method allows access to the array of front corner resources of the card, which represents the resources
     * located at the corners of the card's front side.
     *
     * @return a copy of the array of front corner resources of the card
     */
    public Item[] getFrontCorners() {
        return frontCorners.clone();
    }

    /**
     * This method allows access to the array of back corner resources of the card, which represents the resources
     * located at the corners of the card's back side.
     *
     * @return a copy of the array of back corner resources of the card
     */
    public Item[] getBackCorners() {
        return backCorners.clone();
    }

    /**
     * This method allows access to the flipped status of the card, which indicates whether the card is flipped or not.
     *
     * @return the flipped status of the card
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Prints the front of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("StartCard: ").append(this.getId()).append("\n");
        Item[] frontCorners = this.getFrontCorners();
        sb.append("  Front Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(frontCorners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(frontCorners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(frontCorners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(frontCorners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }

    /**
     * Prints the back of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCardBack() {
        StringBuilder sb = new StringBuilder();
        sb.append("StartCard: ").append(this.getId()).append("\n");
        sb.append("  Back Permanent Resources: \n");
        List<Item> sortedResources = new ArrayList<>(this.getBackPermanentResources());
        sortedResources.sort(Comparator.comparing(Enum::name));
        sb.append("    ");
        for (int i = 0; i < sortedResources.size() - 1; i++) {
            sb.append(Item.itemToColor(sortedResources.get(i))).append(", ");
        }
        sb.append(Item.itemToColor(sortedResources.get(sortedResources.size() - 1))).append("\n");
        Item[] backCorners = this.getBackCorners();
        sb.append("  Back Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(backCorners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(backCorners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(backCorners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(backCorners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }

    public String printSimpleCard(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ".repeat(indent));
        if (!flipped) {
            sb.append("Showing face: front\n");
        } else {
            sb.append("Showing face: back\n");
        }
        sb.append(printSimpleCardHelper(indent, flipped));
        return sb.toString();
    }

    public String printSetupStartCard() {
        int indent = 9;
        StringBuilder sb = new StringBuilder();
        sb.append("  Start card: ")
                .append(this.getId()).append("\n")
                .append("    1- Front:\n")
                .append(printSimpleCardHelper(indent, false));
        sb.append("    2- Back:\n")
                .append(printSimpleCardHelper(indent, true));
        return sb.toString();
    }

    private String printSimpleCardHelper(int indent, boolean flipped) {
        StringBuilder sb = new StringBuilder();
        Item[] corners;
        List<Item> permanentResources = null;
        if (flipped) {
            corners = this.getBackCorners();
            permanentResources = getBackPermanentResources();
        } else {
            corners = this.getFrontCorners();
        }
        if (permanentResources != null) {
            sb.append(" ".repeat(indent))
                    .append("Permanent Resources: "); // TODO add permanent resources
            for (int i = 0; i < permanentResources.size() - 1; i++) {
                sb.append(Item.itemToColor(permanentResources.get(i))).append(", ");
            }
            sb.append(Item.itemToColor(permanentResources.getLast())).append("\n");
        }
        sb.append(" ".repeat(indent))
                .append("Corners: \n")
                .append(" ".repeat(indent + 2))
                .append("TL: ").append(Item.itemToColor(corners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(corners[CornerIndex.TR.getIndex()])).append("\n")
                .append(" ".repeat(indent + 2))
                .append("BL: ").append(Item.itemToColor(corners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(corners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }
}
