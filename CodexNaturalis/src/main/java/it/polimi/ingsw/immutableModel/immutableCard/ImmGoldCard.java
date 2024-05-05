package it.polimi.ingsw.immutableModel.immutableCard;

import it.polimi.ingsw.model.card.*;

import java.util.Map;

/**
 * This class represents an immutable version of a GoldCard.
 * It extends the ImmPlayableCard class and adds additional properties and methods related to the gold card.
 * The class is final, so it can't be extended.
 */
public final class ImmGoldCard extends ImmPlayableCard implements ViewCard {
    /**
     * The constraint is a map where the keys are Items and the values are the quantity of each item required as
     * a constraint.
     */
    private final Map<Item, Integer> constraint;

    /**
     * The requiredItems is a map where the keys are Items and the values are the quantity of each item required.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of a gold card.
     * This constructor takes a GoldCard object as an argument and extracts its properties to create an
     * ImmGoldCard object.
     *
     * @param goldCard the gold card to represent
     */
    public ImmGoldCard(GoldCard goldCard) {
        super(goldCard);
        this.constraint = Map.copyOf(goldCard.getConstraint());
        this.requiredItems = Map.copyOf(goldCard.getRequiredItems());
    }

    @Override
    public Map<Item, Integer> getConstraint() {
        return Map.copyOf(constraint);
    }
    @Override
    public Map<Item, Integer> getRequiredItems() {
        return Map.copyOf(requiredItems);
    }

    /**
     * Prints the front of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("GoldCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Points: ").append(this.getPoints()).append("\n");
        sb.append("  Constraint: ").append("\n");
        for (Map.Entry<Item, Integer> entry : this.getConstraint().entrySet()) {
            sb.append("    Item: #").append(entry.getValue()).append(" ").append(entry.getKey().getType());
        }
        Item[] corners = this.getCorners();
        sb.append("  Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(corners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(corners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(corners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(corners[CornerIndex.BR.getIndex()])).append("\n");
        sb.append("  Required Items: ").append("\n");
        for (Map.Entry<Item, Integer> entry : this.getRequiredItems().entrySet()) {
            sb.append("    Item: #").append(entry.getValue()).append(" ").append(entry.getKey().getType()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Prints the back of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    public String printCardBack() {
        StringBuilder sb = new StringBuilder();
        sb.append("GoldCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Corners: \n");
        sb.append("    TL: empty").append("  TR: empty\n").append("    BL: empty").append("  BR: empty");
        return sb.toString();
    }
}
