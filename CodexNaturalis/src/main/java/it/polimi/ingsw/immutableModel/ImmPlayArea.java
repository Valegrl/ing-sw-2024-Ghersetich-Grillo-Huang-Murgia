package it.polimi.ingsw.immutableModel;

import it.polimi.ingsw.immutableModel.immutableCard.ImmEvaluableCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.model.card.Item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable representation of the PlayArea class.
 * This class provides a read-only view of the player's play area, which can be safely shared across different threads.
 * All fields are final and initialized at construction time, and there are no setter methods.
 * The class also provides getter methods for all fields.
 *
 * @see it.polimi.ingsw.model.player.PlayArea
 */
public final class ImmPlayArea {
    /**
     * The list of playable cards in the player's hand.
     */
    private final List<ImmPlayableCard> hand;

    /**
     * The start card of the player.
     */
    private final ImmStartCard startCard;

    /**
     * The map of played cards in the play area.
     * The keys are the coordinates where the cards are placed.
     */
    private final Map<Coordinate, ImmPlayableCard> playedCards;

    /**
     * The map of uncovered items in the play area.
     * The keys are the items and the values are their quantities.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The selected card in the play area.
     * The key is the coordinate where the card is placed.
     */
    private final Pair<Coordinate, ImmEvaluableCard> selectedCard;


    /**
     * Constructs an immutable representation of a play area.
     *
     * @param playArea the play area to represent
     */
    public ImmPlayArea(PlayArea playArea) {
        this.hand = playArea.getHand().stream()
                .map(ImmPlayableCard::new)
                .toList();
        this.startCard = new ImmStartCard(playArea.getStartCard());
        this.playedCards = playArea.getPlayedCards().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> new ImmPlayableCard(entry.getValue())
                ));
        this.uncoveredItems = Collections.unmodifiableMap(playArea.getUncoveredItems());
        this.selectedCard = new Pair<>(playArea.getSelectedCard().getKey(), new ImmEvaluableCard(playArea.getSelectedCard().getValue()));
    }

    /**
     * Each card is represented as an instance of the ImmPlayableCard class.
     *
     * @return the list of playable cards in the player's hand
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }

    /**
     * The start card is represented as an instance of the ImmStartCard class.
     *
     * @return the start card of the player
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * The keys are the coordinates where the cards are placed, and the values are the cards at those coordinates.
     * Each card is represented as an instance of the ImmPlayableCard class.
     *
     * @return the map of played cards in the play area
     */
    public Map<Coordinate, ImmPlayableCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * The keys are the items and the values are their quantities.
     * Each item is represented as an instance of the Item class.
     *
     * @return the map of uncovered items in the play area
     */
    public Map<Item, Integer> getUncoveredItems() {
        return uncoveredItems;
    }

    /**
     * The key is the coordinate where the card is placed, and the value is the card at that coordinate.
     * The card is represented as an instance of the ImmEvaluableCard class.
     *
     * @return the selected card in the play area
     */
    public Pair<Coordinate, ImmEvaluableCard> getSelectedCard() {
        return selectedCard;
    }
}
