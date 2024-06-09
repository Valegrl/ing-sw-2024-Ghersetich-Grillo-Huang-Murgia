package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayer;
import it.polimi.ingsw.viewModel.viewPlayer.ViewPlayer;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ResourceCard;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ViewModel is a class that represents a view of the game model.
 * It provides a way to get the state of the game without exposing the actual model.
 */
public class ViewModel implements Serializable, CardConverter {
    /**
     * The id of the current game.
     */
    private final String gameId;

    /**
     * The top card of the resource deck.
     */
    private Item topResourceDeck;

    /**
     * The top card of the gold deck.
     */
    private Item topGoldDeck;

    /**
     * The array of visible resource cards.
     */
    private ImmPlayableCard[] visibleResourceCards;

    /**
     * The array of visible gold cards.
     */
    private ImmPlayableCard[] visibleGoldCards;

    /**
     * The Map of opponents.
     */
    private final Map<String, ViewPlayer> opponents;

    /**
     * The self-player.
     */
    private final SelfViewPlayer selfPlayer;

    /**
     * The list of player usernames.
     */
    private final List<String> playerUsernames;

    /**
     * The index of the player whose turn it is.
     */
    private int turnPlayerIndex;

    /**
     * The scoreboard.
     */
    private final Map<String, Integer> scoreboard;

    /**
     * The common objectives.
     */
    private final ImmObjectiveCard[] commonObjectives;

    /**
     * The game status.
     */
    private GameStatus gameStatus;

    /**
     * The flag that indicates if the last circle status has been activated.
     */
    private boolean detectedLC;

