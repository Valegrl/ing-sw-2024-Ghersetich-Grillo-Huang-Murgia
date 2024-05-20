package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents an immutable version of a {@link StartCard}.
 */
public final class ImmStartCard extends ImmCard implements CardToString {
    /**
     * The card's permanent resources on the back side.
     */
    private final List<Item> backPermanentResources;

    /**
     * The card's front corners' Items.
     */
    private final Item[] frontCorners;

    /**
     * The card's back corners' Items.
     */
    private final Item[] backCorners;

    /**
     * Boolean value that indicates if the card is flipped.
     */
    private final boolean flipped;

    /**
     * Constructs an immutable representation of the given {@link StartCard}.
     *
     * @param startCard The start card to represent.
     */
    public ImmStartCard(StartCard startCard) {
        super(startCard);
        this.backPermanentResources = Collections.unmodifiableList(startCard.getBackPermanentResources());
        this.frontCorners = startCard.getFrontCorners().clone();
        this.backCorners = startCard.getBackCorners().clone();
        this.flipped = startCard.isFlipped();
    }

    /**
     * Retrieves the card's permanent resources on the back side.
     * @return {@link ImmStartCard#backPermanentResources}.
     */
    public List<Item> getBackPermanentResources() {
        return backPermanentResources;
    }

    /**
     * Retrieves the card's front corners' Items.
     * @return {@link ImmStartCard#frontCorners}.
     */
    public Item[] getFrontCorners() {
        return frontCorners.clone();
    }

    /**
     * Retrieves the card's back corners' Items.
     * @return {@link ImmStartCard#backCorners}.
     */
    public Item[] getBackCorners() {
        return backCorners.clone();
    }

    /**
     * Retrieves the card's flipped status.
     * @return {@link ImmStartCard#flipped}.
     */
    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public String printCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("StartCard: ").append(this.getId()).append("\n");
        Item[] frontCorners = this.getFrontCorners();
        sb.append("  Front Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(frontCorners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(frontCorners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(frontCorners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(frontCorners[CornerIndex.BR.getIndex()])).append("\n");
        return sb.toString();
    }

    @Override
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

    /**
     * Returns a string representing the card's details in a simplified manner during a Command Line Interface(TUI) game.
     * @param indent The number of spaces to indent the string.
     * @return A string representing the card's details.
     */
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

    /**
     * Returns a string representing the card's details for the setup phase during a Command Line Interface(TUI) game.
     * @return A string representing the card's details.
     */
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

    /**
     * Helper method to print the card's details in a simplified manner.
     * @param indent The number of spaces to indent the string.
     * @param flipped The boolean value that indicates if the card is flipped.
     * @return A string representing the card's details.
     */
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
