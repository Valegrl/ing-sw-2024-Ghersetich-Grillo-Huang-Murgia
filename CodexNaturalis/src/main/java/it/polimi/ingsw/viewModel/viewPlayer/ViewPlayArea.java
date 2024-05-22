package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.utils.AnsiCodes;
import it.polimi.ingsw.viewModel.immutableCard.BackPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmStartCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.model.card.Item;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable representation of an opponent-player's {@link PlayArea}.
 */
public final class ViewPlayArea implements Serializable {
    /**
     * The list of immutable representations of cards in the opponent's hand.
     */
    private List<BackPlayableCard> hand;

    /**
     * The opponent's start card.
     */
    private final ImmStartCard startCard;

    /**
     * The map of played cards in the play area, associated with their coordinates.
     */
    private final Map<Coordinate, ImmPlayableCard> playedCards;

    /**
     * Constructs an immutable representation of the given {@link PlayArea}.
     *
     * @param playArea The play area to represent.
     */
    public ViewPlayArea(PlayArea playArea) {
        this.hand = playArea.getHand().stream()
                .map(BackPlayableCard::new)
                .collect(Collectors.toList());
        this.startCard = new ImmStartCard(playArea.getStartCard());
        this.playedCards = playArea.getPlayedCards().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> new ImmPlayableCard(entry.getValue())
                ));
    }

    /**
     * Sets the opponent's hand with the given list of cards.
     * @param hand The new list of {@link BackPlayableCard} objects representing the opponent's hand.
     */
    public void setHand(List<BackPlayableCard> hand) {
        this.hand = hand;
    }

    /**
     * Retrieves the list of immutable representations of cards in the opponent's hand.
     * @return {@link ViewPlayArea#hand}.
     */
    public List<BackPlayableCard> getHand() {
        return hand;
    }

    /**
     * Retrieves the opponent's start card.
     * @return {@link ViewPlayArea#startCard}.
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * Retrieves the map of played cards in the play area, associated with their coordinates.
     * @return {@link ViewPlayArea#playedCards}.
     */
    public Map<Coordinate, ImmPlayableCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * Returns a string representation of the opponent's hand.
     * @return A string representation of the opponent's hand.
     */
    public String printHand() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player's Hand: \n");
        for (BackPlayableCard card : this.hand) {
            Item item = card.getItem();
            String cardType = card.getCardType().toString();
            sb.append("  ").append(cardType).append(" - ").append(Item.itemToColor(item)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the opponent's play area.
     * @return A string representation of the opponent's play area.
     */
    public String printPlayedCards() {
        StringBuilder sb = new StringBuilder();
        StringBuilder cardString = new StringBuilder();
        int currIndent = 0;

        sb.append(AnsiCodes.BOLD + "PlayArea" + AnsiCodes.RESET + ":\n  (0,0) -> ").append(startCard.getId()).append("\n");
        sb.append(startCard.printPlacedCard(11)).append("\n");
        for (Map.Entry<Coordinate, ImmPlayableCard> entry : playedCards.entrySet()) {
            cardString.setLength(0);
            Coordinate coordinate = entry.getKey();
            ImmPlayableCard card = entry.getValue();
            cardString.append("  ").append(coordinate).append(" -> ");
            currIndent = cardString.length();
            cardString.append(Item.itemToColor(card.getPermanentResource(), card.getId())).append("\n")
                      .append(card.printPlacedCard(currIndent));
            sb.append(cardString);
        }
        return sb.toString();
    }
}
