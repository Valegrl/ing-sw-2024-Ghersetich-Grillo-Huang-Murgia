package it.polimi.ingsw.immutableModel.immutableCard;


import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.util.Map;

/**
 * This class represents an immutable version of an ObjectiveCard.
 * It extends the ImmEvaluableCard class and adds additional properties and methods related to the objective card.
 * The class is final, so it can't be extended.
 */
public final class ImmObjectiveCard extends ImmEvaluableCard {
    /**
     * The requiredPattern is an array of pairs, each pair consists of a Coordinate and an Item.
     * It represents the pattern that needs to be matched on the board for the card to be evaluated.
     */
    private final Pair<Coordinate, Item>[] requiredPattern;

    /**
     * The requiredItems is a map where the keys are Items and the values are the quantity of each item required.
     */
    private final Map<Item, Integer> requiredItems;

    /**
     * Constructs an immutable representation of an objective card.
     * This constructor takes an ObjectiveCard object as an argument and extracts its properties to create an
     * ImmObjectiveCard object.
     *
     * @param objectiveCard the objective card to represent
     */
    public ImmObjectiveCard(ObjectiveCard objectiveCard) {
        super(objectiveCard);
        this.requiredPattern = objectiveCard.getRequiredPattern().clone();
        this.requiredItems = Map.copyOf(objectiveCard.getRequiredItems());
    }

    @Override
    public Pair<Coordinate, Item>[] getRequiredPattern() {
        return requiredPattern.clone();
    }

    @Override
    public Map<Item, Integer> getRequiredItems() {
        return Map.copyOf(requiredItems);
    }

    public String printCard(){
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectiveCard: ").append(this.getId()).append("\n");
        sb.append("  Points: ").append(this.getPoints()).append("\n");
        if(this.getId().equals("OC01") || this.getId().equals("OC03")){
            sb.append(Item.itemToColor(requiredPattern[2].value(), "        |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
        }
        if(this.getId().equals("OC02") || this.getId().equals("OC04")){
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[2].value(), "        |---|")).append("\n");
        }
        if(this.getId().equals("OC05")){
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "  |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[2].value(), "     |---|")).append("\n");
        }
        if(this.getId().equals("OC06")){
            sb.append(Item.itemToColor(requiredPattern[0].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[2].value(), "  |---|")).append("\n");
        }
        if(this.getId().equals("OC07")){
            sb.append(Item.itemToColor(requiredPattern[0].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "  |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[2].value(), "  |---|")).append("\n");
        }
        if(this.getId().equals("OC08")){
            sb.append(Item.itemToColor(requiredPattern[0].value(), "  |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[1].value(), "     |---|")).append("\n");
            sb.append(Item.itemToColor(requiredPattern[2].value(), "     |---|")).append("\n");
        }
        if(this.getId().equals("OC09") || this.getId().equals("OC10") || this.getId().equals("OC11") || this.getId().equals("OC12") ){
            sb.append("  Required: ").append("\n");
            for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                sb.append("    Item: #").append(entry.getValue()).append(" ").append(entry.getKey().getType()).append("\n");
            }
        }
        if(this.getId().equals("OC13") || this.getId().equals("OC14") || this.getId().equals("OC15") || this.getId().equals("OC16") ){
            sb.append("  Required: ").append("\n");
            for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                sb.append("    Item: #").append(entry.getValue()).append(" ").append(entry.getKey().getType()).append("\n");
            }
        }
        return sb.toString();
    }
}
