package it.polimi.ingsw.viewModel.viewPlayer;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.utils.AnsiCodes;
import it.polimi.ingsw.viewModel.CardConverter;
import it.polimi.ingsw.viewModel.immutableCard.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable representation of the self-player's play area.
 */
public final class SelfViewPlayArea implements Serializable, CardConverter {
    /**
     * The player's hand.
     */
    private List<ImmPlayableCard> hand;

    /**
     * The player's start card.
     */
    private final ImmStartCard startCard;

    /**
     * The map of played cards in the play area, associated with their coordinates.
     */
    private final Map<Coordinate, ImmPlayableCard> playedCards;

    /**
     * The map of uncovered items in the play area, each item is associated with its uncovered quantity.
     */
    private final Map<Item, Integer> uncoveredItems;

    /**
     * The selected card in the play area and its coordinate.
     */
    private final Pair<Coordinate, ImmEvaluableCard> selectedCard;

    /**
     * Constructs an immutable representation of the given {@link PlayArea}.
     *
     * @param playArea The play area to represent.
     */
    public SelfViewPlayArea(PlayArea playArea) {
        this.hand = playArea.getHand().stream()
                .map(this::convertToImmCardType)
                .toList();
        this.startCard = new ImmStartCard(playArea.getStartCard());
        this.playedCards = playArea.getPlayedCards().entrySet().stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> this.convertToImmCardType(entry.getValue()),
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ),
                        Collections::unmodifiableMap
                ));
        this.uncoveredItems = new LinkedHashMap<>(playArea.getUncoveredItems());
        if (playArea.getSelectedCard().key() == null)
            this.selectedCard = new Pair<>(null, null);
        else
            this.selectedCard = new Pair<>(playArea.getSelectedCard().key(), new ImmEvaluableCard(playArea.getSelectedCard().value()));
    }

    /**
     * Sets the player's hand with the given list of cards.
     * @param hand The new list of {@link ImmPlayableCard} objects representing the player's hand.
     */
    public void setHand(List<ImmPlayableCard> hand) {
        this.hand = hand;
    }

    /**
     * Retrieves the player's hand.
     * @return {@link SelfViewPlayArea#hand}.
     */
    public List<ImmPlayableCard> getHand() {
        return hand;
    }

    /**
     * Retrieves the player's start card.
     * @return {@link SelfViewPlayArea#startCard}.
     */
    public ImmStartCard getStartCard() {
        return startCard;
    }

    /**
     * Retrieves the map of played cards in the play area.
     * @return {@link SelfViewPlayArea#playedCards}.
     */
    public Map<Coordinate, ImmPlayableCard> getPlayedCards() {
        return playedCards;
    }

    /**
     * Retrieves the map of uncovered items in the play area.
     * @return {@link SelfViewPlayArea#uncoveredItems}.
     */
    public Map<Item, Integer> getUncoveredItems() {
        return uncoveredItems;
    }

    /**
     * Retrieves the selected card in the play area and its coordinate.
     * @return {@link SelfViewPlayArea#selectedCard}.
     */
    public Pair<Coordinate, ImmEvaluableCard> getSelectedCard() {
        return selectedCard;
    }

    /**
     * Returns the available positions where the player can place a card.
     * @return The list of available positions.
     */
    public List<Coordinate> getAvailablePos(){
        StringBuilder sb = new StringBuilder();
        sb.append("Player's available positions: \n");

        Coordinate[] arrayCoordinate = {
                new Coordinate(-1,1),
                new Coordinate(1,1),
                new Coordinate(1,-1),
                new Coordinate(-1,-1)
        };

        Set<Coordinate> okPos = new HashSet<>();
        Set<Coordinate> notOkPos = new HashSet<>();
        Coordinate startCoordinate = new Coordinate(0, 0);
        ImmStartCard sc = this.startCard;
        Item[] temp;
        int j;

        temp = !sc.isFlipped() ? sc.getFrontCorners() : sc.getBackCorners();
        notOkPos.add(startCoordinate);

        /*check startCard's corners*/
        for(CornerIndex i : CornerIndex.values()) {
            j = i.getIndex();
            if(temp[j] != Item.HIDDEN && temp[j] != Item.COVERED)
                okPos.add(startCoordinate.sum(arrayCoordinate[j]));
            else
                notOkPos.add(startCoordinate.sum(arrayCoordinate[j]));
        }

        for(Coordinate pos : this.playedCards.keySet()) {
            ImmPlayableCard currCard = this.playedCards.get(pos);
            for(CornerIndex i : CornerIndex.values()) {
                j = i.getIndex();
                if(currCard.getCorners()[j] != Item.HIDDEN && currCard.getCorners()[j] != Item.COVERED)
                    okPos.add(pos.sum(arrayCoordinate[j]));
                else
                    notOkPos.add(pos.sum(arrayCoordinate[j]));
            }
        }

        okPos.removeAll(notOkPos);
        return okPos.stream().toList();
    }

    /**
     * Returns a String representation of the available positions where the player can place a card.
     * @return The string representation of the available positions.
     */
    public String printAvailablePos() {
        List<Coordinate> availablePos = getAvailablePos();
        StringBuilder sb = new StringBuilder("  ");
        int num = 0;
        for(Coordinate i : availablePos){
            sb.append(i);
            num++;

            if(num == 4){
                sb.append("\n  ");
                num = 0;
            } else {
                sb.append(" - ");
            }
        }
        return sb.toString();
    }

    /**
     * Returns a String representation of the player's hand.
     * @return The string representation of the player's hand.
     */
    private String printHand() {
        int indent = 7;
        StringBuilder sb = new StringBuilder();
        sb.append("  Hand: \n");
        for (int i = 0; i < hand.size(); i++) {
            sb.append("    ")
                    .append(i + 1)
                    .append(") ")
                    .append(hand.get(i).printCard(indent));
        }
        return sb.toString();
    }

    /**
     * Returns a String representation of the played cards in the play area.
     * @return The string representation of the played cards.
     */
    private String printPlayedCards() {
        StringBuilder sb = new StringBuilder();
        StringBuilder cardString = new StringBuilder();
        int currIndent = 0;

        sb.append(AnsiCodes.BOLD + "PlayArea" + AnsiCodes.RESET + "\n  (0,0) -> ").append(startCard.getId()).append("\n");
        sb.append(startCard.printPlacedCard(11)).append("\n");
        for (Map.Entry<Coordinate, ImmPlayableCard> entry : playedCards.entrySet()) {
            cardString.setLength(0);
            Coordinate coordinate = entry.getKey();
            ImmPlayableCard card = entry.getValue();
            cardString.append("  ").append(coordinate).append(" -> ");
            currIndent = cardString.length();
            cardString.append(card).append("\n")
                    .append(card.printPlacedCard(currIndent));
            sb.append(cardString);
        }
        return sb.toString();
    }

    /**
     * Returns a String representation of the uncovered items in the play area.
     * @return The string representation of the uncovered items.
     */
    private String printUncoveredItems() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Uncovered Items:\n");
        for (Map.Entry<Item, Integer> entry : uncoveredItems.entrySet()) {
            sb.append("    - ")
                    .append(Item.itemToColor(entry.getKey()))
                    .append(" x")
                    .append(entry.getValue())
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a String representation of the player's play area.
     * @return The string representation of the player's play area.
     */
    public String printPlayArea() {
        return printPlayedCards() + "\n" + printUncoveredItems() + "\n" + printHand()  + "\n";
    }

    /**
     * Returns a String representation of the player's play area at the end of the game.
     * @return The string representation of the player's play area at the end of the game.
     */
    public String printEndedGamePlayArea() {
        return printPlayedCards() + "\n" + printUncoveredItems() + "\n";
    }
}