    /**
     * Constructor for ViewModel.
     * It initializes the ViewModel based on the given game model and self-player username.
     *
     * @param model the game model.
     * @param username the username of the self-player.
     */
    public ViewModel(Game model, String username) {
        this.gameId = model.getId();
        this.opponents = model.getPlayers().stream()
                .filter(player -> !player.getUsername().equals(username))
                .map(ViewPlayer::new)
                .collect(Collectors.toMap(ViewPlayer::getUsername, viewPlayer -> viewPlayer));
        this.selfPlayer = new SelfViewPlayer(model.getPlayerFromUsername(username));
        this.playerUsernames = model.getPlayerUsernames();
        this.turnPlayerIndex = model.getTurnPlayerIndex();
        this.scoreboard = model.getScoreboard().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().getUsername(),
                        Map.Entry::getValue
                ));
        this.commonObjectives = Arrays.stream(model.getCommonObjectives())
                .map(ImmObjectiveCard::new)
                .toArray(ImmObjectiveCard[]::new);
        this.gameStatus = model.getGameStatus();
        this.detectedLC = model.isDetectedLC();
        this.visibleResourceCards = Arrays.stream(model.getVisibleResourceCards())
                .map(this::convertToImmCardType)
                .toArray(ImmPlayableCard[]::new);
        this.visibleGoldCards = Arrays.stream(model.getVisibleGoldCards())
                .map(this::convertToImmCardType)
                .toArray(ImmPlayableCard[]::new);
        ResourceCard otherRC = model.seeResourceTopCard();
        this.topResourceDeck = otherRC == null ? null : otherRC.getPermanentResource();
        GoldCard otherGC = model.seeGoldTopCard();
        this.topGoldDeck = otherGC == null ? null : otherGC.getPermanentResource();
    }

    /**
     * Copy constructor for ViewModel.
     * It initializes the ViewModel based on the given ViewModel.
     *
     * @param vm the ViewModel to be copied.
     */
    public ViewModel(ViewModel vm) {
        this.gameId = vm.gameId;
        this.opponents = vm.opponents;
        this.selfPlayer = vm.selfPlayer;
        this.playerUsernames = vm.playerUsernames;
        this.turnPlayerIndex = vm.turnPlayerIndex;
        this.scoreboard = vm.scoreboard;
        this.commonObjectives = vm.commonObjectives;
        this.gameStatus = vm.gameStatus;
        this.detectedLC = vm.detectedLC;
        this.visibleResourceCards = vm.visibleResourceCards;
        this.visibleGoldCards = vm.visibleGoldCards;
        this.topResourceDeck = vm.topResourceDeck;
        this.topGoldDeck = vm.topGoldDeck;
    }

    /**
     * Converts the game's decks to a string representation.
     * @return A string representation of the decks.
     */
    public String decksToString() {
        int indent = 12;
        return "Decks: \n" +
                "  1- Gold deck:\n" +
                "       Top card: " +
                Optional.ofNullable(topGoldDeck)
                        .map(Item::itemToColor)
                        .orElse("empty deck") +
                "\n" +
                "       Visible cards:\n" +
                "         1- " +
                Optional.ofNullable(visibleGoldCards[0])
                        .map(card -> card.printCard(indent))
                        .orElse("empty") +
                "\n         2- " +
                Optional.ofNullable(visibleGoldCards[1])
                        .map(card -> card.printCard(indent))
                        .orElse("empty") +
                "\n  2- Resource deck:\n" +
                "       Top card: " +
                Optional.ofNullable(topResourceDeck)
                        .map(Item::itemToColor)
                        .orElse("empty deck") +
                "\n" +
                "       Visible cards: \n" +
                "         1- " +
                Optional.ofNullable(visibleResourceCards[0])
                        .map(card -> card.printCard(indent))
                        .orElse("empty") +
                "\n         2- " +
                Optional.ofNullable(visibleResourceCards[1])
                        .map(card -> card.printCard(indent))
                        .orElse("empty");
    }

    /**
     * Converts the objective cards (common and self-player secret objective) to a string representation.
     * @return A string representation of the objective cards.
     */
    public String objectiveCardsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Common Objectives:\n");
        for (int i = 0; i < commonObjectives.length; i++) {
            sb.append("  ")
                    .append(i + 1)
                    .append(") ")
                    .append(commonObjectives[i].printCard(5))
                    .append("\n");
        }
        sb.append("Private Objective:\n");
        sb.append("  ")
                .append("1) ")
                .append(selfPlayer.getSecretObjective().printCard(5))
                .append("\n");
        return sb.toString();
    }

    /**
     * Converts an opponent's play area to a string representation.
     * @param player The username of the chosen opponent.
     * @return A string representation of the opponent's play area.
     */
    public String opponentPlayAreaToString(String player) {
        return opponents.get(player).playAreaToString();
    }

    /**
     * Converts the self-player's play area to a string representation.
     * @return A string representation of the self-player's play area.
     */
    public String selfPlayAreaToString() {
        return selfPlayer.playAreaToString();
    }

    /**
     * Retrieves the game id.
     * @return {@link ViewModel#gameId}.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Retrieves the top card of the resource deck.
     * @return {@link ViewModel#topResourceDeck}.
     */
    public Item getTopResourceDeck() {
        return topResourceDeck;
    }

    /**
     * Retrieves the top card of the gold deck.
     * @return {@link ViewModel#topGoldDeck}.
     */
    public Item getTopGoldDeck() {
        return topGoldDeck;
    }

    /**
     * Retrieves the visible resource cards.
     * @return {@link ViewModel#visibleResourceCards}.
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * Retrieves the visible gold cards.
     * @return {@link ViewModel#visibleGoldCards}.
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    /**
     * Retrieves the map of opponents.
     * @return {@link ViewModel#opponents}.
     */
    public Map<String, ViewPlayer> getOpponentsMap() {
        return opponents;
    }

    /**
     * Retrieves an opponent.
     * @param username The username of the opponent to be retrieved.
     * @return The opponent with the given username.
     */
    public ViewPlayer getOpponent(String username) {
        return opponents.get(username);
    }

    /**
     * Retrieves the self-player.
     * @return {@link ViewModel#selfPlayer}.
     */
    public SelfViewPlayer getSelfPlayer() {
        return selfPlayer;
    }

    /**
     * Retrieves the list of player usernames.
     * @return {@link ViewModel#playerUsernames}.
     */
    public List<String> getPlayerUsernames() {
        return playerUsernames;
    }

    /**
     * Retrieves the index of the player whose turn it is.
     * @return {@link ViewModel#turnPlayerIndex}.
     */
    public int getTurnPlayerIndex() {
        return turnPlayerIndex;
    }

    /**
     * Retrieves the scoreboard.
     * @return {@link ViewModel#scoreboard}.
     */
    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * Retrieves the common objectives.
     * @return {@link ViewModel#commonObjectives}.
     */
    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    /**
     * Retrieves the game status.
     * @return {@link ViewModel#gameStatus}.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Retrieves the flag that indicates if the last circle status has been activated.
     * @return {@link ViewModel#detectedLC}.
     */
    public boolean isDetectedLC() {
        return detectedLC;
    }

    /**
     * Sets the top card of the resource deck.
     * @param topResourceDeck The card to be set.
     */
    public void setTopResourceDeck(Item topResourceDeck) {
        this.topResourceDeck = topResourceDeck;
    }

    /**
     * Sets the top card of the gold deck.
     * @param topGoldDeck The card to be set.
     */
    public void setTopGoldDeck(Item topGoldDeck) {
        this.topGoldDeck = topGoldDeck;
    }

    /**
     * Sets the visible resource cards.
     * @param visibleResourceCards The cards to be set.
     */
    public void setVisibleResourceCards(ImmPlayableCard[] visibleResourceCards) {
        this.visibleResourceCards = visibleResourceCards;
    }

    /**
     * Sets the visible gold cards.
     * @param visibleGoldCards The cards to be set.
     */
    public void setVisibleGoldCards(ImmPlayableCard[] visibleGoldCards) {
        this.visibleGoldCards = visibleGoldCards;
    }

    /**
     * Sets the index of the player whose turn it is.
     * @param turnPlayerIndex The index to be set.
     */
    public void setTurnPlayerIndex(int turnPlayerIndex) {
        this.turnPlayerIndex = turnPlayerIndex;
    }

    /**
     * Sets the game status.
     * @param gameStatus The new game status to be set.
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setDetectedLC(boolean detectedLC) {
        this.detectedLC = detectedLC;
    }

    public void setScoreboard(Map<String, Integer> scoreboard) {
        this.scoreboard.clear();
        this.scoreboard.putAll(scoreboard);
    }
}
