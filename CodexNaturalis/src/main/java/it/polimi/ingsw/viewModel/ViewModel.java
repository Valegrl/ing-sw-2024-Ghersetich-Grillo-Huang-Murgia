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
import java.util.stream.Collectors;

public class ViewModel implements Serializable {
    private final String gameId;

    private Item topResourceDeck;

    private Item topGoldDeck;

    private ImmPlayableCard[] visibleResourceCards;

    private ImmPlayableCard[] visibleGoldCards;

    private final List<ViewPlayer> opponents;

    private final SelfViewPlayer selfPlayer;

    private int turnPlayerIndex;

    private final Map<String, Integer> scoreboard;

    private final ImmObjectiveCard[] commonObjectives;

    private GameStatus gameStatus;

    public ViewModel(Game model, String username) {
        this.gameId = model.getId();
        this.opponents = model.getPlayers().stream()
                .filter(player -> !player.getUsername().equals(username))
                .map(ViewPlayer::new)
                .toList();
        this.selfPlayer = new SelfViewPlayer(model.getPlayerFromUsername(username));
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
        this.visibleResourceCards = Arrays.stream(model.getVisibleResourceCards())
                .map(ImmPlayableCard::new)
                .toArray(ImmPlayableCard[]::new);
        this.visibleGoldCards = Arrays.stream(model.getVisibleGoldCards())
                .map(ImmPlayableCard::new)
                .toArray(ImmPlayableCard[]::new);
        ResourceCard otherRC = model.seeResourceTopCard();
        this.topResourceDeck = otherRC == null ? null : otherRC.getPermanentResource();
        GoldCard otherGC = model.seeGoldTopCard();
        this.topGoldDeck = otherGC == null ? null : otherGC.getPermanentResource();
    }

    public String getGameId() {
        return gameId;
    }

    public Item getTopResourceDeck() {
        return topResourceDeck;
    }

    public Item getTopGoldDeck() {
        return topGoldDeck;
    }

    public ImmPlayableCard[] getVisibleResourceCards() {
        return visibleResourceCards;
    }

    public ImmPlayableCard[] getVisibleGoldCards() {
        return visibleGoldCards;
    }

    public List<ViewPlayer> getOpponents() {
        return opponents;
    }

    public SelfViewPlayer getSelfPlayer() {
        return selfPlayer;
    }

    public int getTurnPlayerIndex() {
        return turnPlayerIndex;
    }

    public Map<String, Integer> getScoreboard() {
        return scoreboard;
    }

    public ImmObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setTopResourceDeck(Item topResourceDeck) {
        this.topResourceDeck = topResourceDeck;
    }

    public void setTopGoldDeck(Item topGoldDeck) {
        this.topGoldDeck = topGoldDeck;
    }

    public void setVisibleResourceCards(ImmPlayableCard[] visibleResourceCards) {
        this.visibleResourceCards = visibleResourceCards;
    }

    public void setVisibleGoldCards(ImmPlayableCard[] visibleGoldCards) {
        this.visibleGoldCards = visibleGoldCards;
    }

    public void setTurnPlayerIndex(int turnPlayerIndex) {
        this.turnPlayerIndex = turnPlayerIndex;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
