package it.polimi.ingsw.viewModel;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.AnsiCodes;
import it.polimi.ingsw.viewModel.immutableCard.ImmObjectiveCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.viewPlayer.SelfViewPlayArea;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The EndedGameData class represents the state of the game after it has ended.
 * It includes information about the game, players, their play areas, secret objectives,
 * scoreboard, tokens, and the state of the decks. It also includes the results of the game,
 * which is a list of player usernames who were online when the game ended, ordered by their scores.
 */
public class EndedGameData implements Serializable, CardConverter {

    /**
     * The id of the current game.
     */
    private final String gameId;

    /**
     * The results of the game. This is a list of player usernames who were online when the game ended,
     * ordered by their scores.
     */
    private final List<String> results;

    /**
     * A map of player usernames to their play areas.
     * Only includes players who are online.
     */
    private final Map<String, SelfViewPlayArea> playAreas;

    /**
     * A map of player usernames to their secret objectives.
     * Only includes players who are online.
     */
    private final Map<String, ImmObjectiveCard> secretObjectives;

    /**
     * A map of player usernames to their scores.
     * Only includes players who are online.
     */
    private final Map<String, Integer> scoreboard;

    /**
     * A map of player usernames to their tokens.
     * Only includes players who are online.
     */
    private final Map<String, Token> playerTokens;

    /**
     * The top card of the resource deck.
     */
    private final Item topResourceDeck;

    /**
     * The top card of the gold deck.
     */
    private final Item topGoldDeck;

    /**
     * The array of visible resource cards.
     */
    private final ImmPlayableCard[] visibleResourceCards;

    /**
     * The array of visible gold cards.
     */
    private final ImmPlayableCard[] visibleGoldCards;

    /**
     * The common objectives.
     */
    private final ImmObjectiveCard[] commonObjectives;

    /**
     * Constructs an EndedGameData object from a Game model.
     * @param model The game model to construct the EndedGameData from.
     */
    public EndedGameData (Game model, List<Player> results){
        this.gameId = model.getId();
        this.results = results.stream()
                .filter(Player::isOnline)
                .map(Player::getUsername)
                .toList();
        this.playAreas = model.getPlayers().stream()
                .filter(Player::isOnline)
                .collect(Collectors.toUnmodifiableMap(
                        Player::getUsername,
                        player -> new SelfViewPlayArea(player.getPlayArea())
                ));
        this.secretObjectives = model.getPlayers().stream()
                .filter(Player::isOnline)
                .collect(Collectors.toUnmodifiableMap(
                        Player::getUsername,
                        player -> new ImmObjectiveCard(player.getSecretObjective())
                ));
        this.scoreboard = model.getScoreboard().entrySet().stream()
                .filter(entry -> entry.getKey().isOnline())
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getKey().getUsername(),
                        Map.Entry::getValue
                ));
        this.playerTokens = model.getPlayers().stream()
                .filter(Player::isOnline)
                .collect(Collectors.toUnmodifiableMap(
                        Player::getUsername,
                        Player::getToken
                ));
        this.commonObjectives = Arrays.stream(model.getCommonObjectives())
                .map(ImmObjectiveCard::new)
                .toArray(ImmObjectiveCard[]::new);
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
     * Converts the game results to a string representation.
     * @return A string representation of the game results.
     */
    public String resultsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Results:\n");
        for (int i = 0; i < results.size(); i++) {
            sb.append("  ")
                    .append(i + 1)
                    .append(") ")
                    .append(playerTokens.get(results.get(i)).getColorCode())
                    .append(results.get(i))
                    .append(AnsiCodes.RESET)
                    .append(" - ")
                    .append(scoreboard.get(results.get(i)))
                    .append(" points\n");
        }
        return sb.toString();
    }

    /**
     * Converts the given user's {@link SelfViewPlayArea} to a string representation.
     *
     * @param username The username of the user whose play area to convert to a string.
     * @return A string representation of the given user's {@link SelfViewPlayArea}.
     */
    public String playAreaToString(String username) {
        return playAreas.get(username).printEndedGamePlayArea() + objectiveCardsToString(username);
    }

    /**
     * Converts the objective cards to a string representation.
     *
     * @param username The username of the user whose objective cards to convert to a string.
     * @return A string representation of the objective cards.
     */
    private String objectiveCardsToString(String username) {
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
                .append(secretObjectives.get(username).printCard(5))
                .append("\n");
        return sb.toString();
    }

    /**
     * Converts the visible decks to a string representation.
     *
     * @return A string representation of the visible decks.
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
     * @return The id of the game.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Retrieves the results of the game. This is a list of player usernames who were online when the game ended,
     * ordered by their scores.
     *
     * @return A list of player usernames who were online at the end of the game, ordered by their scores.
     */
    public List<String> getResults() {
        return results;
    }

    /**
     * @return A map of player usernames to their play areas.
     */
    public Map<String, SelfViewPlayArea> getPlayAreas() {
        return playAreas;
    }

    /**
     * @return A map of player usernames to their secret objectives.
     */
    public Map<String, ImmObjectiveCard> getSecretObjectives() {
        return secretObjectives;
    }

    /**
     * @return A map of player usernames to their scores.
     */
    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * @return A map of player usernames to their tokens.
     */
    public Map<String, Token> getPlayerTokens() {
        return playerTokens;
    }

    /**
     * @return The top card of the resource deck.
     */
    public Item getTopResourceDeck() {
        return topResourceDeck;
    }

    /**
     * @return The top card of the gold deck.
     */
    public Item getTopGoldDeck() {
        return topGoldDeck;
    }

    /**
     * @return An array of the visible resource cards.
     */
    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    /**
     * @return An array of the visible gold cards.
     */
    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    /**
     * @return An array of the common objectives.
     */
    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }
}
