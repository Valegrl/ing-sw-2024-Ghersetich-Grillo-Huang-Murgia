package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.viewModel.immutableCard.*;

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
public class ViewStartSetup implements Serializable, CardConverter {
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
                .map(this::convertToImmCardType)
                .toList();
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
                .map(this::convertToImmCardType)
                .toArray(ImmPlayableCard[]::new);
    }

    public String printSetupObjCards() {
        int indent = 7;
        return "  Objective cards: \n" +
                "    1- " +
                secretObjectiveCards[0].printCard(indent) +
                "    2- " +
                secretObjectiveCards[1].printCard(indent);
    }

    /**
     * Returns a string representation of the player's setup hand.
     * @return A string representation of the player's setup hand.
     */
    public String printSetupHand() {
        int indent = 5;
        StringBuilder sb = new StringBuilder();
        sb.append("Hand: \n");
        for (int i = 0; i < hand.size(); i++) {
            sb.append("  ")
                    .append(i + 1)
                    .append("- ")
                    .append(hand.get(i).printCard(indent));
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the player's setup common objectives.
     * @return A string representation of the player's setup common objectives.
     */
    public String printSetupCommonObjectives() {
        int indent = 5;
        StringBuilder sb = new StringBuilder();
        sb.append("Common objectives: \n");
        for (int i = 0; i < commonObjectives.length; i++) {
            sb.append("  ")
                    .append(i + 1)
                    .append("- ")
                    .append(commonObjectives[i].printCard(indent));
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the decks in the setup phase.
     * @return A string representation of the decks.
     */
    public String printSetupDecks() {
        int indent = 12;
        return "Decks: \n" +
                "  1- Gold deck:\n" +
                "       Top card: " +
                Item.itemToColor(goldDeck) +
                "\n" +
                "       Visible cards:\n" +
                "         1- " +
                visibleGoldCards[0].printCard(indent) +
                "         2- " +
                visibleGoldCards[1].printCard(indent) +
                "  2- Resource deck:\n" +
                "       Top card: " +
                Item.itemToColor(resourceDeck) +
                "\n" +
                "       Visible cards: \n" +
                "         1- " +
                visibleResourceCards[0].printCard(indent) +
                "         2- " +
                visibleResourceCards[1].printCard(indent);
    }

    /**
     * Returns a string representation of the opponents' back-hand cards.
     * @return A string representation of the opponents' back-hand cards.
     */
    public String printSetupOpponentsHands() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<BackPlayableCard>> entry : opponentsBackHandCards.entrySet()) {
            sb.append("- ")
                    .append(entry.getKey())
                    .append(": [ ");
            for (BackPlayableCard card : entry.getValue()) {
                sb.append(card)
                        .append(" ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    /**
     * Retrieves the secret objective cards.
     * @return {@link ViewStartSetup#secretObjectiveCards}.
     */
    public ImmObjectiveCard[] getSecretObjectiveCards() {
        return secretObjectiveCards;
    }

    /**
     * Retrieves the start card.
     * @return {@link ViewStartSetup#startCard}.
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * Retrieves the player's hand.
     * @return {@link ViewStartSetup#hand}.
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }

    /**
     * Retrieves the visible gold cards.
     * @return {@link ViewStartSetup#visibleGoldCards}.
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    /**
     * Retrieves the gold deck top item.
     * @return {@link ViewStartSetup#goldDeck}.
     */
    public Item getGoldDeck() {
        return goldDeck;
    }

    /**
     * Retrieves the visible resource cards.
     * @return {@link ViewStartSetup#visibleResourceCards}.
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * Retrieves the resource deck top item.
     * @return {@link ViewStartSetup#resourceDeck}.
     */
    public Item getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Retrieves the common objectives.
     * @return {@link ViewStartSetup#commonObjectives}.
     */
    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * Retrieves the opponents' back-cards from their hand.
     * @return {@link ViewStartSetup#opponentsBackHandCards}.
     */
    public Map<String, List<BackPlayableCard>> getOpponentsBackHandCards() {
        return opponentsBackHandCards;
    }
}
