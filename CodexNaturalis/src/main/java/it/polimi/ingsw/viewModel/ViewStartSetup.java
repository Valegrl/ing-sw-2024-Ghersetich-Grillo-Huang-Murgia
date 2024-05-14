package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StartCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the setup of cards for a player in the game.
 * It contains an array of immutable objective cards, an immutable start card, and other card-related attributes.
 * All cards are represented in their immutable form.
 */
public class ViewStartSetup {
    /**
     * An array of secret objective cards assigned to the player.
     */
    private final ImmObjectiveCard[] secretObjectiveCards;

    /**
     * The immutable start card assigned to the player.
     */
    private final ImmStartCard startCard;

    /**
     * A list of immutable playable cards representing the player's hand.
     */
    private final List<ImmPlayableCard> hand;

    /**
     * An array of immutable playable cards representing the player's visible gold cards.
     */
    private final ImmPlayableCard[] visibleGoldCards;

    /**
     * An item representing the player's gold deck.
     */
    private final Item goldDeck;

    /**
     * An array of immutable playable cards representing the player's visible resource cards.
     */
    private final ImmPlayableCard[] visibleResourceCards;

    /**
     * An item representing the player's resource deck.
     */
    private final Item resourceDeck;

    /**
     * An array of immutable objective cards representing the common objectives.
     */
    private final ImmObjectiveCard[] commonObjectives;

    /**
     * Constructs a new ViewStartSetup with the specified objective cards, start card, hand, goldVisible, goldDeck,
     * resourceVisible, resourceDeck, and commonObjectives.
     *
     * @param objectiveCards an array of objective cards
     * @param start the start card
     * @param hand an array of playable cards representing the player's hand
     * @param goldVisible an array of playable cards representing the player's visible gold cards
     * @param goldDeck an item representing the player's gold deck
     * @param resourceVisible an array of playable cards representing the player's visible resource cards
     * @param resourceDeck an item representing the player's resource deck
     * @param commonObjectives an array of objective cards representing the common objectives
     */
    public ViewStartSetup(ObjectiveCard[] objectiveCards, StartCard start, List<PlayableCard> hand,
                          PlayableCard[] goldVisible, Item goldDeck, PlayableCard[] resourceVisible, Item resourceDeck,
                          ObjectiveCard[] commonObjectives) {
        this.secretObjectiveCards = convertToImmObjectiveCards(objectiveCards);
        this.startCard = new ImmStartCard(start);
        this.hand = hand.stream()
                    .map(ImmPlayableCard::new)
                    .collect(Collectors.toList());
        this.visibleGoldCards = convertToImmPlayableCards(goldVisible);
        this.goldDeck = goldDeck;
        this.visibleResourceCards = convertToImmPlayableCards(resourceVisible);
        this.resourceDeck = resourceDeck;
        this.commonObjectives = convertToImmObjectiveCards(commonObjectives);
    }


    /**
     * Converts an array of ObjectiveCard to an array of ImmObjectiveCard.
     *
     * @param cards an array of ObjectiveCard
     * @return an array of ImmObjectiveCard
     */
    private ImmObjectiveCard[] convertToImmObjectiveCards(ObjectiveCard[] cards) {
        return Arrays.stream(cards)
                .map(ImmObjectiveCard::new)
                .toArray(ImmObjectiveCard[]::new);
    }

    /**
     * Converts an array of PlayableCard to an array of ImmPlayableCard.
     *
     * @param cards an array of PlayableCard
     * @return an array of ImmPlayableCard
     */
    private ImmPlayableCard[] convertToImmPlayableCards(PlayableCard[] cards) {
        return Arrays.stream(cards)
                .map(ImmPlayableCard::new)
                .toArray(ImmPlayableCard[]::new);
    }

    /**
     * @return an array of the player's secret objective cards
     */
    public ImmObjectiveCard[] getSecretObjectiveCards() {
        return secretObjectiveCards;
    }

    /**
     * @return the player's immutable start card
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * @return a list of the player's immutable playable cards representing the hand
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }

    /**
     * @return an array of the player's immutable playable cards representing the visible gold cards
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    /**
     * @return the player's gold deck item
     */
    public Item getGoldDeck() {
        return goldDeck;
    }

    /**
     * @return an array of the player's immutable playable cards representing the visible resource cards
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * @return the player's resource deck item
     */
    public Item getResourceDeck() {
        return resourceDeck;
    }

    /**
     * @return an array of the player's immutable objective cards representing the common objectives
     */
    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }
}
