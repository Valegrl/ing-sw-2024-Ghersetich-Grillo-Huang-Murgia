package it.polimi.ingsw.immutableModel;

import it.polimi.ingsw.immutableModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.immutableModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ResourceCard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable representation of the Game class.
 * This class provides a read-only view of the game state, which can be safely shared across different threads.
 * All fields are final and initialized at construction time, and there are no setter methods.
 * The class also provides getter methods for all fields.
 *
 * @see it.polimi.ingsw.model.Game
 */
public final class ImmGame {
    /**
     * The unique identifier of the game.
     */
    private final String id;

    /**
     * The list of players in the game.
     */
    private final List<ImmPlayer> players;

    /**
     * The index of the player whose turn it is.
     */
    private final int turnPlayerIndex;

    /**
     * The scoreboard of the game.
     */
    private final Map<ImmPlayer, Integer> scoreboard;

    /**
     * The common objectives of the game.
     */
    private final ImmObjectiveCard[] commonObjectives;

    /**
     * The current status of the game.
     */
    private final GameStatus gameStatus;

    /**
     * The visible resource cards in the game.
     */
    private final ImmPlayableCard[] visibleResourceCards;

    /**
     * The visible gold cards in the game.
     */
    private final ImmPlayableCard[] visibleGoldCards;

    /**
     * The top card of the resource deck.
     */
    private final Item topResourceDeck;

    /**
     * The top card of the gold deck.
     */
    private final Item topGoldDeck;

    /**
     * Constructs an immutable representation of a game.
     *
     * @param game the game to represent
     */
    public ImmGame(Game game) {
        this.id = game.getId();
        this.players = game.getPlayers().stream()
                .map(ImmPlayer::new)
                .toList();
        this.turnPlayerIndex = game.getTurnPlayerIndex();
        this.scoreboard = game.getScoreboard().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> new ImmPlayer(entry.getKey()),
                        Map.Entry::getValue
                ));
        this.commonObjectives = Arrays.stream(game.getCommonObjectives())
                .map(ImmObjectiveCard::new)
                .toArray(ImmObjectiveCard[]::new);
        this.gameStatus = game.getGameStatus();
        this.visibleResourceCards = Arrays.stream(game.getVisibleResourceCards())
                .map(ImmPlayableCard::new)
                .toArray(ImmPlayableCard[]::new);
        this.visibleGoldCards = Arrays.stream(game.getVisibleGoldCards())
                .map(ImmPlayableCard::new)
                .toArray(ImmPlayableCard[]::new);
        ResourceCard otherRC = game.seeResourceTopCard();
        this.topResourceDeck = otherRC == null ? null : otherRC.getPermanentResource();
        GoldCard otherGC = game.seeGoldTopCard();
        this.topGoldDeck = otherGC == null ? null : otherGC.getPermanentResource();
    }

    /**
     * The ID is a unique string that identifies the game.
     *
     * @return the unique identifier of the game
     */
    public String getId() {
        return id;
    }

    /**
     * Each player is represented as an instance of the ImmPlayer class.
     *
     * @return the list of players in the game
     */
    public List<ImmPlayer> getPlayers() {
        return players;
    }

    /**
     * The index is a zero-based integer that represents the position of the player in the players list.
     *
     * @return the index of the player whose turn it is
     */
    public int getTurnPlayerIndex() {
        return turnPlayerIndex;
    }

    /**
     * The scoreboard is a map where the keys are the players and the values are their scores.
     *
     * @return the scoreboard of the game
     */
    public Map<ImmPlayer, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * The common objectives are represented as an array of ImmObjectiveCard instances.
     *
     * @return the common objectives of the game
     */
    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * The status is represented as an instance of the GameStatus enum.
     *
     * @return the current status of the game
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * The visible resource cards are represented as an array of ImmPlayableCard instances.
     *
     * @return the visible resource cards in the game
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * The visible gold cards are represented as an array of ImmPlayableCard instances.
     *
     * @return the visible gold cards in the game
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    /**
     * The top card is represented as an instance of the Item class.
     *
     * @return the top card of the resource deck
     */
    public Item getTopResourceDeck() {
        return topResourceDeck;
    }

    /**
     * The top card is represented as an instance of the Item class.
     *
     * @return the top card of the gold deck
     */
    public Item getTopGoldDeck() {
        return topGoldDeck;
    }
}
