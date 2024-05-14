package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.viewModel.ViewStartSetup;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.StartCard;

import java.util.List;

/**
 * This class represents the setup of cards for a player in the game.
 * It contains the player's username, an array of objective cards, a start card, and a view of the start setup.
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
     * The view of the start setup.
     */
    private final ViewStartSetup view;

    /**
     * Constructs a new PlayerCardsSetup with the specified username, objective cards, start card, and view of the start setup.
     * The chosen status is set to false by default.
     *
     * @param username the username of the player
     * @param objectiveCards an array of objective cards
     * @param start the start card
     * @param hand an array of playable cards representing the player's hand
     * @param goldVisible an array of playable cards representing the player's visible gold cards
     * @param goldDeck an item representing the player's gold deck
     * @param resourceVisible an array of playable cards representing the player's visible resource cards
     * @param resourceDeck an item representing the player's resource deck
     * @param commonObjectives an array of objective cards representing the common objectives
     */
    public PlayerCardsSetup(String username, ObjectiveCard[] objectiveCards, StartCard start, List<PlayableCard> hand,
                            PlayableCard[] goldVisible, Item goldDeck, PlayableCard[] resourceVisible, Item resourceDeck,
                            ObjectiveCard[] commonObjectives) {
        this.username = username;
        this.chosen = false;
        this.objectiveCards = objectiveCards;
        this.startCard = start;
        this.view = new ViewStartSetup(objectiveCards, start, hand, goldVisible, goldDeck,
                                       resourceVisible, resourceDeck, commonObjectives);
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
     * @return an array of the player's objective cards
     */
    public ObjectiveCard[] getObjectiveCards() {
        return objectiveCards.clone();
    }

    /**
     * @return the player's start card
     */
    public StartCard getStartCard() {
        return startCard;
    }

    /**
     * @return the view of the start setup
     */
    public ViewStartSetup getView() {
        return view;
    }
}
