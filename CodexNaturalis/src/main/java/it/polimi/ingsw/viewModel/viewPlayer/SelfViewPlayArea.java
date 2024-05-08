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

    public String printHand() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player's Hand: \n");
        for (ImmPlayableCard card : this.hand) {
            // TODO
        }
        return sb.toString();
    }

    public String printPlayedCards() {
        StringBuilder sb = new StringBuilder();
        StringBuilder cardString = new StringBuilder();
        int currIndent = 0;

        sb.append("\u001B[1mPlayArea\u001B[0m:\n  (0,0) -> ").append(startCard.getId()).append("\n");
        sb.append(startCard.printSimpleCard(11)).append("\n");
        for (Map.Entry<Coordinate, ImmPlayableCard> entry : playedCards.entrySet()) {
            cardString.setLength(0);
            Coordinate coordinate = entry.getKey();
            ImmPlayableCard card = entry.getValue();
            cardString.append("  ").append(coordinate).append(" -> ");
            currIndent = cardString.length();
            cardString.append(Item.itemToColor(card.getPermanentResource(), card.getId())).append("\n")
                    .append(card.printSimpleCard(currIndent));
            sb.append(cardString);
        }
        return sb.toString();
    }
}
