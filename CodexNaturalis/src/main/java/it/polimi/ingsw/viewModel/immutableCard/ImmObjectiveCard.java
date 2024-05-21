package it.polimi.ingsw.viewModel.immutableCard;


import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class represents an immutable version of an {@link ObjectiveCard}.
 */
public final class ImmObjectiveCard extends ImmEvaluableCard implements CardToString {
    /**
     * The required pattern needed to score points.
     */
    private final Pair<Coordinate, Item>[] requiredPattern;

    /**
     * The required items needed to score points.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of the given {@link ObjectiveCard}.
     *
     * @param objectiveCard the objective card to represent
     */
    public ImmObjectiveCard(ObjectiveCard objectiveCard) {
        super(objectiveCard);
        Pair<Coordinate, Item>[] requiredPattern = objectiveCard.getRequiredPattern();
        this.requiredPattern = (requiredPattern == null ? null: requiredPattern.clone());
        Map<Item, Integer> reqItems = objectiveCard.getRequiredItems();
        this.requiredItems = (reqItems == null ? null: Map.copyOf(reqItems));
    }

    @Override
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return requiredPattern.clone();
    }

    @Override
    public Map<Item, Integer> getRequiredItems() {
        return Map.copyOf(requiredItems);
    }

    /**
     * Returns a string representing the front of a card during a Command Line Interface(TUI) game.
     *
     * @return A string representing the card details.
     */
    @Override
    public String printCard(int indent){
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectiveCard: ").append(this.getId()).append("\n")
            .append(" ".repeat(indent))
            .append("  Points: ").append(this.getPoints()).append("\n")
            .append(" ".repeat(indent));
        if(this.getId().equals("OC01") || this.getId().equals("OC03")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "        |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
        }
        if(this.getId().equals("OC02") || this.getId().equals("OC04")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "        |---|")).append("\n");
        }
        if(this.getId().equals("OC05")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "     |---|")).append("\n");
        }
        if(this.getId().equals("OC06")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
        }
        if(this.getId().equals("OC07")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
        }
        if(this.getId().equals("OC08")){
            sb.append("  Pattern: \n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(" ".repeat(indent));
            sb.append(Item.itemToColor(requiredPattern[2].value(), "     |---|")).append("\n");
        }
        if(this.getId().equals("OC09") || this.getId().equals("OC10") || this.getId().equals("OC11") || this.getId().equals("OC12") ){
            sb.append("  Required Items: ").append("\n");
            for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                sb.append(" ".repeat(indent));
                sb.append("    - #").append(entry.getValue()).append(" ").append(Item.itemToColor(entry.getKey())).append(" items").append("\n");
            }
        }
        if(this.getId().equals("OC13") || this.getId().equals("OC14") || this.getId().equals("OC15") || this.getId().equals("OC16") ){
            sb.append("  Required Items: ").append("\n");
            List<Map.Entry<Item, Integer>> sortedItems = new ArrayList<>(requiredItems.entrySet());
            sortedItems.sort(Map.Entry.comparingByKey(Comparator.comparing(Enum::name)));
            for (Map.Entry<Item, Integer> entry : sortedItems) {
                sb.append(" ".repeat(indent));
                sb.append("    - #").append(entry.getValue()).append(" ").append(Item.itemToColor(entry.getKey())).append(" items").append("\n");
            }
        }
        return sb.toString();
    }
}
