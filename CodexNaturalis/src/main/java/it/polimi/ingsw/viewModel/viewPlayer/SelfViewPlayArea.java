package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.viewModel.immutableCard.ImmEvaluableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SelfViewPlayArea implements Serializable {
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
    public SelfViewPlayArea(PlayArea playArea) {
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
        this.selectedCard = new Pair<>(playArea.getSelectedCard().key(), new ImmEvaluableCard(playArea.getSelectedCard().value()));
    }

    public List<ImmPlayableCard> getHand() {
        return hand;
    }

    public ImmStartCard getStartCard() {
        return startCard;
    }

    public Map<Coordinate, ImmPlayableCard> getPlayedCards() {
        return playedCards;
    }

    public Map<Item, Integer> getUncoveredItems() {
        return uncoveredItems;
    }

    public Pair<Coordinate, ImmEvaluableCard> getSelectedCard() {
        return selectedCard;
    }
}
