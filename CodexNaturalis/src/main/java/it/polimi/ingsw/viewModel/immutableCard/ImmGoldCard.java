package it.polimi.ingsw.viewModel.immutableCard;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.evaluator.CornerEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents an immutable version of a {@link GoldCard}.
 */
public final class ImmGoldCard extends ImmPlayableCard implements CardToString {
    /**
     * The constraint needed to place this card.
     */
    private final Map<Item, Integer> constraint;

    /**
     * The requiredItems needed to score points.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of the given {@link GoldCard}.
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

    @Override
    public String printCard(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("GoldCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Points: ").append(this.getPoints()).append("\n");
        if(!this.getRequiredItems().isEmpty()) {
            sb.append("  Required Items: ");
            for (Map.Entry<Item, Integer> entry : this.getRequiredItems().entrySet()) {
                sb.append("\n    - #").append(entry.getValue()).append(" ").append(Item.itemToColor(entry.getKey())).append(" items");
            }
            sb.append("\n");
        }
        else if(this.getEvaluator() instanceof CornerEvaluator){
            sb.append("  ").append(this.getPoints()).append(" points for each overlapping corner of this card \n");
        }
        else{
            sb.append("");
        }
        Item[] corners = this.getCorners();
        sb.append("  Corners: \n");
        sb.append("    TL: ").append(Item.itemToColor(corners[CornerIndex.TL.getIndex()])).append("  TR: ").append(Item.itemToColor(corners[CornerIndex.TR.getIndex()])).append("\n");
        sb.append("    BL: ").append(Item.itemToColor(corners[CornerIndex.BL.getIndex()])).append("  BR: ").append(Item.itemToColor(corners[CornerIndex.BR.getIndex()])).append("\n");
        sb.append("  Constraint: ");
        List<Map.Entry<Item, Integer>> sortedConstraints = new ArrayList<>(this.getConstraint().entrySet());
        sortedConstraints.sort(Map.Entry.<Item, Integer>comparingByValue().reversed());
        for (Map.Entry<Item, Integer> entry : sortedConstraints) {
            sb.append("\n    - #").append(entry.getValue()).append(" ").append(Item.itemToColor(entry.getKey())).append(" resources");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String printCardBack() {
        StringBuilder sb = new StringBuilder();
        sb.append("GoldCard: ").append(Item.itemToColor(this.getPermanentResource(), this.getId())).append("\n");
        sb.append("  Corners: \n");
        sb.append("    TL: empty").append("  TR: empty\n").append("    BL: empty").append("  BR: empty\n");
        return sb.toString();
    }
}
