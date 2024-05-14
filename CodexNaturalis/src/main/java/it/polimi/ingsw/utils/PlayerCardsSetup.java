package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.StartCard;

/**
 * This class represents the setup of cards for a player in the game.
 * It contains the player's username and an array of secret objective.
 */
public class PlayerCardsSetup {
    /**
     * The username of the player.
     */
    private final String username;

    /**
     * This field represents whether the player's card setup has been chosen.
     * If true, the card setup has been chosen. If false, it has not been chosen yet.
     */
    private boolean chosen;

    /**
     * An array of objective cards assigned to the player.
     */
    private final ObjectiveCard[] objectiveCards;

    /**
     * The start card assigned to the player.
     */
    private final StartCard startCard;

    /**
     * Constructs a new PlayerCardsSetup with the specified username, secret objective cards, and start card.
     * The chosen status is set to false by default.
     *
     * @param username the username of the player
     * @param objectiveCards an array of objective cards
     * @param start the start card of the player
     */
    public PlayerCardsSetup(String username, ObjectiveCard[] objectiveCards, StartCard start) {
        this.username = username;
        this.chosen = false;
        this.objectiveCards = objectiveCards;
        this.startCard = start;
    }

    /**
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method checks if the player's card setup has been chosen.
     *
     * @return true if the card setup has been chosen, false otherwise.
     */
    public boolean isChosen() {
        return chosen;
    }

    /**
     * This method sets the chosen status of the player's card setup.
     *
     * @param chosen the new chosen status to set. True indicates that the card setup has been chosen,
     *               false indicates it has not.
     */
    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    /**
     * @return The StartCard object representing the player's start card.
     */
    public StartCard getStartCard() {
        return startCard;
    }

    /**
     * @return an array of the player's objective cards
     */
    public ObjectiveCard[] getObjectiveCards() {
        return objectiveCards.clone();
    }
}