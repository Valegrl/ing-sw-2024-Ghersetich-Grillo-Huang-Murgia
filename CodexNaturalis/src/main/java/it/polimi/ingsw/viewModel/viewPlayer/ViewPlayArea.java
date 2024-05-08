package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.card.Item;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable representation of the PlayArea class.
 * This class provides a read-only view of the player's play area, which can be safely shared across different threads.
 * All fields are final and initialized at construction time, and there are no setter methods.
 * The class also provides getter methods for all fields.
 *
 * @see it.polimi.ingsw.model.player.PlayArea
 */
public final class ViewPlayArea implements Serializable {
    /**
     * The list of playable cards in the player's hand.
     */
    private final List<Item> hand;

    /**
     * The start card of the player.
     */
    private final ImmStartCard startCard;

    /**
     * The map of played cards in the play area.
     * The keys are the coordinates where the cards are placed.
     */
    private final Map<Coordinate, ImmPlayableCard> playedCards;

    public ViewPlayArea(PlayArea playArea) {
        this.hand = playArea.getHand().stream()
                .map(PlayableCard::getPermanentResource)
                .toList();
        this.startCard = new ImmStartCard(playArea.getStartCard());
        this.playedCards = playArea.getPlayedCards().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> new ImmPlayableCard(entry.getValue())
                ));
    }

    public List<Item> getHand() {
        return hand;
    }

    public ImmStartCard getStartCard() {
        return startCard;
    }

    public Map<Coordinate, ImmPlayableCard> getPlayedCards() {
        return playedCards;
    }

    public String printHand() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player's Hand: \n");
        for (Item item : this.hand) {
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
