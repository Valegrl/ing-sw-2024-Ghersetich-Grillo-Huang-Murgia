package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ObjectiveCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.card.StartCard;
import it.polimi.ingsw.viewModel.immutableCard.BackPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents the setup of cards for a player in the game.
 * It contains an array of immutable objective cards, an immutable start card, and other card-related attributes.
 * All cards are represented in their immutable form.
 */
public class ViewStartSetup implements Serializable {
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
     * A map representing the back-hand cards of each opponent.
     * The map's keys are the usernames of the opponents, and the values are lists of {@link BackPlayableCard} representing
     * the opponents' back-hand cards.
     */
    private final Map<String, List<BackPlayableCard>> opponentsBackHandCards;

    /**
     * Constructs a new ViewStartSetup with the specified objective cards, start card, hand, goldVisible, goldDeck,
     * resourceVisible, resourceDeck, commonObjectives, and opponentsBackHandCards.
     *
     * @param objectiveCards an array of ObjectiveCard representing the player's objective cards
     * @param start a StartCard representing the player's start card
     * @param hand a list of PlayableCard representing the player's hand
     * @param goldVisible an array of PlayableCard representing the player's visible gold cards
     * @param goldDeck an Item representing the player's gold deck
     * @param resourceVisible an array of PlayableCard representing the player's visible resource cards
     * @param resourceDeck an Item representing the player's resource deck
     * @param commonObjectives an array of ObjectiveCard representing the common objectives
     * @param back a map where the keys are the usernames of the opponents, and the values are lists of PlayableCard
     *             representing the opponents' back-hand cards. This map will be converted to a map with lists of
     *             BackPlayableCard.
     */
    public ViewStartSetup(ObjectiveCard[] objectiveCards, StartCard start, List<PlayableCard> hand,
                          PlayableCard[] goldVisible, Item goldDeck, PlayableCard[] resourceVisible, Item resourceDeck,
                          ObjectiveCard[] commonObjectives, Map<String, List<PlayableCard>> back) {
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
        this.opponentsBackHandCards = new HashMap<>();
        for (Map.Entry<String, List<PlayableCard>> entry : back.entrySet()) {
            List<BackPlayableCard> backPlayableCards = entry.getValue().stream()
                    .map(BackPlayableCard::new)
                    .collect(Collectors.toList());
            this.opponentsBackHandCards.put(entry.getKey(), backPlayableCards);
        }
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

    /**
     * @return A map where the keys are the usernames of the opponents, and the values are lists of BackPlayableCard
     * representing the opponents' back-hand cards.
     */
    public Map<String, List<BackPlayableCard>> getOpponentsBackHandCards() {
        return opponentsBackHandCards;
    }
}
