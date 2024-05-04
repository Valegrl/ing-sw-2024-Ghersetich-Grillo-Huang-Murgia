package it.polimi.ingsw.utils;

import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.StartCard;

import java.util.Arrays;

/**
 * This class represents the setup of cards for a player in the game.
 * It contains the player's username, an array of objective cards, and a start card.
 */
public class PlayerCardsSetup {
    /**
     * The username of the player.
     */
    private final String username;

    /**
     * An array of objective cards assigned to the player.
     */
    private final ObjectiveCard[] objectiveCards;

    /**
     * The start card assigned to the player.
     */
    private final StartCard startCard;

    /**
     * An array of immutable objective cards assigned to the player.
     */
    private final ImmObjectiveCard[] immObjectiveCards;

    /**
     * The immutable start card assigned to the player.
     */
    private final ImmStartCard immStartCard;

    /**
     * Constructs a new PlayerCardsSetup with the specified username, objective cards, and start card.
     *
     * @param username the username of the player
     * @param objectiveCards an array of objective cards
     * @param start the start card
     */
    public PlayerCardsSetup(String username, ObjectiveCard[] objectiveCards, StartCard start) {
        this.username = username;
        this.objectiveCards = objectiveCards;
        this.startCard = start;
        this.immObjectiveCards = Arrays.stream(objectiveCards)
                .map(ImmObjectiveCard::new)
                .toArray(ImmObjectiveCard[]::new);
        this.immStartCard = new ImmStartCard(start);
    }

    /**
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return an array of the player's objective cards
     */
    public ObjectiveCard[] getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * @return the player's start card
     */
    public StartCard getStartCard() {
        return startCard;
    }

    /**
     * @return an array of the player's immutable objective cards
     */
    public ImmObjectiveCard[] getImmObjectiveCards() {
        return immObjectiveCards;
    }

    /**
     * @return the player's immutable start card
     */
    public ImmStartCard getImmStartCard() {
        return immStartCard;
    }
}
